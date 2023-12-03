package brickGame;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

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

    public Breaker(GameEngine engine) {
        if (engine == null) {
            throw new IllegalArgumentException("Engine cannot be null");
        }
        this.engine = engine;

        rect = new Rectangle();
        rect.setWidth(BREAK_WIDTH);
        rect.setHeight(BREAK_HEIGHT);
        xBreak = (double) (Game.SCENE_WIDTH / 2) - HALF_BREAK_WIDTH;
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));
        rect.setFill(pattern);
    }

    public void move(Game.Move move) {
        new Thread(() -> {
            int fps = engine.getFps();
            for (int i = 0; i < MOVE_STEPS; i++) {
                if (xBreak >= (Game.SCENE_WIDTH - BREAK_WIDTH) && move == Game.Move.RIGHT) {
                    return;
                }
                if (xBreak <= 0 && move == Game.Move.LEFT) {
                    return;
                }
                if (move == Game.Move.RIGHT) {
                    xBreak += MOVE_DISTANCE;
                } else {
                    xBreak -= MOVE_DISTANCE;
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