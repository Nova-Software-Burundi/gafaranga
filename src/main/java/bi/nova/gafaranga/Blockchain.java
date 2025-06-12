package bi.nova.gafaranga;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    public static List<Block> chain = new ArrayList<>();
    public static int difficulty = 3;

    public static void addBlock(Block block) {
        block.mineBlock(difficulty);
        chain.add(block);
        // store to LevelDB here
    }

    public static Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public static void initializeChain() {
        Block genesis = new Block("Genesis Block", "0");
        addBlock(genesis);
    }
}
