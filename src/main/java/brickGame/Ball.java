package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Ball {
    public static final int BALL_RADIUS = 10;
    public double xBall;
    public double yBall;
    public double speed = 1.500;
    public double vX = speed;
    public double vY = speed;
    Circle ball;
    public boolean goDownBall = true;
    public boolean goUpBall = false;
    public boolean goRightBall = false;
    public boolean goLeftBall = false;

    public Ball(Game game) {
        xBall = (double) UI.SCENE_WIDTH / 2;
        yBall = (double) UI.SCENE_WIDTH / 2 + ((game.level + 1) * Block.getHeight()) + 15;
        ball = new Circle();
        ball.setRadius(BALL_RADIUS);
        setBallImagePattern("ball.png");
    }

    public void setBallImagePattern(String imageName) {
        ball.setFill(new ImagePattern(new Image(imageName)));
    }

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

    public void resetCollideFlags() {
        goUpBall = false;
        goDownBall = false;
        goLeftBall = false;
        goRightBall = false;
    }
}