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

            int index = chain.size();
            long timestamp = System.currentTimeMillis();
            Block lastBlock = chain.get(chain.size() - 1);
            String previousHash = lastBlock.getHash();

            Block newBlock = new Block(index, timestamp, new ArrayList<>(pendingTransactions), previousHash);

        System.out.println("Pending tx: " + pendingTransactions.size());

            // Apply transactions
            for (Transaction tx : pendingTransactions) {
                if (!tx.sender.equals("SYSTEM")) {
                    BigDecimal senderBalance = balances.getOrDefault(tx.sender, BigDecimal.ZERO);
                    if (senderBalance.compareTo(tx.amount) < 0) {
                        System.out.println("Insufficient funds, skipping transaction");
                        continue;
                    }
                    balances.put(tx.sender, senderBalance.subtract(tx.amount));
                }

                BigDecimal receiverBalance = balances.getOrDefault(tx.recipient, BigDecimal.ZERO);
                balances.put(tx.recipient, receiverBalance.add(tx.amount));
            }

            chain.add(newBlock);
            pendingTransactions.clear();
        }
}
