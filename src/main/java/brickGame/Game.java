package brickGame;

import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Game implements GameEngine.OnAction {
    public static String savePath = "D:/save/save.mdds";

    public int level = 0;
    public int heart = 300;  // TEMPORARY FOR DEBUGGING
    public int score = 0;
    public long time = 0;
    private GameEngine engine;
    private boolean loadFromSave = false;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;


    private Ball Ball;
    private Breaker breaker;
    private CollisionManager collision;
    private BlockManager manager;
    private BonusManager bonuses;
    private UI ui;

    public Game() {

    }

    private GameEngine createGameEngine() {
        GameEngine engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        return engine;
    }

    public void start(Stage primaryStage) {
        ui = new UI(primaryStage);

        if (!loadFromSave) {
            level++;
            if (level > 1) {
                ui.showMessage("Level Up :)", ui);
            }
            if (level == 18) {
                ui.showWin();
                return; // remove this and win will work.... require further work
            }

            Ball = new Ball(this);
            engine = createGameEngine();
            breaker = new Breaker(engine);
            manager = new BlockManager(this);
            collision = new CollisionManager(this, Ball, breaker, manager, ui);
            bonuses = new BonusManager(this, Ball, ui);

            collision.setBonuses(bonuses);
            bonuses.setCollision(collision);

            Ball.speed += (0.400 * (level - 1));
            Ball.vX = Ball.speed;
            Ball.vY = Ball.speed;

            ui.setupButtons();
        }

        ui.setupScene(this, breaker, Ball, manager, loadFromSave);
        ui.showScene(); // primaryStage

        if (!loadFromSave) {
            if (level > 1 && level < 18) {
                ui.load.setVisible(false);
                ui.newGame.setVisible(false);
                engine = createGameEngine();
                engine.start();
            }

            ui.load.setOnAction(event -> {
//                loadGame();

                ui.load.setVisible(false);
                ui.newGame.setVisible(false);
            });

            ui.newGame.setOnAction(event -> {
                engine = createGameEngine();
                engine.start();

                ui.load.setVisible(false);
                ui.newGame.setVisible(false);
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

    public void stopengine() {
        engine.stop();
    }

    public void nextLevel() {
        Platform.runLater(() -> {
            try {
                engine.stop();
                Ball.resetCollideFlags();
                Ball.ballBounce(BounceDirection.DOWN);

                bonuses.isGoldStatus = false;
                manager.isExistHeartBlock = false;

                time = 0;
                bonuses.goldTime = 0;

                engine.stop();
                manager.clearBlocks();
                bonuses.chocos.clear();
                manager.destroyedBlockCount = 0;
                start(ui.getPrimaryStage());

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
                manager.destroyedBlockCount = 0;
                Ball.resetCollideFlags();
                Ball.ballBounce(BounceDirection.DOWN);

                bonuses.isGoldStatus = false;
                manager.isExistHeartBlock = false;
                time = 0;
                bonuses.goldTime = 0;

                manager.clearBlocks();
                bonuses.chocos.clear();

                start(ui.getPrimaryStage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void onUpdate() {
        Platform.runLater(() -> {
            ui.updateScore(score);
            ui.updateHeart(heart);

            ui.updateBreaker(breaker);
            ui.updateBall(Ball);

            ui.updateBonus(bonuses);
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
        manager.checkDestroyedCount();

        Ball.moveBall();
        collision.checkCollisions();

        bonuses.goldBall();
        bonuses.bonusFall();
    }

    @Override
    public void onTime(long time) {
        this.time = time;
    }

    public enum Move {
        LEFT, RIGHT
    }
}