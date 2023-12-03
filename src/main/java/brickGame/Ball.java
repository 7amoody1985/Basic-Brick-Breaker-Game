package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Ball {
    private Game game;
    public static final int BALL_RADIUS = 10;
    public double xBall;
    public double yBall;
    private boolean goDownBall = true;
    private boolean goUpBall = false;
    private boolean goRightBall = false;
    private boolean goLeftBall = false;
    public double speed = 1.500;
    public double vX = speed;
    public double vY = speed;
    Circle ball = new Circle();
    public void setBallImagePattern(String imageName) {
        ball.setFill(new ImagePattern(new Image(imageName)));
    }

//    public Ball(float x, float y, float radius) {
//        this.x = x;
//        this.y = y;
//        this.radius = radius;
//    }

    public Ball(Game game) {
        this.game = game;
        xBall = (double) Game.SCENE_WIDTH / 2;
        yBall = (double) Game.SCENE_WIDTH / 2 + ((game.level + 1) * Block.getHeight()) + 15;
        ball = new Circle();
        ball.setRadius(BALL_RADIUS);
        setBallImagePattern("ball.png");
    }

    // getters and setters for x, y, and radius

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

    public void ballBounce( BounceDirection bounce) {
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


    // other methods related to ball's movement and collision detection
}