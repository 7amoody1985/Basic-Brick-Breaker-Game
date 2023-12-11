package io;

import ui.UI;
import entities.Ball;
import entities.Block;
import entities.Breaker;
import game.Game;
import managers.BlockManager;
import managers.BonusManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * The SaveGame class is responsible for saving the game state.
 * It handles the serialization of the game state to a file.
 */
public class SaveGame {
    public static final String savePath = System.getProperty("user.home") + "/Documents/BrickGame/save.mdds";
    private static final String saveDirectory = System.getProperty("user.home") + "/Documents/BrickGame";
    private final Game game;
    private final UI ui;
    private final BonusManager bonuses;
    private final Breaker breaker;
    private final Ball ball;
    private final BlockManager manager;

    /**
     * Constructs a new SaveGame object.
     *
     * @param game    the game instance.
     * @param ui      the UI instance.
     * @param manager the block manager instance.
     * @param bonuses the bonus manager instance.
     * @param breaker the breaker instance.
     * @param ball    the ball instance.
     */
    public SaveGame(Game game, UI ui, BlockManager manager, BonusManager bonuses, Breaker breaker, Ball ball) {
        this.game = game;
        this.ui = ui;
        this.manager = manager;
        this.bonuses = bonuses;
        this.breaker = breaker;
        this.ball = ball;
    }

    /**
     * Saves the game state to a file.
     * The game state includes the game settings, the ball state, the breaker state, and the blocks state.
     * The game state is serialized and written to a file in a new thread.
     */
    public void saveGame() {
        new Thread(() -> {
            File directory = new File(saveDirectory);
            if (!directory.exists()) {
                //noinspection ResultOfMethodCallIgnored
                directory.mkdir();
            }
            File file = new File(savePath);
            ObjectOutputStream outputStream;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(UI.SCENE_WIDTH);
                outputStream.writeInt(game.horizontalGridSize);
                outputStream.writeInt(game.level);
                outputStream.writeInt(game.score);
                outputStream.writeInt(game.heart);

                outputStream.writeDouble(ball.xBall);
                outputStream.writeDouble(ball.yBall);
                outputStream.writeDouble(breaker.xBreak);
                outputStream.writeDouble(breaker.yBreak);
                outputStream.writeDouble(breaker.centerBreakX);
                outputStream.writeLong(game.time);
                outputStream.writeLong(bonuses.goldTime);
                outputStream.writeDouble(ball.vX);
                outputStream.writeDouble(ball.vY);
                outputStream.writeDouble(ball.speed);

                outputStream.writeBoolean(manager.isExistHeartBlock);
                outputStream.writeBoolean(bonuses.isGoldStatus);
                outputStream.writeBoolean(ball.goDownBall);
                outputStream.writeBoolean(ball.goUpBall);
                outputStream.writeBoolean(ball.goLeftBall);
                outputStream.writeBoolean(ball.goRightBall);

                ArrayList<BlockSerializable> blockSerializable = new ArrayList<>();
                for (Block block : manager.getBlocks()) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializable.add(new BlockSerializable(block.row, block.column, block.type));
                }
                outputStream.writeObject(blockSerializable);

                ui.showMessage("Game Saved");

                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}