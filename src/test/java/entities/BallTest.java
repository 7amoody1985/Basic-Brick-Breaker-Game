package entities;

import enums.BounceDirection;
import game.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BallTest {
    @Test
    void shouldMoveBallDownWhenGoDownBallIsTrue() {
        Ball ball = new Ball(new Game());
        ball.goDownBall = true;
        double initialY = ball.yBall;
        ball.moveBall();
        assertTrue(ball.yBall > initialY);
    }

    @Test
    void shouldMoveBallUpWhenGoUpBallIsTrue() {
        Ball ball = new Ball(new Game());
        ball.goUpBall = true;
        double initialY = ball.yBall;
        ball.moveBall();
        assertTrue(ball.yBall < initialY);
    }

    @Test
    void shouldMoveBallRightWhenGoRightBallIsTrue() {
        Ball ball = new Ball(new Game());
        ball.goRightBall = true;
        double initialX = ball.xBall;
        ball.moveBall();
        assertTrue(ball.xBall > initialX);
    }

    @Test
    void shouldMoveBallLeftWhenGoLeftBallIsTrue() {
        Ball ball = new Ball(new Game());
        ball.goLeftBall = true;
        double initialX = ball.xBall;
        ball.moveBall();
        assertTrue(ball.xBall < initialX);
    }

    @Test
    void shouldBounceBallToLeftWhenBounceDirectionIsLeft() {
        Ball ball = new Ball(new Game());
        ball.ballBounce(BounceDirection.LEFT);
        assertTrue(ball.goLeftBall);
        assertFalse(ball.goRightBall);
    }

    @Test
    void shouldBounceBallToRightWhenBounceDirectionIsRight() {
        Ball ball = new Ball(new Game());
        ball.ballBounce(BounceDirection.RIGHT);
        assertTrue(ball.goRightBall);
        assertFalse(ball.goLeftBall);
    }

    @Test
    void shouldBounceBallUpWhenBounceDirectionIsUp() {
        Ball ball = new Ball(new Game());
        ball.ballBounce(BounceDirection.UP);
        assertTrue(ball.goUpBall);
        assertFalse(ball.goDownBall);
    }

    @Test
    void shouldBounceBallDownWhenBounceDirectionIsDown() {
        Ball ball = new Ball(new Game());
        ball.ballBounce(BounceDirection.DOWN);
        assertTrue(ball.goDownBall);
        assertFalse(ball.goUpBall);
    }

    @Test
    void shouldBounceBallStraightUpWhenBounceDirectionIsStraightUp() {
        Ball ball = new Ball(new Game());
        ball.ballBounce(BounceDirection.STRAIGHT_UP);
        assertTrue(ball.goUpBall);
        assertFalse(ball.goDownBall);
        assertFalse(ball.goLeftBall);
        assertFalse(ball.goRightBall);
    }

    @Test
    void shouldResetCollideFlagsWhenResetCollideFlagsIsCalled() {
        Ball ball = new Ball(new Game());
        ball.goUpBall = true;
        ball.goDownBall = true;
        ball.goLeftBall = true;
        ball.goRightBall = true;
        ball.resetCollideFlags();
        assertFalse(ball.goUpBall);
        assertFalse(ball.goDownBall);
        assertFalse(ball.goLeftBall);
        assertFalse(ball.goRightBall);
    }
}
