package game;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        new JFXPanel();
        game = new Game();
    }

    @Test
    void shouldStartNewGame() {
        game.start(null);
        assertEquals(1, game.level);
        assertEquals(3, game.heart);
        assertEquals(0, game.score);
        assertFalse(game.loadFromSave);
    }

    @Test
    void shouldLoadSavedGame() {
        game.loadFromSave = true;
        game.start(null);
        assertTrue(game.loadFromSave);
    }

    @Test
    void shouldIncreaseLevel() {
        game.level = 1;
        game.nextLevel();
        assertEquals(2, game.level);
    }

    @Test
    void shouldResetGame() {
        game.level = 2;
        game.heart = 2;
        game.score = 100;
        game.restartGame();
        assertEquals(1, game.level);
        assertEquals(3, game.heart);
        assertEquals(0, game.score);
    }
}
