package entities;

import javafx.scene.paint.ImagePattern;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BonusTest {
    @Test
    void shouldCreateBonusWithCorrectPosition() {
        Bonus bonus = new Bonus(1, 2);
        assertEquals((2 * Block.getWidth()) + Block.getPaddingH() + ((double) Block.getWidth() / 2) - 15, bonus.x);
        assertEquals((Block.getHeight()) + Block.getPaddingTop() + ((double) Block.getHeight() / 2) - 15, bonus.y);
    }

    @Test
    void shouldNotBeTakenWhenCreated() {
        Bonus bonus = new Bonus(1, 2);
        assertFalse(bonus.taken);
    }

    @Test
    void shouldCreateRectangleWithCorrectDimensionsAndPosition() {
        Bonus bonus = new Bonus(1, 2);
        assertEquals(30, bonus.choco.getWidth());
        assertEquals(30, bonus.choco.getHeight());
        assertEquals(bonus.x, bonus.choco.getX());
        assertEquals(bonus.y, bonus.choco.getY());
    }

    @Test
    void shouldCreateRectangleWithImagePatternFill() {
        Bonus bonus = new Bonus(1, 2);
        assertInstanceOf(ImagePattern.class, bonus.choco.getFill());
    }
}