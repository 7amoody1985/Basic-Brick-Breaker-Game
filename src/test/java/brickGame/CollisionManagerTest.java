package brickGame;

import entities.Ball;
import entities.Breaker;
import game.Game;
import game.GameEngine;
import javafx.stage.Stage;
import managers.BlockManager;
import managers.CollisionManager;
import org.junit.jupiter.api.Test;
import ui.Sound;
import ui.UI;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class CollisionManagerTest {
    @Test
    void shouldCollideWithTopWall() {
        Game game = new Game();
        Ball ball = new Ball(game);
        Breaker breaker = new Breaker(new GameEngine(new UI(new Stage(), new Sound(), game)));
        BlockManager blockManager = new BlockManager(game, new UI(new Stage(), new Sound(), game), game.horizontalGridSize);
        CollisionManager collisionManager = new CollisionManager(game, ball, breaker, blockManager, new UI(new Stage(), new Sound(), game), new Sound());

        ball.yBall = Ball.BALL_RADIUS;
        collisionManager.checkCollisions();
        assertFalse(ball.goUpBall);
    }

    @Test
    void shouldCollideWithBottomWall() {
        Game game = new Game();
        Ball ball = new Ball(game);
        Breaker breaker = new Breaker(new GameEngine(new UI(new Stage(), new Sound(), game)));
        BlockManager blockManager = new BlockManager(game, new UI(new Stage(), new Sound(), game), game.horizontalGridSize);
        CollisionManager collisionManager = new CollisionManager(game, ball, breaker, blockManager, new UI(new Stage(), new Sound(), game), new Sound());

        ball.yBall = UI.SCENE_HEIGHT - Ball.BALL_RADIUS;
        collisionManager.checkCollisions();
        assertFalse(ball.goDownBall);
    }

    @Test
    void shouldCollideWithLeftWall() {
        Game game = new Game();
        Ball ball = new Ball(game);
        Breaker breaker = new Breaker(new GameEngine(new UI(new Stage(), new Sound(), game)));
        BlockManager blockManager = new BlockManager(game, new UI(new Stage(), new Sound(), game), game.horizontalGridSize);
        CollisionManager collisionManager = new CollisionManager(game, ball, breaker, blockManager, new UI(new Stage(), new Sound(), game), new Sound());

        ball.xBall = Ball.BALL_RADIUS;
        collisionManager.checkCollisions();
        assertFalse(ball.goLeftBall);
    }

    @Test
    void shouldCollideWithRightWall() {
        Game game = new Game();
        Ball ball = new Ball(game);
        Breaker breaker = new Breaker(new GameEngine(new UI(new Stage(), new Sound(), game)));
        BlockManager blockManager = new BlockManager(game, new UI(new Stage(), new Sound(), game), game.horizontalGridSize);
        CollisionManager collisionManager = new CollisionManager(game, ball, breaker, blockManager, new UI(new Stage(), new Sound(), game), new Sound());

        ball.xBall = UI.SCENE_WIDTH - Ball.BALL_RADIUS;
        collisionManager.checkCollisions();
        assertFalse(ball.goRightBall);
    }
}
