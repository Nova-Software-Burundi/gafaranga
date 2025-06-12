package bi.nova.gafaranga;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class BlockchainApi {

    private Blockchain blockchain;

    public BlockchainApi(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public void startServer() {
        Javalin app = Javalin.create().start(7000);

        app.get("/chain", ctx -> ctx.json(blockchain.getChain()));
        app.get("/block/{index}", this::getBlockByIndex);
        app.post("/mine", this::mineBlock);
    }

    private void getBlockByIndex(Context ctx) {
        int index = Integer.parseInt(ctx.pathParam("index"));
        List<Block> chain = blockchain.getChain();
        if (index >= 0 && index < chain.size()) {
            ctx.json(chain.get(index));
        } else {
            ctx.status(404).result("Block not found");
        }
    }

    private void mineBlock(Context ctx) {
        String data = ctx.body();
        Block lastBlock = blockchain.getLatestBlock();
        Block newBlock = new Block(
                lastBlock.getIndex() + 1,
                System.currentTimeMillis(),
                data,
                lastBlock.getHash()
        );
        blockchain.addBlock(newBlock);
        ctx.json(newBlock);
    }
}
