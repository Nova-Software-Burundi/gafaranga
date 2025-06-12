package bi.nova.gafaranga;

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        blockchain.addBlock(new Block(1, System.currentTimeMillis(), "Genesis data", blockchain.getLatestBlock().getHash()));

        BlockchainApi api = new BlockchainApi(blockchain);
        api.startServer();
    }
}
