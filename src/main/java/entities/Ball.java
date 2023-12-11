package entities;

import enums.BounceDirection;
import game.Game;
import ui.UI;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * The Ball class represents a ball in the game.
 * It handles the movement and bouncing of the ball.
 */
public class Ball {
    public static final int BALL_RADIUS = 10;
    public double xBall;
    public double yBall;
    public double speed = 1.500;
    public double vX = speed;
    public double vY = speed;
    public boolean goDownBall = true;
    public boolean goUpBall = false;
    public boolean goRightBall = false;
    public boolean goLeftBall = false;
    public Circle ball;

    /**
     * Constructs a new Ball object.
     *
     * @param game the game instance.
     */
    public Ball(Game game) {
        xBall = (double) UI.SCENE_WIDTH / 2;
        yBall = (double) UI.SCENE_HEIGHT / 2 + ((game.level + 1) + (Block.getHeight() * ((double) game.level / 2)) + Block.getPaddingTop());
        ball = new Circle();
        ball.setRadius(BALL_RADIUS);
        setBallImagePattern("ball.png");
        ball.setVisible(false);
    }

    /**
     * Sets the image pattern of the ball.
     *
     * @param imageName the name of the image file.
     */
    public void setBallImagePattern(String imageName) {
        ball.setFill(new ImagePattern(new Image(imageName)));
    }

    /**
     * Moves the ball based on its current speed and instructed direction flag.
     */
    public void moveBall() {
        if (goDownBall) {
            yBall += vY;
        }
        if (goUpBall) {
            yBall -= vY;
        }

        if (goRightBall) {
            xBall += vX;
        }
        if (goLeftBall) {
            xBall -= vX;
        }

        if (goRightBall || goLeftBall) {
            double desired_speed = speed;

            double angle = Math.toRadians(60);
            vX = desired_speed * Math.cos(angle);
            vY = desired_speed * Math.sin(angle);
        } else {
            vX = speed;
            vY = speed;
        }
    }

    /**
     * Changes the direction of the ball's movement based on a bounce direction.
     *
     * @param bounce the bounce direction.
     */
    public void ballBounce(BounceDirection bounce) {
        if (bounce == BounceDirection.LEFT) {
            goRightBall = false;
            goLeftBall = true;
        }
        if (bounce == BounceDirection.RIGHT) {
            goLeftBall = false;
            goRightBall = true;
        }
        if (bounce == BounceDirection.UP) {
            goDownBall = false;
            goUpBall = true;
        }
        if (bounce == BounceDirection.DOWN) {
            goUpBall = false;
            goDownBall = true;
        }
        if (bounce == BounceDirection.STRAIGHT_UP) {
            goUpBall = true;
            goDownBall = false;
            goLeftBall = false;
            goRightBall = false;
        }
    }

    /**
     * Resets the ball's direction flags.
     */
    public void resetCollideFlags() {
        goUpBall = false;
        goDownBall = false;
        goLeftBall = false;
        goRightBall = false;
    }
}