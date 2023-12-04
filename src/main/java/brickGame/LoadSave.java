package brickGame;

import javafx.application.Platform;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class LoadSave {
    private Game game;
    private UI ui;
    private BlockManager manager;
    private BonusManager bonuses;
    private Breaker breaker;
    private Ball ball;

    public LoadSave(Game game, UI ui, BlockManager manager, BonusManager bonuses, Breaker breaker, Ball ball) {
        this.game = game;
        this.ui = ui;
        this.manager = manager;
        this.bonuses = bonuses;
        this.breaker = breaker;
        this.ball = ball;
    }

    public void loadGame() {
        new Thread(() -> {
            File file = new File(SaveGame.savePath);
            ObjectInputStream inputStream = null;
            try {
                inputStream = new ObjectInputStream(new FileInputStream(file));

                game.level = inputStream.readInt();
                game.score = inputStream.readInt();
                game.heart = inputStream.readInt();
                manager.destroyedBlockCount = inputStream.readInt();

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

                ArrayList<BlockSerializable> blockSerializable;
                try {
                    blockSerializable = (ArrayList<BlockSerializable>) inputStream.readObject();
                } catch (OptionalDataException e) {
                    blockSerializable = new ArrayList<>();
                }
                for (BlockSerializable block : blockSerializable) {
                    int r = new Random().nextInt(200);
                    manager.getBlocks().add(new Block(block.row, block.j, manager.colors[r % manager.colors.length], block.type));
                }

                ui.showMessage("Game Loaded");

                try {
                    game.loadFromSave = true;
                    Platform.runLater(() -> game.start(ui.getPrimaryStage()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                inputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}