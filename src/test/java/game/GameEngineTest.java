package game;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import ui.Sound;
import ui.UI;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineTest {
    @Test
    void shouldStartGameEngineWhenStartIsCalled() {
        UI ui = new UI(new Stage(), new Sound(), new Game());
        GameEngine gameEngine = new GameEngine(ui);
        gameEngine.start();
        assertFalse(gameEngine.isStopped());
    }

    @Test
    void shouldStopGameEngineWhenStopIsCalled() {
        UI ui = new UI(new Stage(), new Sound(), new Game());
        GameEngine gameEngine = new GameEngine(ui);
        gameEngine.start();
        gameEngine.stop();
        assertTrue(gameEngine.isStopped());
    }

    @Test
    void shouldSetFpsWhenSetFpsIsCalled() {
        UI ui = new UI(new Stage(), new Sound(), new Game());
        GameEngine gameEngine = new GameEngine(ui);
        gameEngine.setFps(60);
        assertEquals(60, gameEngine.getFps());
    }
}
