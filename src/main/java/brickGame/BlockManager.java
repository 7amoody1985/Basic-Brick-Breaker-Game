package brickGame;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The BlockManager class is responsible for managing the blocks in the game.
 * It handles the creation, storage, and destruction of blocks.
 */
public class BlockManager {
    public final Color[] colors = new Color[]{Color.MAGENTA, Color.RED, Color.GOLD, Color.CORAL, Color.AQUA, Color.VIOLET, Color.GREENYELLOW, Color.ORANGE, Color.PINK, Color.SLATEGREY, Color.YELLOW, Color.TOMATO, Color.TAN,};
    private final List<Block> blocks;
    private final UI ui;
    public boolean isExistHeartBlock = false;
    public int destroyedBlockCount = 0;

    /**
     * Constructs a new BlockManager object.
     *
     * @param game           the game instance.
     * @param ui             the UI instance.
     * @param horizontalSize the number of blocks in a row.
     */
    public BlockManager(Game game, UI ui, int horizontalSize) {
        this.ui = ui;
        this.blocks = new ArrayList<>();
        if (game.level <= game.finalLevel) {
            for (int i = 0; i < horizontalSize; i++) {
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

    /**
     * Returns the list of blocks.
     *
     * @return the list of blocks.
     */
    public List<Block> getBlocks() {
        return this.blocks;
    }

    /**
     * Clears the list of blocks.
     */
    public void clearBlocks() {
        this.blocks.clear();
    }

    /**
     * Checks if all blocks are destroyed.
     * If all blocks are destroyed, it prints "You Win" and moves to the next menu.
     *
     * @param engine the game engine instance.
     */
    public void checkDestroyedCount(GameEngine engine) {
        if (destroyedBlockCount == blocks.size()) {
            System.out.println("You Win");
            ui.nextMenu(engine);
        }
    }
}