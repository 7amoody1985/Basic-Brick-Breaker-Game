package brickGame;

import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Game implements GameEngine.OnAction {
    public int level = 0;
    public int heart = 300;  // TEMPORARY FOR DEBUGGING
    public int score = 0;
    public long time = 0;
    public boolean loadFromSave = false;
    private GameEngine engine;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;

    private Ball Ball;
    private Breaker breaker;
    private CollisionManager collision;
    private BlockManager manager;
    private BonusManager bonuses;
    private UI ui;
    private SaveGame save;
    private LoadSave load;

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
                ui.showMessage("Level Up :)");
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
            save = new SaveGame(this, ui, manager, bonuses, breaker, Ball);
            load = new LoadSave(this, ui, manager, bonuses, breaker, Ball);

            collision.setBonuses(bonuses);
            bonuses.setCollision(collision);

            Ball.speed += (0.400 * (level - 1));
            Ball.vX = Ball.speed;
            Ball.vY = Ball.speed;

            ui.setupButtons();
        }

        ui.setupScene(this, breaker, Ball, manager, loadFromSave);
        ui.showScene();

        if (!loadFromSave) {
            if (level > 1 && level < 18) {
                ui.hide();
                engine = createGameEngine();
                engine.start();
            }

            ui.load.setOnAction(event -> {
                load.loadGame();

                ui.hide();
            });

            ui.newGame.setOnAction(event -> {
                engine = createGameEngine();
                engine.start();

                ui.hide();
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
                save.saveGame();
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