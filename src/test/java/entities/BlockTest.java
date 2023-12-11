package entities;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlockTest {
    @Test
    void shouldDrawBlockWithCorrectDimensionsAndPosition() {
        Block block = new Block(1, 1, Color.RED, Block.Type.NORMAL.ordinal());
        assertEquals(Block.getWidth(), block.rect.getWidth());
        assertEquals(Block.getHeight(), block.rect.getHeight());
        assertEquals((Block.getWidth()) + Block.getPaddingH(), block.rect.getX());
        assertEquals((Block.getHeight()) + Block.getPaddingTop(), block.rect.getY());
    }

    @Test
    void shouldNotHitWhenBallIsFarFromBlock() {
        Block block = new Block(1, 1, Color.RED, Block.Type.NORMAL.ordinal());
        assertEquals(Block.HitDirection.NO_HIT.ordinal(), block.checkHitToBlock(500, 500, 10));
    }

    @Test
    void shouldHitTopWhenBallIsAboveBlock() {
        Block block = new Block(1, 1, Color.RED, Block.Type.NORMAL.ordinal());
        assertEquals(Block.HitDirection.TOP.ordinal(), block.checkHitToBlock(block.x + (double) Block.getWidth() / 2, block.y - 10, 10));
    }

    @Test
    void shouldHitBottomWhenBallIsBelowBlock() {
        Block block = new Block(1, 1, Color.RED, Block.Type.NORMAL.ordinal());
        assertEquals(Block.HitDirection.BOTTOM.ordinal(), block.checkHitToBlock(block.x + (double) Block.getWidth() / 2, block.y + Block.getHeight() + 10, 10));
    }

    @Test
    void shouldHitLeftWhenBallIsLeftOfBlock() {
        Block block = new Block(1, 1, Color.RED, Block.Type.NORMAL.ordinal());
        assertEquals(Block.HitDirection.LEFT.ordinal(), block.checkHitToBlock(block.x - 10, block.y + (double) Block.getHeight() / 2, 10));
    }

    @Test
    void shouldHitRightWhenBallIsRightOfBlock() {
        Block block = new Block(1, 1, Color.RED, Block.Type.NORMAL.ordinal());
        assertEquals(Block.HitDirection.RIGHT.ordinal(), block.checkHitToBlock(block.x + Block.getWidth() + 10, block.y + (double) Block.getHeight() / 2, 10));
    }
}
