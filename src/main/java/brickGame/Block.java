package brickGame;


import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Block implements Serializable {
    private static final Block block = new Block(-1, -1, Color.TRANSPARENT, 99); // color ???
    public enum HitDirection {
        RIGHT, BOTTOM, LEFT, TOP, NO_HIT
    }
    public enum Type {
        NORMAL, CHOCO, STAR, HEART
    }
    private final Color color;
    private final int WIDTH = 100;
    private final int HEIGHT = 30;
    private final int PADDING_TOP = HEIGHT * 2;
    private final int PADDING_HORIZONTAL = 50;
    public int row;
    public int column;
    public boolean isDestroyed = false;
    public int type;
    public int x;
    public int y;
    public Rectangle rect;


    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }

    public static int getPaddingTop() {
        return block.PADDING_TOP;
    }

    public static int getPaddingH() {
        return block.PADDING_HORIZONTAL;
    }

    public static int getHeight() {
        return block.HEIGHT;
    }

    public static int getWidth() {
        return block.WIDTH;
    }

    private void draw() {
        x = (column * WIDTH) + PADDING_HORIZONTAL;
        y = (row * HEIGHT) + PADDING_TOP;

        rect = new Rectangle();
        rect.setWidth(WIDTH);
        rect.setHeight(HEIGHT);
        rect.setX(x);
        rect.setY(y);

        if (type == Type.CHOCO.ordinal()) {
            Image image = new Image("choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == Type.HEART.ordinal()) {
            Image image = new Image("heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == Type.STAR.ordinal()) {
            Image image = new Image("star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }

    }

    public int checkHitToBlock(double xBall, double yBall, double ballRadius) {
        if (isDestroyed) {
            return HitDirection.NO_HIT.ordinal();
        }

        Rectangle2D blockBounds = new Rectangle2D(x, y, WIDTH, HEIGHT);
        Rectangle2D ballBounds = new Rectangle2D(xBall - ballRadius, yBall - ballRadius, 2 * ballRadius, 2 * ballRadius);

        if (blockBounds.intersects(ballBounds)) {
            // Check for specific direction of collision
            if (xBall + ballRadius >= x + WIDTH && xBall - ballRadius <= x + WIDTH) {
                return HitDirection.RIGHT.ordinal();
            } else if (xBall - ballRadius <= x && xBall + ballRadius >= x) {
                return HitDirection.LEFT.ordinal();
            } else if (yBall + ballRadius >= y + HEIGHT && yBall - ballRadius <= y + HEIGHT) {
                return HitDirection.BOTTOM.ordinal();
            } else if (yBall - ballRadius <= y && yBall + ballRadius >= y) {
                return HitDirection.TOP.ordinal();
            }
        }

        return HitDirection.NO_HIT.ordinal();
    }
}
