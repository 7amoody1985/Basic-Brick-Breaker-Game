package brickGame;


import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Block implements Serializable {
    private static final Block block = new Block(-1, -1, Color.TRANSPARENT, 99); // color ???
    public static int NO_HIT = -1;
    public static int HIT_RIGHT = 0;
    public static int HIT_BOTTOM = 1;
    public static int HIT_LEFT = 2;
    public static int HIT_TOP = 3;
    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_CHOCO = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;
    private final Color color;
    private final int width = 100;
    private final int height = 30;
    private final int paddingTop = height * 2;
    private final int paddingH = 50;
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
        return block.paddingTop;
    }

    public static int getPaddingH() {
        return block.paddingH;
    }

    public static int getHeight() {
        return block.height;
    }

    public static int getWidth() {
        return block.width;
    }

    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        if (type == BLOCK_CHOCO) {
            Image image = new Image("choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }

    }

    public int checkHitToBlock(double xBall, double yBall, double ballRadius) {
        if (isDestroyed) {
            return NO_HIT;
        }

        Rectangle2D blockBounds = new Rectangle2D(x, y, width, height);
        Rectangle2D ballBounds = new Rectangle2D(xBall - ballRadius, yBall - ballRadius, 2 * ballRadius, 2 * ballRadius);

        if (blockBounds.intersects(ballBounds)) {
            // Check for specific direction of collision
            if (xBall + ballRadius >= x + width && xBall - ballRadius <= x + width) {
                return HIT_RIGHT;
            } else if (xBall - ballRadius <= x && xBall + ballRadius >= x) {
                return HIT_LEFT;
            } else if (yBall + ballRadius >= y + height && yBall - ballRadius <= y + height) {
                return HIT_BOTTOM;
            } else if (yBall - ballRadius <= y && yBall + ballRadius >= y) {
                return HIT_TOP;
            }
        }

        return NO_HIT;
    }
}
