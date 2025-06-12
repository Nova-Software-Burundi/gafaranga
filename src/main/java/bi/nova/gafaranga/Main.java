package bi.nova.gafaranga;

public class Main {
    public static void main(String[] args) {
        DBManager.init();
        Blockchain.initializeChain();
        Blockchain.addBlock(new Block("First real block", Blockchain.getLatestBlock().hash));
        Blockchain.addBlock(new Block("Second block", Blockchain.getLatestBlock().hash));
        DBManager.close();
    }
}