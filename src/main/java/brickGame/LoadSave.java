package brickGame;

import javafx.application.Platform;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The LoadSave class is responsible for loading and saving the game state.
 * It handles the serialization and deserialization of the game state.
 */
public class LoadSave {
    private final Game game;
    private final UI ui;
    private final BlockManager manager;
    private final BonusManager bonuses;
    private final Breaker breaker;
    private final Ball ball;

    /**
     * Constructs a new LoadSave object.
     *
     * @param game    the game instance.
     * @param ui      the UI instance.
     * @param manager the block manager instance.
     * @param bonuses the bonus manager instance.
     * @param breaker the breaker instance.
     * @param ball    the ball instance.
     */
    public LoadSave(Game game, UI ui, BlockManager manager, BonusManager bonuses, Breaker breaker, Ball ball) {
        this.game = game;
        this.ui = ui;
        this.manager = manager;
        this.bonuses = bonuses;
        this.breaker = breaker;
        this.ball = ball;
    }

    /**
     * Loads the game state from a file.
     * The game state includes the game settings, the ball state, the breaker state, and the blocks state.
     * After loading the game state, it starts the game.
     */
    public void loadGame() {
        new Thread(() -> {
            File file = new File(SaveGame.savePath);
            ObjectInputStream inputStream;
            try {
                inputStream = new ObjectInputStream(new FileInputStream(file));

                UI.SCENE_WIDTH = inputStream.readInt();
                game.horizontalGridSize = inputStream.readInt();
                game.level = inputStream.readInt();
                game.score = inputStream.readInt();
                game.heart = inputStream.readInt();

                ball.xBall = inputStream.readDouble();
                ball.yBall = inputStream.readDouble();
                breaker.xBreak = inputStream.readDouble();
                breaker.yBreak = inputStream.readDouble();
                breaker.centerBreakX = inputStream.readDouble();
                game.time = inputStream.readLong();
                bonuses.goldTime = inputStream.readLong();
                ball.vX = inputStream.readDouble();
                ball.vY = inputStream.readDouble();
                ball.speed = inputStream.readDouble();

                manager.isExistHeartBlock = inputStream.readBoolean();
                bonuses.isGoldStatus = inputStream.readBoolean();
                ball.goDownBall = inputStream.readBoolean();
                ball.goUpBall = inputStream.readBoolean();
                ball.goLeftBall = inputStream.readBoolean();
                ball.goRightBall = inputStream.readBoolean();

                manager.clearBlocks();
                bonuses.chocos.clear();

                if (bonuses.isGoldStatus) {
                    Platform.runLater(() -> {
                        ball.setBallImagePattern("goldBall.png");
                        ui.root.getStyleClass().add("goldRoot");
                    });
                }

                ArrayList<BlockSerializable> blockSerializable;
                try {
                    //noinspection unchecked
                    blockSerializable = (ArrayList<BlockSerializable>) inputStream.readObject();
                } catch (OptionalDataException e) {
                    blockSerializable = new ArrayList<>();
                }
                for (BlockSerializable block : blockSerializable) {
                    int r = new Random().nextInt(200);
                    manager.getBlocks().add(new Block(block.row, block.j, manager.colors[r % manager.colors.length], block.type));
                }

                inputStream.close();

                try {
                    game.loadFromSave = true;
                    Platform.runLater(() -> {
                        game.start(ui.getPrimaryStage());
                        ui.showMessage("Game Loaded");
                        breaker.rect.setX(breaker.xBreak);
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}