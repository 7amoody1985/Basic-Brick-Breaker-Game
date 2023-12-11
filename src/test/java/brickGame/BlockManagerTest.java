package brickGame;

import entities.Block;
import game.Game;
import game.GameEngine;
import javafx.stage.Stage;
import managers.BlockManager;
import org.junit.jupiter.api.Test;
import ui.Sound;
import ui.UI;

import static org.junit.jupiter.api.Assertions.*;

public class BlockManagerTest {
    @Test
    void shouldCreateBlocksWhenBlockManagerIsConstructed() {
        Game game = new Game();
        UI ui = new UI(new Stage(), new Sound(), game);
        BlockManager blockManager = new BlockManager(game, ui, 5);
        assertFalse(blockManager.getBlocks().isEmpty());
    }

    @Test
    void shouldClearBlocksWhenClearBlocksIsCalled() {
        Game game = new Game();
        UI ui = new UI(new Stage(), new Sound(), game);
        BlockManager blockManager = new BlockManager(game, ui, 5);
        blockManager.clearBlocks();
        assertTrue(blockManager.getBlocks().isEmpty());
    }

    @Test
    void shouldNotCreateHeartBlockWhenOneAlreadyExists() {
        Game game = new Game();
        UI ui = new UI(new Stage(), new Sound(), game);
        BlockManager blockManager = new BlockManager(game, ui, 5);
        blockManager.isExistHeartBlock = true;
        blockManager.getBlocks().forEach(block -> assertNotEquals(Block.Type.HEART.ordinal(), block.type));
    }

    @Test
    void shouldIncreaseDestroyedBlockCountWhenBlockIsDestroyed() {
        Game game = new Game();
        UI ui = new UI(new Stage(), new Sound(), game);
        BlockManager blockManager = new BlockManager(game, ui, 5);
        blockManager.destroyedBlockCount = 0;
        blockManager.getBlocks().get(0).isDestroyed = true;
        blockManager.checkDestroyedCount(new GameEngine(ui));
        assertEquals(1, blockManager.destroyedBlockCount);
    }

    @Test
    void shouldNotIncreaseDestroyedBlockCountWhenBlockIsNotDestroyed() {
        Game game = new Game();
        UI ui = new UI(new Stage(), new Sound(), game);
        BlockManager blockManager = new BlockManager(game, ui, 5);
        blockManager.destroyedBlockCount = 0;
        blockManager.checkDestroyedCount(new GameEngine(ui));
        assertEquals(0, blockManager.destroyedBlockCount);
    }
}
