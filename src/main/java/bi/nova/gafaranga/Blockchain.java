package bi.nova.gafaranga;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;
    private List<Transaction> pendingTransactions = new ArrayList<>();

    public Blockchain() {
        chain = new ArrayList<>();
        chain.add(createGenesisBlock());
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
