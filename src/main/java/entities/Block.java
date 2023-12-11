package entities;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.EnumMap;

/**
 * The Block class represents a block in the game.
 * Each block has a position (row and column), a color, a type, and a state (destroyed or not).
 * It handles the drawing of the block and collision detection.
 */
public class Block implements Serializable {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 30;
    private static final int PADDING_TOP = HEIGHT * 2;
    private static final int PADDING_HORIZONTAL = 50;
    private final Color color;
    public int row;
    public int column;
    public boolean isDestroyed = false;
    public int type;
    public int x;
    public int y;
    public Rectangle rect;

    /**
     * Constructs and draws a new Block object.
     *
     * @param row    the row of the block.
     * @param column the column of the block.
     * @param color  the color of the block.
     * @param type   the type of the block.
     */
    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }

    /**
     * Returns the top padding of the block.
     * This is the space above the block.
     *
     * @return the top padding of the block.
     */
    public static int getPaddingTop() {
        return PADDING_TOP;
    }

    /**
     * Returns the horizontal padding of the block.
     * This is the space on the left and right sides of the block.
     *
     * @return the horizontal padding of the block.
     */
    public static int getPaddingH() {
        return PADDING_HORIZONTAL;
    }

    /**
     * Returns the height of the block.
     *
     * @return the height of the block.
     */
    public static int getHeight() {
        return HEIGHT;
    }

    /**
     * Returns the width of the block.
     *
     * @return the width of the block.
     */
    public static int getWidth() {
        return WIDTH;
    }

    /**
     * Draws the block at its position.
     * The block is represented as a rectangle with an image or color fill.
     */
    private void draw() {
        x = (column * WIDTH) + PADDING_HORIZONTAL;
        y = (row * HEIGHT) + PADDING_TOP;

        rect = new Rectangle();
        rect.setWidth(WIDTH);
        rect.setHeight(HEIGHT);
        rect.setX(x);
        rect.setY(y);

        EnumMap<Type, String> typeToImageFile = new EnumMap<>(Type.class);
        typeToImageFile.put(Type.CHOCO, "choco.jpg");
        typeToImageFile.put(Type.HEART, "heart.jpg");
        typeToImageFile.put(Type.STAR, "star.jpg");

        String imageFile = typeToImageFile.get(Type.values()[type]);

        if (imageFile != null) {
            Image image = new Image(imageFile);
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }

    }

    /**
     * Checks if the block is hit by the ball.
     * The hit direction is determined based on the ball's position relative to the block.
     *
     * @param xBall      the x coordinate of the ball.
     * @param yBall      the y coordinate of the ball.
     * @param ballRadius the radius of the ball.
     * @return the direction of the hit.
     */
    public int checkHitToBlock(double xBall, double yBall, double ballRadius) {
        if (isDestroyed) {
            return HitDirection.NO_HIT.ordinal();
        }

        Rectangle2D blockBounds = new Rectangle2D(x, y, WIDTH, HEIGHT);
        Rectangle2D ballBounds = new Rectangle2D(xBall - ballRadius, yBall - ballRadius, 2 * ballRadius, 2 * ballRadius);

        if (blockBounds.intersects(ballBounds)) {
            if (blockRightSideHit(xBall, ballRadius)) {
                return HitDirection.RIGHT.ordinal();
            } else if (blockLeftSideHit(xBall, ballRadius)) {
                return HitDirection.LEFT.ordinal();
            } else if (blockBottomHit(yBall, ballRadius)) {
                return HitDirection.BOTTOM.ordinal();
            } else if (blockTopHit(yBall, ballRadius)) {
                return HitDirection.TOP.ordinal();
            }
        }
        return HitDirection.NO_HIT.ordinal();
    }

    /**
     * Checks if the ball hits the top of the block.
     *
     * @param yBall      the y coordinate of the ball.
     * @param ballRadius the radius of the ball.
     * @return true if the ball hits the top of the block, false otherwise.
     */
    private boolean blockTopHit(double yBall, double ballRadius) {
        return yBall - ballRadius <= y && yBall + ballRadius >= y;
    }

    /**
     * Checks if the ball hits the bottom of the block.
     *
     * @param yBall      the y coordinate of the ball.
     * @param ballRadius the radius of the ball.
     * @return true if the ball hits the bottom of the block, false otherwise.
     */
    private boolean blockBottomHit(double yBall, double ballRadius) {
        return yBall + ballRadius >= y + HEIGHT && yBall - ballRadius <= y + HEIGHT;
    }

    /**
     * Checks if the ball hits the left side of the block.
     *
     * @param xBall      the x coordinate of the ball.
     * @param ballRadius the radius of the ball.
     * @return true if the ball hits the left side of the block, false otherwise.
     */
    private boolean blockLeftSideHit(double xBall, double ballRadius) {
        return xBall - ballRadius <= x && xBall + ballRadius >= x;
    }

    /**
     * Checks if the ball hits the right side of the block.
     *
     * @param xBall      the x coordinate of the ball.
     * @param ballRadius the radius of the ball.
     * @return true if the ball hits the right side of the block, false otherwise.
     */
    private boolean blockRightSideHit(double xBall, double ballRadius) {
        return xBall + ballRadius >= x + WIDTH && xBall - ballRadius <= x + WIDTH;
    }

    /**
     * Enum representing the possible directions of a hit on the block.
     */
    public enum HitDirection {
        RIGHT, BOTTOM, LEFT, TOP, NO_HIT
    }

    /**
     * Enum representing the possible types of a block.
     */
    public enum Type {
        NORMAL, CHOCO, STAR, HEART
    }
}
