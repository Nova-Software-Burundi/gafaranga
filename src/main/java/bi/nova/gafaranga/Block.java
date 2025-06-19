package bi.nova.gafaranga;

import java.util.List;

public class Block {
    private int index;
    private long timestamp;
    private List<Transaction> transactions;
    private String previousHash;
    private String hash;
    private int nonce;

    public Block(int index, long timestamp, List<Transaction> transactions, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    private String calculateHash() {
        // You can improve this later
        return StringUtil.applySha256(index + previousHash + timestamp + transactions.toString() + nonce);
    }

    public String getHash() {
        return hash;
    }

    public int getIndex() {
        return index;
    }

    // Other getters as needed...


    public List<Transaction> getTransactions() {
        return transactions;
    }
}
