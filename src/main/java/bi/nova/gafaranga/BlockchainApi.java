package bi.nova.gafaranga;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BlockchainApi {

    private Blockchain blockchain;

    public BlockchainApi(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public void startServer() {
        Javalin app = Javalin.create().start(7000);

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

        if ("SYSTEM".equalsIgnoreCase(tx.getSender())) {
            ctx.status(403).result("SYSTEM address is reserved and cannot be used for transactions.");
            return;
        }

        BigDecimal senderBalance = blockchain.getBalance(tx.getSender());

        if (senderBalance.compareTo(tx.getAmount()) < 0) {
            ctx.status(400).result("Insufficient funds for transaction.");
            return;
        }

        blockchain.addTransaction(tx);
        ctx.status(201).result("Transaction added");
    }


    private void mineBlock(Context ctx) {
        blockchain.mineBlock();
        ctx.result("Block mined successfully");
    }
}
