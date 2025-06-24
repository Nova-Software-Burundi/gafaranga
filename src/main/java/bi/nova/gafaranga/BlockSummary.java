package bi.nova.gafaranga;

public class BlockSummary {
    public int index;
    public long timestamp;
    public int txCount;
    public String hash;

    public BlockSummary(int index, long timestamp, int txCount, String hash) {
        this.index = index;
        this.timestamp = timestamp;
        this.txCount = txCount;
        this.hash = hash;
    }
}
