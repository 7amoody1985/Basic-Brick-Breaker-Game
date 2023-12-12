package ui;

import entities.Ball;
import entities.Block;
import entities.Bonus;
import entities.Breaker;
import game.Game;
import game.GameEngine;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import managers.BlockManager;
import managers.BonusManager;

/**
 * The UI class is responsible for managing the user interface of the game.
 * It handles the creation and setup of the game scene, the game menu, and the game controls.
 * It also manages the display of the game score, heart count, and frames per second (fps).
 */
public class UI {
    public static final int SCENE_HEIGHT = 700;
    public static int SCENE_WIDTH = 500;
    private final Stage primaryStage;
    private final Scene scene;
    private final Rectangle rect;
    private final Sound sound;
    private final Game game;
    public Pane root;
    public Button newGame;
    public Button load;
    public Button difficulty;
    public Button easy;
    public Button medium;
    public Button hard;
    public Button settings;
    public Button exit;
    public Button back;
    public Button resume;
    public Button save;
    public Button mainMenu;
    public ToggleButton fpsCounter;
    public ToggleButton soundButton;
    public ToggleButton musicButton;
    public VBox buttonBox = new VBox();
    private boolean isPauseMenu = false;
    private Label scoreLabel;
    private Label heartLabel;
    private Label fpsLabel;
    private Label controlsLabel;
    private boolean isControlsVisible = true;

    /**
     * Constructs a new UI object.
     * Initializes the stage, scene, root pane, and game menu.
     * Sets up the game controls and starts the game music.
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set.
     * @param sound        the sound instance.
     * @param game         the game instance.
     */
    public UI(Stage primaryStage, Sound sound, Game game) {
        this.rect = new Rectangle();
        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(false);
        this.game = game;
        this.load = new Button("Load Game");
        this.newGame = new Button("Start New Game");
        this.difficulty = new Button("Difficulty");
        this.easy = new Button("Easy");
        this.medium = new Button("Medium");
        this.hard = new Button("Hard");
        this.settings = new Button("Settings");
        this.fpsCounter = new ToggleButton("FPS Counter: OFF");
        this.soundButton = new ToggleButton("Sound: ON");
        this.musicButton = new ToggleButton("Music: ON");
        this.sound = sound;
        this.back = new Button("Back");
        this.exit = new Button("Exit");
        this.resume = new Button("Resume");
        this.save = new Button("Save");
        this.mainMenu = new Button("Main Menu");
        this.root = new Pane();
        this.scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        Image icon = new Image("icon.jpg");
        primaryStage.getIcons().add(icon);
        scene.getStylesheets().add("style.css");
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Returns the primary stage of the application.
     *
     * @return the primary stage of the application.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Sets up the game scene.
     * Adds the breaker, ball, score label, heart label, level label, button box, fps label, and controls label to the root pane.
     * Sets up the key event handlers for the scene.
     *
     * @param game    the game instance.
     * @param breaker the breaker instance.
     * @param ball    the ball instance.
     * @param manager the block manager instance.
     */
    public void setupScene(Game game, Breaker breaker, Ball ball, BlockManager manager) {
        Platform.runLater(() -> root.getChildren().clear());
        scoreLabel = new Label("Score: " + game.score);
        Label levelLabel = new Label("Level: " + game.level);
        levelLabel.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> levelLabel.setTranslateX(((double) SCENE_WIDTH / 2) - (levelLabel.getWidth() / 2))));
        heartLabel = new Label("Heart: " + game.heart);
        fpsLabel = new Label();
        controlsLabel = new Label("""
                Controls:\s
                C: Hide Controls
                Left Arrow: Move Left
                Right Arrow: Move Right
                Space: Speed Boost
                S: Save Game
                Esc: Pause Game
                """);
        controlsLabel.getStyleClass().add("controlsLabel");

        Platform.runLater(() -> {
            heartLabel.setTranslateX(SCENE_WIDTH - 95);
            fpsLabel.setText("FPS: ");
            fpsLabel.setVisible(false);
            fpsLabel.setTranslateY(17);
            controlsLabel.setTranslateY(SCENE_HEIGHT - 80);
        });

        Platform.runLater(() -> root.getChildren().addAll(breaker.rect, ball.ball, scoreLabel, heartLabel, levelLabel, buttonBox, fpsLabel, controlsLabel));

