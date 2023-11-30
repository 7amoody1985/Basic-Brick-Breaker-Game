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
    private static final int SCENE_WIDTH = 500;
    private static final int SCENE_HEIGHT = 700;
    private static final int BALL_RADIUS = 10;
    private static final int MOVE_STEPS = 5;
    private static final double MOVE_DISTANCE = 0.5;
    public static String savePath = "D:/save/save.mdds";
    private final int BREAK_WIDTH = 130;
    private final int BREAK_HEIGHT = 30;
    private final int HALF_BREAK_WIDTH = BREAK_WIDTH / 2;
    private final ArrayList<Block> blocks = new ArrayList<>();
    private final ArrayList<Bonus> chocos = new ArrayList<>();
    private final Color[] colors = new Color[]{Color.MAGENTA, Color.RED, Color.GOLD, Color.CORAL, Color.AQUA, Color.VIOLET, Color.GREENYELLOW, Color.ORANGE, Color.PINK, Color.SLATEGREY, Color.YELLOW, Color.TOMATO, Color.TAN,};
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
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;

    private boolean BreakerRightZone(double rightZone) {
        return xBall >= rightZone && xBall <= xBreak + BREAK_WIDTH;
    }

    private boolean BreakerMiddleZone(double leftZone, double rightZone) { return xBall > leftZone && xBall < rightZone; }

    private boolean BreakerLeftZone(double leftZone) {
        return xBall >= xBreak && xBall <= leftZone;
    }

    private boolean BallCollideTopWall() {
        return yBall <= BALL_RADIUS;
    }

    private boolean BallCollideBottomWall() {
        return yBall >= SCENE_HEIGHT - BALL_RADIUS;
    }

    private boolean BallCollideWithBreaker() {
        return yBall >= yBreak - BALL_RADIUS;
    }

    private void setBallImagePattern(String imageName) {
        ball.setFill(new ImagePattern(new Image(imageName)));
    }

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
        heartLabel.setTranslateX(SCENE_WIDTH - 80);
        if (!loadFromSave) {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel, newGame);
        } else {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
        }
        for (Block block : blocks) {
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
                loadGame();

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

    private void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level; j++) {
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

    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                isLeftPressed = true;
                break;
            case RIGHT:
                isRightPressed = true;
                break;
            case S:
                saveGame();
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

    private void move(Move move) {
        new Thread(() -> {
            int fps = engine.getFps();
            for (int i = 0; i < MOVE_STEPS; i++) {
                if (xBreak >= (SCENE_WIDTH - BREAK_WIDTH) && move == Move.RIGHT) {
                    return;
                }
                if (xBreak <= 0 && move == Move.LEFT) {
                    return;
                }
                if (move == Move.RIGHT) {
                    xBreak += MOVE_DISTANCE;
                } else {
                    xBreak -= MOVE_DISTANCE;
                }
                centerBreakX = xBreak + HALF_BREAK_WIDTH;
                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void initBall() {
        xBall = (double) SCENE_WIDTH / 2;
        yBall = (double) SCENE_WIDTH / 2 + ((level + 1) * Block.getHeight()) + 15;
        ball = new Circle();
        ball.setRadius(BALL_RADIUS);
        setBallImagePattern("ball.png");
    }

    private void initBreak() {
        rect = new Rectangle();
        rect.setWidth(BREAK_WIDTH);
        rect.setHeight(BREAK_HEIGHT);
        xBreak = (double) (SCENE_WIDTH / 2) - HALF_BREAK_WIDTH;
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

        moveBall();

        if (BallCollideTopWall()) {
            resetCollideFlags();
            goUpBall = false;
            goDownBall = true;
            return;
        }
        if (BallCollideBottomWall()) {
            resetCollideFlags();
            goDownBall = false;
            goUpBall = true;
            if (!isGoldStatus) {
                //TODO game over
                heart--;
                new Score().show((double) SCENE_WIDTH / 2, (double) SCENE_HEIGHT / 2, -1, this);

                if (heart == 0) {
                    new Score().showGameOver(this);
                    engine.stop();
                }
            }
        }

        if (BallCollideWithBreaker()) {
            double zoneWidth = BREAK_WIDTH / 3.0;
            double leftZone = xBreak + zoneWidth;
            double rightZone = xBreak + 2 * zoneWidth;

            if (BreakerLeftZone(leftZone)) {
                resetCollideFlags();
                goDownBall = false;
                goUpBall = true;
                goRightBall = false;
                goLeftBall = true;
            } else if (BreakerMiddleZone(leftZone, rightZone)) {
                resetCollideFlags();
                goDownBall = false;
                goUpBall = true;
                // Make the ball go straight up
                goLeftBall = false;
                goRightBall = false;
            } else if (BreakerRightZone(rightZone)) {
                resetCollideFlags();
                goDownBall = false;
                goUpBall = true;
                goLeftBall = false;
                goRightBall = true;
            }
        }

        if (xBall >= SCENE_WIDTH - BALL_RADIUS) {
            resetCollideFlags();
            collideToRightWall = true;
        }

        if (xBall <= BALL_RADIUS) {
            resetCollideFlags();
            collideToLeftWall = true;
        }

        if (collideToRightWall || collideToLeftBlock) {
            goRightBall = false;
            goLeftBall = true;
        }
        if (collideToLeftWall || collideToRightBlock) {
            goLeftBall = false;
            goRightBall = true;
        }
        if (collideToTopBlock) {
            goDownBall = false;
            goUpBall = true;
        }
        if (collideToBottomBlock) {
            goUpBall = false;
            goDownBall = true;
        }
    }

    private void moveBall() {
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
            double desired_speed = speed;

            double angle = Math.toRadians(60);
            vX = desired_speed * Math.cos(angle);
            vY = desired_speed * Math.sin(angle);
        } else {
            vX = speed;
            vY = speed;
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

                new Score().showMessage("Game Saved", Game.this);

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

        if (isLeftPressed) {
            move(Move.LEFT);
        }
        if (isRightPressed) {
            move(Move.RIGHT);
        }

        if (yBall >= Block.getPaddingTop() - BALL_RADIUS && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop() - BALL_RADIUS) {
            synchronized (blocks) {
                for (Block block : blocks) {
                    int hitCode = block.checkHitToBlock(xBall, yBall, BALL_RADIUS);
                    if (hitCode != Block.HitDirection.NO_HIT.ordinal()) {
                        score += 1;

                        Platform.runLater(() -> {
                            new Score().show(block.x, block.y, 1, this);
                            block.rect.setVisible(false);
                        });

                        block.isDestroyed = true;
                        destroyedBlockCount++;
                        resetCollideFlags();

                        if (block.type == Block.Type.CHOCO.ordinal()) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.timeCreated = time;
                            Platform.runLater(() -> root.getChildren().add(choco.choco));
                            chocos.add(choco);
                        }
                        if (block.type == Block.Type.STAR.ordinal()) {
                            goldTime = time;
                            setBallImagePattern("goldball.png");
                            System.out.println("gold ball");
                            Platform.runLater(() -> root.getStyleClass().add("goldRoot"));
                            isGoldStatus = true;
                        }
                        if (block.type == Block.Type.HEART.ordinal()) {
                            heart++;
                        }

                        if (hitCode == Block.HitDirection.RIGHT.ordinal()) {
                            collideToRightBlock = true;
                        } else if (hitCode == Block.HitDirection.BOTTOM.ordinal()) {
                            collideToBottomBlock = true;
                        } else if (hitCode == Block.HitDirection.LEFT.ordinal()) {
                            collideToLeftBlock = true;
                        } else if (hitCode == Block.HitDirection.TOP.ordinal()) {
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
            setBallImagePattern("ball.png");
            root.getStyleClass().remove("goldRoot");
            isGoldStatus = false;
        }

        for (Bonus choco : chocos) {
            if (choco.y > SCENE_HEIGHT || choco.taken) {
                continue;
            }
            if (choco.y >= yBreak && choco.y <= yBreak + BREAK_HEIGHT && choco.x >= xBreak && choco.x <= xBreak + BREAK_WIDTH) {
                System.out.println("You Got it and +3 score for you");
                choco.taken = true;
                choco.choco.setVisible(false);
                score += 3;
                new Score().show(choco.x, choco.y, 3, this);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }
    }

    @Override
    public void onTime(long time) {
        this.time = time;
    }

    public enum Move {
        LEFT, RIGHT
    }
}