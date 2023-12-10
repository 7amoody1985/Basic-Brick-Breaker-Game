package brickGame;

import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * The Game class represents the main game logic.
 * It handles user input, game state updates, and transitions between game levels.
 */
public class Game implements GameEngine.OnAction {
    private final Sound sound = new Sound();
    public int level = 0;
    public int finalLevel = 15;
    public int heart = 3;
    public int score = 0;
    public long time = 0;
    public int horizontalGridSize = 4;
    public boolean loadFromSave = false;
    private boolean isPaused = false;
    private GameEngine engine;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;
    private boolean isSpacePressed = false;
    private Ball Ball;
    private Breaker breaker;
    private CollisionManager collision;
    private BlockManager manager;
    private BonusManager bonuses;
    private UI ui;
    private SaveGame save;
    private LoadSave load;

    /**
     * Default constructor for the Game class.
     */
    public Game() {

    }

    /**
     * Creates a new GameEngine instance.
     *
     * @return a new GameEngine instance.
     */
    private GameEngine createGameEngine() {
        GameEngine engine = new GameEngine(ui);
        engine.setOnAction(this);
        engine.setFps(120);
        return engine;
    }

    /**
     * Starts the game. This method initializes the game state and starts the game loop.
     *
     * @param primaryStage the primary stage for this application.
     */
    public void start(Stage primaryStage) {

        if (!loadFromSave) {
            ui = new UI(primaryStage, sound, this);
            level++;
            if (level > 1) {
                ui.showMessage("Level Up :)");
            }

            Ball = new Ball(this);
            engine = createGameEngine();
            breaker = new Breaker(engine);
            manager = new BlockManager(this, ui, horizontalGridSize);
            setBallSpeed();
            ui.setupButtons();

            bonuses = new BonusManager(this, Ball, ui);
            collision = new CollisionManager(this, Ball, breaker, manager, ui, sound);
        }

        collision.setBonuses(bonuses);
        bonuses.setCollision(collision);

        ui.setupScene(this, breaker, Ball, manager);
        ui.showScene();

        save = new SaveGame(this, ui, manager, bonuses, breaker, Ball);
        load = new LoadSave(this, ui, manager, bonuses, breaker, Ball);

        if (!loadFromSave) {
            if (level > 1 && level <= finalLevel) {
                ui.hide();
                Ball.ball.setVisible(true);

                engine = createGameEngine();
                engine.start();
            }

            if (level == finalLevel + 1) {
                ui.hide();
                ui.restartMenu(this, "You Win!");
                sound.musicOff();
                sound.playSound("Win.mp3");
            }

            ui.load.setOnAction(event -> {
                ui.buttonClickSound();
                isPaused = false;
                load.loadGame();

                ui.hide();
            });

            ui.newGame.setOnAction(event -> {
                ui.buttonClickSound();
                isPaused = false;
                engine = createGameEngine();
                engine.start();

                ui.hide();
                Ball.ball.setVisible(true);
            });
        } else {
            engine.start();
            Ball.ball.setVisible(true);
            loadFromSave = false;
        }
    }

    /**
     * Sets the speed of the ball based on the current level.
     */
    private void setBallSpeed() {
        Ball.speed += (0.400 * (level - 1));
        Ball.vX = Ball.speed;
        Ball.vY = Ball.speed;
    }

    /**
     * Saves the current game state.
     */
    public void saveGame() {
        save.saveGame();
    }

    /**
     * Handles user input.
     *
     * @param event the KeyEvent that triggered this method call.
     */
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                isLeftPressed = true;
                break;
            case RIGHT:
                isRightPressed = true;
                break;
            case SPACE:
                isSpacePressed = true;
                break;
            case S:
                saveGame();
                break;
            case ESCAPE:
                if (isPaused) {
                    unPause();
                } else {
                    pause();
                }
                break;
            case C:
                ui.toggleControls();
        }
    }

    /**
     * Unpauses the game.
     */
    public void unPause() {
        ui.buttonBox.setVisible(false);
        if (engine.isStopped()) {
            engine.start();
        }
        isPaused = false;
    }

    /**
     * Pauses the game.
     */
    public void pause() {
        engine.stop();
        ui.showPauseMenu();
        isPaused = true;
    }

    /**
     * Handles user input when a key is released.
     *
     * @param event the KeyEvent that triggered this method call.
     */
    public void handleReleased(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                isLeftPressed = false;
                break;
            case RIGHT:
                isRightPressed = false;
                break;
            case SPACE:
                isSpacePressed = false;
                break;
        }
    }

    /**
     * Stops the game engine.
     */
    public void stopengine() {
        engine.stop();
    }

    /**
     * Advances the game to the next level.
     */
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

    /**
     * Restarts the game.
     */
    public void restartGame() {
        Platform.runLater(() -> {
            try {
                sound.musicOn();
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

    /**
     * Handles game state updates.
     */
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
        if (isSpacePressed && isLeftPressed) {
            breaker.move(Move.LEFTFAST);
        }
        if (isSpacePressed && isRightPressed) {
            breaker.move(Move.RIGHTFAST);
        }

        collision.checkBlockCollisions();
    }

    /**
     * Called when the game engine initializes.
     */
    @Override
    public void onInit() {
    }

    /**
     * Called when the game engine updates the physics.
     */
    @Override
    public void onPhysicsUpdate() {
        manager.checkDestroyedCount(engine);

        Ball.moveBall();
        collision.checkCollisions();

        bonuses.goldBall();
        bonuses.bonusFall();
    }

    /**
     * Called when the game engine updates the time.
     *
     * @param time the current time.
     */
    @Override
    public void onTime(long time) {
        this.time = time;
    }

    /**
     * Represents the possible move directions.
     */
    public enum Move {
        LEFT, RIGHT, LEFTFAST, RIGHTFAST
    }
}