        for (Block block : manager.getBlocks()) {
            Platform.runLater(() -> root.getChildren().add(block.rect));
        }
        scene.setOnKeyPressed(game::handle);
        scene.setOnKeyReleased(game::handleReleased);
    }

    /**
     * Shows the game scene.
     */
    public void showScene() {
        primaryStage.setTitle("Brick Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Updates the score label with the given score.
     *
     * @param score the score to display.
     */
    public void updateScore(int score) {
        Platform.runLater(() -> scoreLabel.setText("Score: " + score));
    }

    /**
     * Updates the heart label with the given heart count.
     *
     * @param heart the heart count to display.
     */
    public void updateHeart(int heart) {
        Platform.runLater(() -> heartLabel.setText("Heart: " + heart));
    }

    /**
     * Updates the breaker's position.
     *
     * @param breaker the breaker instance.
     */
    public void updateBreaker(Breaker breaker) {
        Platform.runLater(() -> {
            rect.setX(breaker.xBreak);
            rect.setY(breaker.yBreak);
        });
    }

    /**
     * Updates the ball's position.
     *
     * @param ball the ball instance.
     */
    public void updateBall(Ball ball) {
        Platform.runLater(() -> {
            ball.ball.setCenterX(ball.xBall);
            ball.ball.setCenterY(ball.yBall);
        });
    }

    /**
     * Updates the bonus's position.
     *
     * @param bonuses the bonus manager instance.
     */
    public void updateBonus(BonusManager bonuses) {
        for (Bonus choco : bonuses.chocos) {
            Platform.runLater(() -> choco.choco.setY(choco.y));
        }
    }

    /**
     * Updates the fps label with the given fps.
     *
     * @param fps the fps to display.
     */
    public void updateFPS(int fps) {
        Platform.runLater(() -> fpsLabel.setText("FPS: " + fps));
    }

    /**
     * Displays a message in the center of the screen.
     *
     * @param message the message to display.
     */
    public void showMessage(String message) {
        new Score().showMessage(message, this);
    }

    /**
     * Displays the restart menu with the given message.
     *
     * @param game   the game instance.
     * @param string the message to display.
     */
    public void restartMenu(Game game, String string) {
        Platform.runLater(() -> {
            Label gameOverLabel = new Label(string);
            gameOverLabel.setScaleX(2);
            gameOverLabel.setScaleY(2);

            Button restart = new Button("Start Menu");
            restart.setOnAction(event -> {
                buttonClickSound();
                game.restartGame();
            });

            setupSmallBox(gameOverLabel, restart);
        });
    }

    /**
     * Sets up a small box containing a label and a button.
     * The box is positioned in the center of the screen.
     *
     * @param gameOverLabel the label to display in the box.
     * @param restart       the button to display in the box.
     */
    private void setupSmallBox(Label gameOverLabel, Button restart) {
        VBox box = new VBox();
        box.getChildren().addAll(gameOverLabel, restart);
        box.setSpacing(30);
        box.setAlignment(javafx.geometry.Pos.CENTER);
        box.getStyleClass().add("buttonBox");

        box.setPrefSize(320, 190);

        root.getChildren().add(box);
        root.toFront();

        box.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> box.setTranslateX((SCENE_WIDTH - newValue.getWidth()) / 2));
        box.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> box.setTranslateY((SCENE_HEIGHT - newValue.getHeight()) / 2));
    }

    /**
     * Displays the next level menu.
     *
     * @param engine the game engine instance.
     */
    public void nextMenu(GameEngine engine) {
        engine.stop();
        Platform.runLater(() -> {
            Label nextLabel = new Label("You completed level " + game.level + " !");
            nextLabel.setScaleX(1.3);
            nextLabel.setScaleY(1.3);

            Button restart = new Button("Next Level");
            restart.setOnAction(event -> {
                buttonClickSound();
                engine.start();
                game.nextLevel();
            });

            setupSmallBox(nextLabel, restart);
        });
    }

    /**
     * Displays a score label at the given position.
     *
     * @param x     the x-coordinate of the label.
     * @param y     the y-coordinate of the label.
     * @param score the score to display.
     */
    public void show(final double x, final double y, String score) {
        new Score().show(x, y, score, this);
    }

    /**
     * Displays an image at the given position.
     *
     * @param x     the x-coordinate of the image.
     * @param y     the y-coordinate of the image.
     * @param image the image to display.
     */
    public void showImg(final double x, final double y, Image image) {
        new Score().showImage(x, y, image, this);
    }

    /**
     * Displays the start menu.
     */
    public void showStartMenu() {
        isPauseMenu = false;
        Platform.runLater(() -> {
            buttonBox.getChildren().clear();
            buttonBox.getChildren().addAll(newGame, load, difficulty, settings, exit);
            buttonBox.toFront();
        });
    }

    /**
     * Sets up the game menu.
     */
    public void setupButtons() {
        showStartMenu();

        Platform.runLater(() -> {
            buttonBox.setSpacing(30);
            buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
            buttonBox.getStyleClass().add("buttonBox");

            buttonBox.setPrefSize(320, 380);
        });

        positionButtonBox();

        Platform.runLater(() -> root.getChildren().add(buttonBox));

        startMenuHandler();
    }

    /**
     * Positions the game menu in the center of the screen.
     */
    private void positionButtonBox() {
        buttonBox.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> {
            double boxWidth = buttonBox.getWidth();
            double boxHeight = buttonBox.getHeight();
            buttonBox.setLayoutX((double) (SCENE_WIDTH / 2) - (boxWidth / 2));
            buttonBox.setLayoutY((double) (SCENE_HEIGHT / 2) - (boxHeight / 2));
        }));
    }

    /**
     * Hides the game menu.
     */
    public void hide() {
        Platform.runLater(() -> buttonBox.setVisible(false));
    }

    /**
     * Plays the button click sound effect.
     */
    public void buttonClickSound() {
        sound.playSound("Button Click.mp3");
    }

    /**
     * Handles the button actions for the start menu.
     */
    public void startMenuHandler() {

        settings.setOnAction(event -> {
            buttonClickSound();
            showSettings();
        });

        soundButton.setOnAction(event -> {
            if (soundButton.isSelected()) {
                sound.toggleSound();
                Platform.runLater(() -> soundButton.setText("Sound: OFF"));
            } else {
                sound.toggleSound();
                Platform.runLater(() -> soundButton.setText("Sound: ON"));
            }
            buttonClickSound();
        });

        musicButton.setOnAction(event -> {
            buttonClickSound();
            if (musicButton.isSelected()) {
                sound.musicOff();
                Platform.runLater(() -> musicButton.setText("Music: OFF"));
            } else {
                sound.musicOn();
                Platform.runLater(() -> musicButton.setText("Music: ON"));
            }
        });

        fpsCounter.setOnAction(event -> {
            buttonClickSound();
            if (fpsCounter.isSelected()) {
                fpsLabel.setVisible(true);
                Platform.runLater(() -> fpsCounter.setText("FPS Counter: ON"));
            } else {
                fpsLabel.setVisible(false);
                Platform.runLater(() -> fpsCounter.setText("FPS Counter: OFF"));
            }
        });

        back.setOnAction(event -> {
            buttonClickSound();
            if (isPauseMenu) {
                showPauseMenu();
            } else {
                showStartMenu();
            }
        });

        exit.setOnAction(event -> {
            buttonClickSound();
            System.exit(0);
        });

        resume.setOnAction(event -> {
            buttonClickSound();
            game.unPause();
        });

        save.setOnAction(event -> {
            buttonClickSound();
            game.saveGame();
        });

        mainMenu.setOnAction(event -> {
            buttonClickSound();
            game.restartGame();
        });

        difficulty.setOnAction(event -> {
            buttonClickSound();
            showDifficulty();
        });

        easy.setOnAction(event -> {
            buttonClickSound();
            SCENE_WIDTH = 500;
            game.horizontalGridSize = 4;
            game.restartGame();
        });

        medium.setOnAction(event -> {
            buttonClickSound();
            SCENE_WIDTH = 700;
            game.horizontalGridSize = 6;
            game.restartGame();
        });

        hard.setOnAction(event -> {
            buttonClickSound();
            SCENE_WIDTH = 900;
            game.horizontalGridSize = 8;
            game.restartGame();
        });
    }

    /**
     * Displays the settings menu.
     */
    public void showSettings() {
        Platform.runLater(() -> {
            buttonBox.getChildren().clear();
            buttonBox.getChildren().addAll(soundButton, musicButton, fpsCounter, back);
            buttonBox.toFront();
        });
    }

    /**
     * Displays the difficulty menu.
     */
    public void showDifficulty() {
        Platform.runLater(() -> {
            buttonBox.getChildren().clear();
            buttonBox.getChildren().addAll(easy, medium, hard, back);
            buttonBox.toFront();
        });
    }

    /**
     * Displays the pause menu.
     */
    public void showPauseMenu() {
        isPauseMenu = true;
        Platform.runLater(() -> {
            buttonBox.getChildren().clear();
            buttonBox.getChildren().addAll(resume, save, mainMenu, settings, exit);
            buttonBox.setVisible(true);
            buttonBox.toFront();
        });
    }

    /**
     * Toggles the visibility of the game controls.
     */
    public void toggleControls() {
        if (isControlsVisible) {
            Platform.runLater(() -> controlsLabel.setVisible(false));
            isControlsVisible = false;
        } else {
            Platform.runLater(() -> controlsLabel.setVisible(true));
            isControlsVisible = true;
        }
    }
}