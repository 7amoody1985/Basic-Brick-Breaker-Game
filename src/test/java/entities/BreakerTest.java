package entities;

import game.Game;
import game.GameEngine;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import ui.Sound;
import ui.UI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BreakerTest {
    @Test
    void shouldCreateBreakerWithCorrectDimensionsAndPosition() {
        Breaker breaker = new Breaker(new GameEngine(new UI(new Stage(), new Sound(), new Game())));
        assertEquals(breaker.BREAK_WIDTH, breaker.rect.getWidth());
        assertEquals(breaker.BREAK_HEIGHT, breaker.rect.getHeight());
        assertEquals(((double) UI.SCENE_WIDTH / 2) - (double) breaker.BREAK_WIDTH / 2, breaker.xBreak);
        assertEquals(640.0f, breaker.yBreak);
    }

    @Test
    void shouldNotMoveBreakerBeyondRightBoundary() {
        Breaker breaker = new Breaker(new GameEngine(new UI(new Stage(), new Sound(), new Game())));
        breaker.xBreak = UI.SCENE_WIDTH - breaker.BREAK_WIDTH;
        breaker.move(Game.Move.RIGHT);
        assertEquals(UI.SCENE_WIDTH - breaker.BREAK_WIDTH, breaker.xBreak);
    }

    @Test
    void shouldNotMoveBreakerBeyondLeftBoundary() {
        Breaker breaker = new Breaker(new GameEngine(new UI(new Stage(), new Sound(), new Game())));
        breaker.xBreak = 0;
        breaker.move(Game.Move.LEFT);
        assertEquals(0, breaker.xBreak);
    }

    @Test
    void shouldMoveBreakerRight() {
        Breaker breaker = new Breaker(new GameEngine(new UI(new Stage(), new Sound(), new Game())));
        double initialX = breaker.xBreak;
        breaker.move(Game.Move.RIGHT);
        assertTrue(breaker.xBreak > initialX);
    }

    @Test
    void shouldMoveBreakerLeft() {
        Breaker breaker = new Breaker(new GameEngine(new UI(new Stage(), new Sound(), new Game())));
        double initialX = breaker.xBreak;
        breaker.move(Game.Move.LEFT);
        assertTrue(breaker.xBreak < initialX);
    }
}
