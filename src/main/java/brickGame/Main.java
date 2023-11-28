package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
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

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    public static String savePath = "D:/save/save.mdds";
    private final int breakWidth = 130;
    private final int breakHeight = 30;
    private final int halfBreakWidth = breakWidth / 2;
    private final int sceneWidth = 500;
    private final int sceneHeight = 700;
    private final int ballRadius = 10;
    private final ArrayList<Block> blocks = new ArrayList<>();
    private final ArrayList<Bonus> chocos = new ArrayList<>();
    private final Color[] colors = new Color[]{
            Color.MAGENTA,
            Color.RED,
            Color.GOLD,
            Color.CORAL,
            Color.AQUA,
            Color.VIOLET,
            Color.GREENYELLOW,
            Color.ORANGE,
            Color.PINK,
            Color.SLATEGREY,
            Color.YELLOW,
            Color.TOMATO,
            Color.TAN,
    };
    public Pane root;
    Stage primaryStage;
    Button load = null;
    Button newGame = null;
    private int level = 0;
    private double xBreak = 0.0f;
    private double centerBreakX;
    private double yBreak = 640.0f;
    private Circle ball;
    private double xBall;
    private double yBall;
    private boolean isGoldStatus = false;
    private boolean isExistHeartBlock = false;
    private Rectangle rect;
    private int destroyedBlockCount = 0;
    private int heart = 300;  // TEMPORARY FOR DEBUGGING
    private int score = 0;
    private long time = 0;
    private long goldTime = 0;
    private GameEngine engine;
    private Label scoreLabel;
    private Label heartLabel;
    private boolean loadFromSave = false;
    private boolean goDownBall = true;
    private boolean goUpBall = false;
    private boolean goRightBall = false;
    private boolean goLeftBall = false;
    private boolean collideToRightWall = false;
    private boolean collideToLeftWall = false;
    private boolean collideToRightBlock = false;
    private boolean collideToBottomBlock = false;
    private boolean collideToLeftBlock = false;
    private boolean collideToTopBlock = false;
    private double speed = 1.500;
    private double vX = speed;
    private double vY = speed;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
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

            initBall();
            initBreak();
            initBoard();

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
        heartLabel.setTranslateX(sceneWidth - 80);
        if (!loadFromSave) {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel, newGame);
        } else {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
        }
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);

        primaryStage.setTitle("Brick Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        if (!loadFromSave) {
            if (level > 1 && level < 18) {
                load.setVisible(false);
                newGame.setVisible(false);
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
            }

            load.setOnAction(event -> {
                loadGame();

                load.setVisible(false);
                newGame.setVisible(false);
            });

            newGame.setOnAction(event -> {
                engine = new GameEngine();
                engine.setOnAction(Main.this);
                engine.setFps(120);
                engine.start();

                load.setVisible(false);
                newGame.setVisible(false);
            });
        } else {
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
            loadFromSave = false;
        }


    }

    private void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level; j++) {
                int r = new Random().nextInt(500);
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO;
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR;
                } else {
                    type = Block.BLOCK_NORMAL;
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
                //System.out.println("colors " + r % (colors.length));
            }
        }
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:

                move(RIGHT);
                break;
            case DOWN:
