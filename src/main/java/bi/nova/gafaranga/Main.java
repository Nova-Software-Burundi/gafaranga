package bi.nova.gafaranga;

public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        BlockchainApi api = new BlockchainApi(blockchain);
        api.startServer();
    }
}
