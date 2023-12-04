package brickGame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SaveGame {
    public static String savePath = "D:/save/save.mdds";
    private final Game game;
    private final UI ui;
    private final BonusManager bonuses;
    private final Breaker breaker;
    private final Ball ball;
    private final BlockManager manager;

    public SaveGame(Game game, UI ui, BlockManager manager, BonusManager bonuses, Breaker breaker, Ball ball) {
        this.game = game;
        this.ui = ui;
        this.manager = manager;
        this.bonuses = bonuses;
        this.breaker = breaker;
        this.ball = ball;
    }

    public void saveGame() {
        new Thread(() -> {
            File directory = new File("D:/save");
            if (! directory.exists()){
                directory.mkdir();
            }
            File file = new File(savePath);
            ObjectOutputStream outputStream;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(game.level);
                outputStream.writeInt(game.score);
                outputStream.writeInt(game.heart);
//                outputStream.writeLong(manager.destroyedBlockCount);
//                outputStream.writeDouble(manager.getBlocks().size());


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