package bi.nova.gafaranga;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blockchain {
    private List<Block> chain;
    private List<Transaction> pendingTransactions = new ArrayList<>();
    private Map<String, BigDecimal> balances = new HashMap<>();
    private String minerAddress = "GAF_MINER"; // To be set via constructor later

    public Blockchain() {
        chain = new ArrayList<>();
        Transaction genesisTx = new Transaction("SYSTEM", "GAF_FOUNDER", new BigDecimal("100000000"));
        List<Transaction> genesisTxs = new ArrayList<>();
        genesisTxs.add(genesisTx);

        Block genesis = new Block(0, System.currentTimeMillis(), genesisTxs, "0");
        chain.add(genesis);

        balances.put("GAF_FOUNDER", new BigDecimal("100000000"));
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
        pendingTransactions.add(tx);
    }

    public void mineBlock() {
        if (pendingTransactions.isEmpty()) {
            System.out.println("No transactions to mine.");
            return;
        }

        // Add mining reward
        Transaction rewardTx = new Transaction("SYSTEM", minerAddress, new BigDecimal("1"));
        List<Transaction> blockTxs = new ArrayList<>();
        blockTxs.add(rewardTx);
        blockTxs.addAll(pendingTransactions);

        int index = chain.size();
        long timestamp = System.currentTimeMillis();
        String previousHash = chain.get(chain.size() - 1).getHash();
        Block newBlock = new Block(index, timestamp, blockTxs, previousHash);

        // Apply all transactions (reward + pending)
        for (Transaction tx : blockTxs) {
            if (!tx.getSender().equals("SYSTEM")) {
                BigDecimal senderBalance = balances.getOrDefault(tx.getSender(), BigDecimal.ZERO);
                if (senderBalance.compareTo(tx.getAmount()) < 0) {
                    System.out.println("Skipped tx: insufficient funds from " + tx.getSender());
                    continue;
                }
                balances.put(tx.getSender(), senderBalance.subtract(tx.getAmount()));
            }

            BigDecimal receiverBalance = balances.getOrDefault(tx.getRecipient(), BigDecimal.ZERO);
            balances.put(tx.getRecipient(), receiverBalance.add(tx.getAmount()));
        }

        chain.add(newBlock);
        pendingTransactions.clear();
        }
}
