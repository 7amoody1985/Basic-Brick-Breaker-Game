package brickGame;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockManager {
    public final Color[] colors = new Color[]{Color.MAGENTA, Color.RED, Color.GOLD, Color.CORAL, Color.AQUA, Color.VIOLET, Color.GREENYELLOW, Color.ORANGE, Color.PINK, Color.SLATEGREY, Color.YELLOW, Color.TOMATO, Color.TAN,};
    private final List<Block> blocks;
    private final Game game;
    public boolean isExistHeartBlock = false;
    public int destroyedBlockCount = 0;

    public BlockManager(Game game) {
        this.game = game;
        this.blocks = new ArrayList<>();
        if (game.level <= game.finalLevel) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < game.level; j++) {
                    int r = new Random().nextInt(500);
                    int type;
                    if (r % 10 == 1) {
                        type = Block.Type.CHOCO.ordinal();
                    } else if (r % 10 == 2) {
                        if (!isExistHeartBlock) {
                            type = Block.Type.HEART.ordinal();
                            isExistHeartBlock = true;
                        } else {
                            type = Block.Type.NORMAL.ordinal();
                        }
                    } else if (r % 10 == 3) {
                        type = Block.Type.STAR.ordinal();
                    } else {
                        type = Block.Type.NORMAL.ordinal();
                    }
                    blocks.add(new Block(j, i, colors[r % (colors.length)], type));
                }
            }
        }
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

    public void clearBlocks() {
        this.blocks.clear();
    }

    public void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            //TODO win level todo...
            System.out.println("You Win");
            game.nextLevel();
        }
    }
}