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
        //chain.add(createGenesisBlock());

        // Premint 100 million GAF to founder address
        String founderAddress = "GAF_FOUNDER";
        balances.put(founderAddress, new BigDecimal("100000000"));

        // Optional: add a transaction from SYSTEM to founder
        Transaction genesisTx = new Transaction("SYSTEM", founderAddress, new BigDecimal("100000000"));
        long timestamp = System.currentTimeMillis();
        Block genesisBlock = new Block(0, timestamp, List.of(genesisTx), "0");
        chain.add(genesisBlock);
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
        Block newBlock = new Block(
                getLatestBlock().getIndex() + 1,
                System.currentTimeMillis(),
                new ArrayList<>(pendingTransactions),
                getLatestBlock().getHash()
        );
        addBlock(newBlock);
        pendingTransactions.clear();
    }
}
