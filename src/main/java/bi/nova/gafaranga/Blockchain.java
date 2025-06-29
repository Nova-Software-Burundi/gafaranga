package bi.nova.gafaranga;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static bi.nova.gafaranga.BlockChainConfig.FOUNDER_ADDRESS;
import static bi.nova.gafaranga.BlockChainConfig.SYSTEM_ADDRESS;

public class Blockchain {
    private List<Block> chain;
    private List<Transaction> pendingTransactions = new ArrayList<>();
    private Map<String, BigDecimal> balances = new HashMap<>();
    private String minerAddress = "GAF_MINER"; // To be set via constructor later

    private final Map<String, Boolean> faucetHistory = new HashMap<>();

    public static final BigDecimal TRANSACTION_FEE = BlockChainConfig.TRANSACTION_FEE;

    private Map<String, String> walletSecrets = new HashMap<>();

    // For test/demo use only
    public void registerWallet(String address, String privateKey) {
        walletSecrets.put(address, privateKey);
    }

    public boolean canUseFaucet(String address) {
        return !faucetHistory.containsKey(address);
    }

    public void useFaucet(String address) {
        faucetHistory.put(address, true);
        addTransaction(new Transaction("GAF_FOUNDER", address, new BigDecimal(1000)));
    }

    public Blockchain() {
        chain = new ArrayList<>();
        Transaction genesisTx = new Transaction(SYSTEM_ADDRESS, FOUNDER_ADDRESS, new BigDecimal("100000000"));
        List<Transaction> genesisTxs = new ArrayList<>();
        genesisTxs.add(genesisTx);

        Block genesis = new Block(0, System.currentTimeMillis(), genesisTxs, "0");
        chain.add(genesis);

        balances.put("GAF_FOUNDER", new BigDecimal("100000000"));
    }

    public List<BlockSummary> getBlockSummaries() {
        return chain.stream()
                .map(block -> new BlockSummary(
                        block.getIndex(),
                        block.getTimestamp(),
                        block.getTransactions().size(),
                        block.getHash().substring(0, Math.min(10, block.getHash().length()))
                ))
                .collect(Collectors.toList());
    }

    public Transaction getTransactionById(String txId) {
        for (Block block : chain) {
            for (Transaction tx : block.getTransactions()) {
                if (tx.getTxId().equals(txId)) {
                    return tx;
                }
            }
        }
        return null;
    }

    public BigDecimal getBalance(String address) {
        return balances.getOrDefault(address, BigDecimal.ZERO);
    }

    private Block createGenesisBlock() {
        return new Block(0, System.currentTimeMillis(), pendingTransactions, "0");
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Block newBlock) {
        chain.add(newBlock);
    }

    public List<Block> getChain() {
        return chain;
    }

    public void addTransaction(Transaction tx) {
        if (tx.getSender().equals("GAF_FOUNDER")) {
            // System account — no signature check needed
            pendingTransactions.add(tx);
        } else {
            // Simulated check with secret map (insecure on purpose for now)
            String fakePrivateKey = walletSecrets.get(tx.getSender());
            if (fakePrivateKey == null) {
                throw new RuntimeException("Unknown wallet or missing private key");
            }

            boolean valid = SignatureUtil.verifyTransaction(tx.getSender(), tx.getRecipient(), tx.getAmount(), tx.getSignature(), fakePrivateKey);
            System.out.println("Sender: " + tx.getSender());
            System.out.println("Recipient: " + tx.getRecipient());
            System.out.println("Amount: " + tx.getAmount());
            System.out.println("Signature: " + tx.getSignature());
            System.out.println("Priv Key: " + fakePrivateKey);

            if (!valid) {
                throw new RuntimeException("Invalid signature");
            }

            pendingTransactions.add(tx);
        }
    }


    public void mineBlock() {
        if (pendingTransactions.isEmpty()) {
            System.out.println("No transactions to mine.");
            return;
        }

        // Add mining reward
        Transaction rewardTx = new Transaction(SYSTEM_ADDRESS, minerAddress, new BigDecimal("1"));
        List<Transaction> blockTxs = new ArrayList<>();
        blockTxs.add(rewardTx);
        blockTxs.addAll(pendingTransactions);

        int index = chain.size();
        long timestamp = System.currentTimeMillis();
        String previousHash = chain.get(chain.size() - 1).getHash();
        Block newBlock = new Block(index, timestamp, blockTxs, previousHash);

        // Apply all transactions (reward + pending)
        for (Transaction tx : blockTxs) {
            if (!SYSTEM_ADDRESS.equals(tx.getSender())) {
                BigDecimal senderBalance = balances.getOrDefault(tx.getSender(), BigDecimal.ZERO);
                BigDecimal totalDeduction = tx.getAmount().add(TRANSACTION_FEE);

                if (senderBalance.compareTo(totalDeduction) < 0) {
                    System.out.println("Skipped tx: insufficient funds from " + tx.getSender());
                    continue;
                }

                balances.put(tx.getSender(), senderBalance.subtract(totalDeduction));

                // Add fee to miner
                BigDecimal minerBalance = balances.getOrDefault(minerAddress, BigDecimal.ZERO);
                balances.put(minerAddress, minerBalance.add(TRANSACTION_FEE));
            }

            BigDecimal receiverBalance = balances.getOrDefault(tx.getRecipient(), BigDecimal.ZERO);
            balances.put(tx.getRecipient(), receiverBalance.add(tx.getAmount()));
        }

        chain.add(newBlock);
        pendingTransactions.clear();
        }

    public Map<String, BigDecimal> getAllBalances() {
        return Collections.unmodifiableMap(balances);
    }

    public List<Transaction> getPendingTransactions() {
        return Collections.unmodifiableList(pendingTransactions);
    }

    public BigDecimal getTotalSupply() {
        return balances.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Block getBlockByHash(String hash) {
        for (Block block : chain) {
            if (block.getHash().equals(hash)) {
                return block;
            }
        }
        return null;
    }

    public List<Transaction> getTransactionsForAddress(String address) {
        List<Transaction> result = new ArrayList<>();
        for (Block block : chain) {
            for (Transaction tx : block.getTransactions()) {
                if (address.equals(tx.getSender()) || address.equals(tx.getRecipient())) {
                    result.add(tx);
                }
            }
        }
        return result;
    }

    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();

        int chainHeight = chain.size();
        long totalTransactions = chain.stream()
                .mapToLong(block -> block.getTransactions().size())
                .sum();

        Set<String> uniqueWallets = new HashSet<>();
        chain.forEach(block -> {
            for (Transaction tx : block.getTransactions()) {
                uniqueWallets.add(tx.getSender());
                uniqueWallets.add(tx.getRecipient());
            }
        });

        long latestTimestamp = chain.isEmpty()
                ? 0
                : chain.get(chainHeight - 1).getTimestamp();

        status.put("chainHeight", chainHeight);
        status.put("totalTransactions", totalTransactions);
        status.put("uniqueWallets", uniqueWallets.size());
        status.put("latestBlockTime", latestTimestamp);

        return status;
    }

}