//                setPhysicsToBall();               ????????????????????????
                break;
            case S:
                saveGame();
                break;
        }
    }

    private void move(final int direction) {
        new Thread(() -> {
            int fps = engine.getFps();
            for (int i = 0; i < 30; i++) {
                if (xBreak >= (sceneWidth - breakWidth) && direction == RIGHT) {
                    return;
                }
                if (xBreak <= 0 && direction == LEFT) {
                    return;
                }
                if (direction == RIGHT) {
                    xBreak += 0.5;
                } else {
                    xBreak -= 0.5;
                }
                centerBreakX = xBreak + halfBreakWidth;
                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


    }

    private void initBall() {
        xBall = (double) sceneWidth / 2;
        yBall = (double) sceneWidth / 2 + ((level + 1) * Block.getHeight()) + 15;
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));
    }

    private void initBreak() {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        xBreak = (double) sceneWidth / 2 - halfBreakWidth;
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));

        rect.setFill(pattern);
    }

    private void resetCollideFlags() {

        collideToRightWall = false;
        collideToLeftWall = false;

        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;
    }

    private void setPhysicsToBall() {
        //v = ((time - hitTime) / 1000.000) + 1.000;

        if (goDownBall) {
            yBall += vY;
        }
        if (goUpBall) {
            yBall -= vY;
        }

        if (goRightBall) {
            xBall += vX;
        }
        if (goLeftBall) {
            xBall -= vX;
        }

        if (goRightBall || goLeftBall) {
            double desired_speed = speed; // Set this to the desired speed

            // Adjust the angle of the ball
            double angle = Math.toRadians(30); // Set this to the desired angle
            vX = desired_speed * Math.cos(angle);
            vY = desired_speed * Math.sin(angle);
        }
        else {
            vX = speed;
            vY = speed;
        }

        if (yBall <= ballRadius) {
            //vX = 1.000;
            resetCollideFlags();
            goUpBall = false;
            goDownBall = true;
            return;
        }
        if (yBall >= sceneHeight - ballRadius) {
            goDownBall = false;
            goUpBall = true;
            if (!isGoldStatus) {
                //TODO game over
                heart--;
                new Score().show((double) sceneWidth / 2, (double) sceneHeight / 2, -1, this);

                if (heart == 0) {
                    new Score().showGameOver(this);
                    engine.stop();
                }

            }
            //return;
        }

        if (yBall >= yBreak - ballRadius) {
            double leftZone = (xBreak + breakWidth * 0.33) - ballRadius;
            double rightZone = (xBreak + breakWidth * 0.66) - ballRadius;

            if (xBall >= xBreak && xBall <= leftZone) {
                resetCollideFlags();
                goDownBall = false;
                goUpBall = true;
                goRightBall = false;
                goLeftBall = true;
            } else if (xBall > leftZone && xBall < rightZone) {
                resetCollideFlags();
                goDownBall = false;
                goUpBall = true;
                // Make the ball go straight up
                goLeftBall = false;
                goRightBall = false;
            } else if (xBall >= rightZone && xBall <= xBreak + breakWidth) {
                resetCollideFlags();
                goDownBall = false;
                goUpBall = true;
                goLeftBall = false;
                goRightBall = true;
            }
        }

        if (xBall >= sceneWidth - ballRadius) {
            resetCollideFlags();
            //vX = 1.000;
            collideToRightWall = true;
        }

        if (xBall <= ballRadius) {
            resetCollideFlags();
            //vX = 1.000;
            collideToLeftWall = true;
        }

        //Wall Collide

        if (collideToRightWall || collideToLeftBlock) {
            goRightBall = false;
            goLeftBall = true;
        }

        if (collideToLeftWall || collideToRightBlock) {
            goLeftBall = false;
            goRightBall = true;
        }

        //Block Collide

        if (collideToTopBlock) {
            goDownBall = false;
            goUpBall = true;
        }

        if (collideToBottomBlock) {
            goUpBall = false;
            goDownBall = true;
        }

    }


    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            //TODO win level todo...
            System.out.println("You Win");

            nextLevel();
        }
    }

    private void saveGame() {
        new Thread(() -> {
//            new File(savePathDir);        TODO: delete this line (redundant)
            File file = new File(savePath);
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(level);
                outputStream.writeInt(score);
                outputStream.writeInt(heart);
                outputStream.writeInt(destroyedBlockCount);


                outputStream.writeDouble(xBall);
                outputStream.writeDouble(yBall);
                outputStream.writeDouble(xBreak);
                outputStream.writeDouble(yBreak);
                outputStream.writeDouble(centerBreakX);
                outputStream.writeLong(time);
                outputStream.writeLong(goldTime);
                outputStream.writeDouble(vX);


                outputStream.writeBoolean(isExistHeartBlock);
                outputStream.writeBoolean(isGoldStatus);
                outputStream.writeBoolean(goDownBall);
                outputStream.writeBoolean(goUpBall);
                outputStream.writeBoolean(goLeftBall);
                outputStream.writeBoolean(goRightBall);
                outputStream.writeBoolean(collideToRightWall);
                outputStream.writeBoolean(collideToLeftWall);
                outputStream.writeBoolean(collideToRightBlock);
                outputStream.writeBoolean(collideToBottomBlock);
                outputStream.writeBoolean(collideToLeftBlock);
                outputStream.writeBoolean(collideToTopBlock);

                ArrayList<BlockSerializable> blockSerializable = new ArrayList<>();
                for (Block block : blocks) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializable.add(new BlockSerializable(block.row, block.column, block.type));
                }

                outputStream.writeObject(blockSerializable);

                new Score().showMessage("Game Saved", Main.this);


            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    assert outputStream != null;
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    //noinspection ThrowFromFinallyBlock
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

    private void loadGame() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();


        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStatus = loadSave.isGoldStatus;
        goDownBall = loadSave.goDownBall;
        goUpBall = loadSave.goUpBall;
        goLeftBall = loadSave.goLeftBall;
        goRightBall = loadSave.goRightBall;
        collideToRightWall = loadSave.collideToRightWall;
        collideToLeftWall = loadSave.collideToLeftWall;
        collideToRightBlock = loadSave.collideToRightBlock;
        collideToBottomBlock = loadSave.collideToBottomBlock;
        collideToLeftBlock = loadSave.collideToLeftBlock;
        collideToTopBlock = loadSave.collideToTopBlock;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        destroyedBlockCount = loadSave.destroyedBlockCount;
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xBreak = loadSave.xBreak;
        yBreak = loadSave.yBreak;
        centerBreakX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        blocks.clear();
        chocos.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }


        try {
            loadFromSave = true;
            start(primaryStage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private void nextLevel() {
        Platform.runLater(() -> {
            try {
                engine.stop();
                resetCollideFlags();
                goUpBall = false;
                goDownBall = true;
                goLeftBall = false;
                goRightBall = false;

                isGoldStatus = false;
                isExistHeartBlock = false;

                time = 0;
                goldTime = 0;

                engine.stop();
                blocks.clear();
                chocos.clear();
                destroyedBlockCount = 0;
                speed += 0.400;
                System.out.println("speed is " + speed);
                vX = speed;
                vY = speed;
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
                speed = 1.500;
                vX = speed;
                vY = speed;
                destroyedBlockCount = 0;
                resetCollideFlags();
                goUpBall = false;
                goDownBall = true;

                isGoldStatus = false;
                isExistHeartBlock = false;
                time = 0;
                goldTime = 0;

                blocks.clear();
                chocos.clear();

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

            rect.setX(xBreak);
            rect.setY(yBreak);
            ball.setCenterX(xBall);
            ball.setCenterY(yBall);

            for (Bonus choco : chocos) {
                choco.choco.setY(choco.y);
            }
        });


        if (yBall >= Block.getPaddingTop() - ballRadius && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop() - ballRadius) {
            synchronized (blocks) {
                for (Block block : blocks) {
                    int hitCode = block.checkHitToBlock(xBall, yBall, ballRadius);
                    if (hitCode != Block.NO_HIT) {
                        score += 1;

                        Platform.runLater(() -> {
                            new Score().show(block.x, block.y, 1, this);
                            block.rect.setVisible(false);
                        });

                        block.isDestroyed = true;
                        destroyedBlockCount++;
//                        System.out.println("destroyedBlockCount is " + destroyedBlockCount);
//                        System.out.println("size is " + blocks.size());
                        resetCollideFlags();

                        if (block.type == Block.BLOCK_CHOCO) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.timeCreated = time;
                            Platform.runLater(() -> root.getChildren().add(choco.choco));
                            chocos.add(choco);
                        }

                        if (block.type == Block.BLOCK_STAR) {
                            goldTime = time;
                            ball.setFill(new ImagePattern(new Image("goldball.png")));
                            System.out.println("gold ball");
                            Platform.runLater(() -> root.getStyleClass().add("goldRoot"));
                            isGoldStatus = true;
                        }

                        if (block.type == Block.BLOCK_HEART) {
                            heart++;
                        }

                        if (hitCode == Block.HIT_RIGHT) {
                            collideToRightBlock = true;
                        } else if (hitCode == Block.HIT_BOTTOM) {
                            collideToBottomBlock = true;
                        } else if (hitCode == Block.HIT_LEFT) {
                            collideToLeftBlock = true;
                        } else if (hitCode == Block.HIT_TOP) {
                            collideToTopBlock = true;
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onInit() {

    }

    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();


        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball.png")));
            root.getStyleClass().remove("goldRoot");
            isGoldStatus = false;
        }

        for (Bonus choco : chocos) {
            if (choco.y > sceneHeight || choco.taken) {
                continue;
            }
            if (choco.y >= yBreak && choco.y <= yBreak + breakHeight && choco.x >= xBreak && choco.x <= xBreak + breakWidth) {
                System.out.println("You Got it and +3 score for you");
                choco.taken = true;
                choco.choco.setVisible(false);
                score += 3;
                new Score().show(choco.x, choco.y, 3, this);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }

        //System.out.println("time is:" + time + " goldTime is " + goldTime);

    }


    @Override
    public void onTime(long time) {
        this.time = time;
    }
}
