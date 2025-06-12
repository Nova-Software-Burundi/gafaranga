package bi.nova.gafaranga;

public class Block {
    private int index;
    private long timestamp;
    private String data;
    private String previousHash;
    private String hash;

    public Block(int index, long timestamp, String data, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    private String calculateHash() {
        String input = index + Long.toString(timestamp) + data + previousHash;
        return StringUtil.applySha256(input); // You must have a utility for hashing
    }

    public String getHash() {
        return hash;
    }

    public int getIndex() {
        return index;
    }

    // Other getters as needed...
}
