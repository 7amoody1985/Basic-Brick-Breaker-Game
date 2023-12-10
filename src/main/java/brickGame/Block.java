package brickGame;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.EnumMap;

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

    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }

    public static int getPaddingTop() {
        return PADDING_TOP;
    }

    public static int getPaddingH() {
        return PADDING_HORIZONTAL;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static int getWidth() {
        return WIDTH;
    }

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

    private boolean blockTopHit(double yBall, double ballRadius) {
        return yBall - ballRadius <= y && yBall + ballRadius >= y;
    }

    private boolean blockBottomHit(double yBall, double ballRadius) {
        return yBall + ballRadius >= y + HEIGHT && yBall - ballRadius <= y + HEIGHT;
    }

    private boolean blockLeftSideHit(double xBall, double ballRadius) {
        return xBall - ballRadius <= x && xBall + ballRadius >= x;
    }

    private boolean blockRightSideHit(double xBall, double ballRadius) {
        return xBall + ballRadius >= x + WIDTH && xBall - ballRadius <= x + WIDTH;
    }

    public enum HitDirection {
        RIGHT, BOTTOM, LEFT, TOP, NO_HIT
    }

    public enum Type {
        NORMAL, CHOCO, STAR, HEART
    }
}
