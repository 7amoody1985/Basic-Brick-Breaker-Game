package brickGame;


import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.EnumMap;

import java.io.Serializable;
import java.util.Random;

public class Block implements Serializable {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 30;
    private static final int PADDING_TOP = HEIGHT * 2;
    private static final int PADDING_HORIZONTAL = 50;
    private final Color color;
    public int row;
    public int column;
    public boolean isDestroyed = false;
    public boolean isExistHeartBlock = false;
    public int type;
    public int x;
    public int y;
    public int destroyedBlockCount = 0;
    public final ArrayList<Block> blocks = new ArrayList<>();
    public final Color[] colors = new Color[]{Color.MAGENTA, Color.RED, Color.GOLD, Color.CORAL, Color.AQUA, Color.VIOLET, Color.GREENYELLOW, Color.ORANGE, Color.PINK, Color.SLATEGREY, Color.YELLOW, Color.TOMATO, Color.TAN,};
    public Rectangle rect;
    private Game game;
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

        // EnumMap
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

    public void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < game.level; j++) {
                int r = new Random().nextInt(500);
                int type;
                if (r % 10 == 1) {
                    type = Block.Type.CHOCO.ordinal();
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.Type.HEART.ordinal();
                        isExistHeartBlock = true;
                    } else {
                        type = Block.Type.NORMAL.ordinal();
                    }
                } else if (r % 10 == 3) {
                    type = Block.Type.STAR.ordinal();
                } else {
                    type = Block.Type.NORMAL.ordinal();
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
            }
        }
    }

    public void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            //TODO win level todo...
            System.out.println("You Win");
            game.nextLevel();
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

    public enum HitDirection {
        RIGHT, BOTTOM, LEFT, TOP, NO_HIT
    }

    public enum Type {
        NORMAL, CHOCO, STAR, HEART
    }
}
