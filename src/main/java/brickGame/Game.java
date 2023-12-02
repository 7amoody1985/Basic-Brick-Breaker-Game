package brickGame;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class Game implements GameEngine.OnAction {
    public static final int SCENE_WIDTH = 500;
    public static final int SCENE_HEIGHT = 700;
    public static String savePath = "D:/save/save.mdds";

    public Pane root;
    Stage primaryStage;
    Button load = null;
    Button newGame = null;
    public int level = 0;




//    private Circle ball;
//    private double xBall;
//    private double yBall;

    private Rectangle rect;

    public int heart = 300;  // TEMPORARY FOR DEBUGGING
    public int score = 0;
    public long time = 0;
    private GameEngine engine;
    private Label scoreLabel;
    private Label heartLabel;
    private boolean loadFromSave = false;



    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;







    private GameEngine gameEngine;
    private Ball Ball;
    private Breaker breaker;
    private CollisionManager collision;
    private Bonus bonus;
    private Block block;
    private BounceDirection bounce;




    private GameEngine createGameEngine() {
        GameEngine engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        return engine;
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        if (!loadFromSave) {
            level++;
            if (level > 1) {
                new Score().showMessage("Level Up :)", this);
            }
            if (level == 18) {
                new Score().showWin(this);
                return;
            }

            Ball.initBall();
            breaker.initBreak();
            block.initBoard();

            load = new Button("Load Game");
            newGame = new Button("Start New Game");
            load.setTranslateX(220);
            load.setTranslateY(300);
            newGame.setTranslateX(220);
            newGame.setTranslateY(340);
        }

        root = new Pane();
        scoreLabel = new Label("Score: " + score);
        Label levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(SCENE_WIDTH - 80);
        if (!loadFromSave) {
            root.getChildren().addAll(rect, Ball.ball, scoreLabel, heartLabel, levelLabel, newGame);
        } else {
            root.getChildren().addAll(rect, Ball.ball, scoreLabel, heartLabel, levelLabel);
        }
        for (Block block : block.blocks) {
            root.getChildren().add(block.rect);
        }
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(Game.this::handle);
        scene.setOnKeyReleased(Game.this::handleReleased);

        primaryStage.setTitle("Brick Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        if (!loadFromSave) {
            if (level > 1 && level < 18) {
                load.setVisible(false);
                newGame.setVisible(false);
                engine = createGameEngine();
                engine.start();
            }

            load.setOnAction(event -> {
//                loadGame();

                load.setVisible(false);
                newGame.setVisible(false);
            });

            newGame.setOnAction(event -> {
                engine = createGameEngine();
                engine.start();

                load.setVisible(false);
                newGame.setVisible(false);
            });
        } else {
            engine = createGameEngine();
            engine.start();
            loadFromSave = false;
        }
    }


    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                isLeftPressed = true;
                break;
            case RIGHT:
                isRightPressed = true;
                break;
            case S:
//                saveGame();
                break;
        }
    }

    public void handleReleased(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                isLeftPressed = false;
                break;
            case RIGHT:
                isRightPressed = false;
                break;
        }
    }











