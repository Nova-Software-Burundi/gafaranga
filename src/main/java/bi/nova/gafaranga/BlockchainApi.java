package bi.nova.gafaranga;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static bi.nova.gafaranga.BlockChainConfig.SYSTEM_ADDRESS;

public class BlockchainApi {

    private Blockchain blockchain;

    public BlockchainApi(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public void startServer() {
        Javalin app = Javalin.create(config -> {
            // Serve static files (e.g., HTML, CSS, JS) under /explorer
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/explorer";
                staticFileConfig.directory = "/public/explorer"; // maps to src/main/resources/public/explorer
                staticFileConfig.location = Location.CLASSPATH;
            });
        }).start(7000);

        app.get("/chain", ctx -> ctx.json(blockchain.getChain()));
        app.get("/block/{index}", this::getBlockByIndex);
        app.post("/mine", this::mineBlock);
        app.post("/transaction", this::createTransaction);
        app.get("/balance/{address}", ctx -> {
            String address = ctx.pathParam("address");
            ctx.result(blockchain.getBalance(address).toPlainString());
        });
        app.get("/balances", ctx -> ctx.json(blockchain.getAllBalances()));
        app.get("/transactions/{address}", this::getTransactionsForAddress);
        app.get("/block/hash/{hash}", this::getBlockByHash);
        app.get("/transaction/{id}", this::getTransactionById);
        app.get("/mempool", ctx -> ctx.json(blockchain.getPendingTransactions()));
        app.get("/supply", ctx -> ctx.json(Map.of("totalSupply", blockchain.getTotalSupply())));
        app.get("/wallet/new", ctx -> {
            String address = StringUtil.generateWalletAddress();
            String privateKey = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            blockchain.registerWallet(address, privateKey);

            ctx.json(Map.of(
                    "address", address,
                    "privateKey", privateKey,
                    "note", "This key is required to sign transactions. Keep it secure."
            ));
        });
        app.get("/explorer", ctx -> ctx.json(blockchain.getBlockSummaries()));
        app.get("/status", ctx -> ctx.json(blockchain.getStatus()));
        app.post("/faucet", this::faucet);
        app.get("/transactions", ctx -> {
            List<Transaction> allTxs = blockchain.getChain().stream()
                    .flatMap(block -> block.getTransactions().stream())
                    .collect(Collectors.toList());
            ctx.json(allTxs);
        });
    }

    private void faucet(Context ctx) {
        Map<String, String> request = ctx.bodyAsClass(Map.class);
        String address = request.get("address");

        if (address == null || !address.startsWith("wallet_")) {
            ctx.status(400).json(Map.of("status", "error", "message", "Invalid wallet address format"));
            return;
        }

        if (blockchain.canUseFaucet(address)) {
            blockchain.useFaucet(address);
            ctx.status(200).json(Map.of("status", "ok", "message", "1000 GAF sent to " + address));
        } else {
            ctx.status(429).json(Map.of("status", "error", "message", "Faucet already used by this address"));
        }
    }


    private void getTransactionById(Context ctx) {
        String id = ctx.pathParam("id");
        Transaction tx = blockchain.getTransactionById(id);
        if (tx != null) {
            ctx.json(tx);
        } else {
            ctx.status(404).result("Transaction not found");
        }
    }

    private void getBlockByHash(Context ctx) {
        String hash = ctx.pathParam("hash");
        Block result = blockchain.getBlockByHash(hash);
        if (result != null) {
            ctx.json(result);
        } else {
            ctx.status(404).result("Block not found");
        }
    }

    private void getTransactionsForAddress(Context ctx) {
        String address = ctx.pathParam("address");
        List<Transaction> txs = blockchain.getTransactionsForAddress(address);
        ctx.json(txs);
    }

    private void getBlockByIndex(Context ctx) {
        int index = Integer.parseInt(ctx.pathParam("index"));
        List<Block> chain = blockchain.getChain();
        if (index >= 0 && index < chain.size()) {
            ctx.json(chain.get(index));
        } else {
            ctx.status(404).result("Block not found");
        }
    }
    private void createTransaction(Context ctx) {
        Transaction tx = ctx.bodyAsClass(Transaction.class);

        if (!isValidAddress(tx.getSender()) || !isValidAddress(tx.getRecipient())) {
            ctx.status(400).result("Invalid wallet address format.");
            return;
        }

        if ("SYSTEM".equalsIgnoreCase(tx.getSender()) || "GAF_FOUNDER".equalsIgnoreCase(tx.getSender())) {
            ctx.status(403).result("Reserved address: transaction from SYSTEM or GAF_FOUNDER is not allowed.");
            return;
        }

        if (SYSTEM_ADDRESS.equalsIgnoreCase(tx.getSender())) {
            ctx.status(403).result("SYSTEM address is reserved and cannot be used for transactions.");
            return;
        }

        // Force generation of txId after deserialization
        if (tx.getTxId() == null || tx.getTxId().isEmpty()) {
            tx.setTxId(tx.generateTxId());
        }

        BigDecimal senderBalance = blockchain.getBalance(tx.getSender());
        BigDecimal total = tx.getAmount().add(Blockchain.TRANSACTION_FEE);
        if (senderBalance.compareTo(total) < 0) {
            ctx.status(400).result("Insufficient funds for transaction and fee.");
            return;
        }

        blockchain.addTransaction(tx);
        ctx.status(201).result("Transaction added");
    }

    private boolean isValidAddress(String addr) {
        return addr != null && addr.matches("^wallet_[a-zA-Z0-9]{4,40}$");
    }

    private void mineBlock(Context ctx) {
        blockchain.mineBlock();
        ctx.result("Block mined successfully");
    }
}
