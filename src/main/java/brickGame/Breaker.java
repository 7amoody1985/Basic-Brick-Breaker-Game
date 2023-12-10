package brickGame;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * The Breaker class represents the player's paddle in the game.
 * The paddle can move left or right, and it is represented as a rectangle with an image fill.
 */
public class Breaker {
    private static final int MOVE_STEPS = 5;
    private static final double MOVE_DISTANCE = 0.5;
    public final int BREAK_HEIGHT = 30;
    public final int BREAK_WIDTH = 130;
    private final GameEngine engine;
    private final int HALF_BREAK_WIDTH = 65;
    public double xBreak;
    public double yBreak = 640.0f;
    public double centerBreakX;
    public Rectangle rect;

    /**
     * Constructs a new Breaker object.
     * Initializes the paddle with the given game engine, and draws the paddle.
     *
     * @param engine the game engine instance.
     */
    public Breaker(GameEngine engine) {
        this.engine = engine;

        rect = new Rectangle();
        rect.setWidth(BREAK_WIDTH);
        rect.setHeight(BREAK_HEIGHT);
        xBreak = (double) (UI.SCENE_WIDTH / 2) - HALF_BREAK_WIDTH;
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));
        rect.setFill(pattern);
    }

    /**
     * Moves the paddle in the given direction.
     * The movement is performed in a new thread, and the paddle is moved in steps.
     *
     * @param move the direction to move the paddle.
     */
    public void move(Game.Move move) {
        new Thread(() -> {
            int fps = engine.getFps();
            for (int i = 0; i < MOVE_STEPS; i++) {
                if (xBreak >= (UI.SCENE_WIDTH - BREAK_WIDTH) && move == Game.Move.RIGHT) {
                    return;
                }
                if (xBreak <= 0 && move == Game.Move.LEFT) {
                    return;
                }
                if (xBreak >= (UI.SCENE_WIDTH - BREAK_WIDTH) && move == Game.Move.RIGHTFAST) {
                    return;
                }
                if (xBreak <= 0 && move == Game.Move.LEFTFAST) {
                    return;
                }
                if (move == Game.Move.RIGHT) {
                    xBreak += MOVE_DISTANCE;
                } else if (move == Game.Move.LEFT) {
                    xBreak -= MOVE_DISTANCE;
                } else if (move == Game.Move.LEFTFAST) {
                    xBreak -= MOVE_DISTANCE + 0.5;
                } else if (move == Game.Move.RIGHTFAST) {
                    xBreak += MOVE_DISTANCE + 0.5;
                }
                centerBreakX = xBreak + HALF_BREAK_WIDTH;
                Platform.runLater(() -> rect.setX(xBreak));
                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}