//    private void saveGame() {
//        new Thread(() -> {
//            File file = new File(savePath);
//            ObjectOutputStream outputStream = null;
//            try {
//                outputStream = new ObjectOutputStream(new FileOutputStream(file));
//
//                outputStream.writeInt(level);
//                outputStream.writeInt(score);
//                outputStream.writeInt(heart);
//                outputStream.writeInt(block.destroyedBlockCount);
//
//                outputStream.writeDouble(Ball.xBall);
//                outputStream.writeDouble(Ball.yBall);
//                outputStream.writeDouble(breaker.xBreak);
//                outputStream.writeDouble(breaker.yBreak);
//                outputStream.writeDouble(breaker.centerBreakX);
//                outputStream.writeLong(time);
//                outputStream.writeLong(bonus.goldTime);
//                outputStream.writeDouble(Ball.vX);
//
//                outputStream.writeBoolean(block.isExistHeartBlock);
//                outputStream.writeBoolean(bonus.isGoldStatus);
//                outputStream.writeBoolean(Ball.goDownBall);
//                outputStream.writeBoolean(goUpBall);
//                outputStream.writeBoolean(goLeftBall);
//                outputStream.writeBoolean(goRightBall);
//                outputStream.writeBoolean(collideToRightWall);
//                outputStream.writeBoolean(collideToLeftWall);
//                outputStream.writeBoolean(collideToRightBlock);
//                outputStream.writeBoolean(collideToBottomBlock);
//                outputStream.writeBoolean(collideToLeftBlock);
//                outputStream.writeBoolean(collideToTopBlock);
//
//                ArrayList<BlockSerializable> blockSerializable = new ArrayList<>();
//                for (Block block : blocks) {
//                    if (block.isDestroyed) {
//                        continue;
//                    }
//                    blockSerializable.add(new BlockSerializable(block.row, block.column, block.type));
//                }
//                outputStream.writeObject(blockSerializable);
//
//                new Score().showMessage("Game Saved", Game.this);
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } finally {
//                try {
//                    assert outputStream != null;
//                    outputStream.flush();
//                    outputStream.close();
//                } catch (IOException e) {
//                    //noinspection ThrowFromFinallyBlock
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();
//    }

//    private void loadGame() {
//        LoadSave loadSave = new LoadSave();
//        loadSave.read();
//
//        isExistHeartBlock = loadSave.isExistHeartBlock;
//        isGoldStatus = loadSave.isGoldStatus;
//        goDownBall = loadSave.goDownBall;
//        goUpBall = loadSave.goUpBall;
//        goLeftBall = loadSave.goLeftBall;
//        goRightBall = loadSave.goRightBall;
//        collideToRightWall = loadSave.collideToRightWall;
//        collideToLeftWall = loadSave.collideToLeftWall;
//        collideToRightBlock = loadSave.collideToRightBlock;
//        collideToBottomBlock = loadSave.collideToBottomBlock;
//        collideToLeftBlock = loadSave.collideToLeftBlock;
//        collideToTopBlock = loadSave.collideToTopBlock;
//        level = loadSave.level;
//        score = loadSave.score;
//        heart = loadSave.heart;
//        destroyedBlockCount = loadSave.destroyedBlockCount;
//        xBall = loadSave.xBall;
//        yBall = loadSave.yBall;
//        xBreak = loadSave.xBreak;
//        yBreak = loadSave.yBreak;
//        centerBreakX = loadSave.centerBreakX;
//        time = loadSave.time;
//        goldTime = loadSave.goldTime;
//        vX = loadSave.vX;
//
//        blocks.clear();
//        bonus.chocos.clear();
//
//        for (BlockSerializable ser : loadSave.blocks) {
//            int r = new Random().nextInt(200);
//            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
//        }
//        try {
//            loadFromSave = true;
//            start(primaryStage);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void nextLevel() {
        Platform.runLater(() -> {
            try {
                engine.stop();
                Ball.resetCollideFlags();
                Ball.ballBounce(BounceDirection.DOWN);

                bonus.isGoldStatus = false;
                block.isExistHeartBlock = false;

                time = 0;
                bonus.goldTime = 0;

                engine.stop();
                block.blocks.clear();
                bonus.chocos.clear();
                block.destroyedBlockCount = 0;
                Ball.speed += 0.400;
                Ball.vX = Ball.speed;
                Ball.vY = Ball.speed;
                start(primaryStage);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void restartGame() {
        Platform.runLater(() -> {
            try {
                level = 0;
                heart = 3;
                score = 0;
                Ball.speed = 1.500;
                Ball.vX = Ball.speed;
                Ball.vY = Ball.speed;
                block.destroyedBlockCount = 0;
                Ball.resetCollideFlags();
                Ball.ballBounce(BounceDirection.DOWN);

                bonus.isGoldStatus = false;
                block.isExistHeartBlock = false;
                time = 0;
                bonus.goldTime = 0;

                block.blocks.clear();
                bonus.chocos.clear();

                start(primaryStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void onUpdate() {
        Platform.runLater(() -> {
            scoreLabel.setText("Score: " + score);
            heartLabel.setText("Heart : " + heart);

            rect.setX(breaker.xBreak);                          // TODO: check tf is this
            rect.setY(breaker.yBreak);
            Ball.ball.setCenterX(Ball.xBall);
            Ball.ball.setCenterY(Ball.yBall);

            for (Bonus choco : bonus.chocos) {
                choco.choco.setY(choco.y);
            }
        });

        if (isLeftPressed) {
            breaker.move(Move.LEFT);
        }
        if (isRightPressed) {
            breaker.move(Move.RIGHT);
        }

        collision.checkBlockCollisions();
    }

    @Override
    public void onInit() {
    }

    @Override
    public void onPhysicsUpdate() {
        block.checkDestroyedCount();

        Ball.moveBall();
        collision.checkCollisions();

        bonus.goldBall();
        bonus.bonusFall();
    }

    @Override
    public void onTime(long time) {
        this.time = time;
    }

    public enum Move {
        LEFT, RIGHT
    }
}