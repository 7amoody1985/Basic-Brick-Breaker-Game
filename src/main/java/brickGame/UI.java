package brickGame;

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
    VBox buttonBox = new VBox();
    private boolean isPauseMenu = false;
    private Label scoreLabel;
    private Label heartLabel;
    private Label fpsLabel;
    private Label controlsLabel;
    private boolean isControlsVisible = true;

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
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setupScene(Game game, Breaker breaker, Ball ball, BlockManager manager, boolean loadFromSave) {
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

        if (!loadFromSave) {
            Platform.runLater(() -> root.getChildren().addAll(breaker.rect, ball.ball, scoreLabel, heartLabel, levelLabel, buttonBox, fpsLabel, controlsLabel));
        } else {
            Platform.runLater(() -> root.getChildren().addAll(breaker.rect, ball.ball, scoreLabel, heartLabel, levelLabel, fpsLabel, controlsLabel));
        }
        for (Block block : manager.getBlocks()) {
            Platform.runLater(() -> root.getChildren().add(block.rect));
        }
        scene.setOnKeyPressed(game::handle);
        scene.setOnKeyReleased(game::handleReleased);
    }

    public void showScene() {
        primaryStage.setTitle("Brick Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateScore(int score) {
        Platform.runLater(() -> scoreLabel.setText("Score: " + score));
    }

    public void updateHeart(int heart) {
        Platform.runLater(() -> heartLabel.setText("Heart: " + heart));
    }

    public void updateBreaker(Breaker breaker) {
        Platform.runLater(() -> {
            rect.setX(breaker.xBreak);
            rect.setY(breaker.yBreak);
        });
    }

    public void updateBall(Ball ball) {
        Platform.runLater(() -> {
            ball.ball.setCenterX(ball.xBall);
            ball.ball.setCenterY(ball.yBall);
        });
    }

    public void updateBonus(BonusManager bonuses) {
        for (Bonus choco : bonuses.chocos) {
            Platform.runLater(() -> choco.choco.setY(choco.y));
        }
    }

    public void updateFPS(int fps) {
        Platform.runLater(() -> fpsLabel.setText("FPS: " + fps));
    }

    public void showMessage(String message) {
        new Score().showMessage(message, this);
    }

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

    private void setupSmallBox(Label gameOverLabel, Button restart) {
        VBox box = new VBox();
        box.getChildren().addAll(gameOverLabel, restart);
        box.setSpacing(30);
        box.setAlignment(javafx.geometry.Pos.CENTER);
        box.getStyleClass().add("buttonBox");

        box.setPrefSize(320, 190);

        root.getChildren().add(box);

        box.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> box.setTranslateX((SCENE_WIDTH - newValue.getWidth()) / 2));
        box.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> box.setTranslateY((SCENE_HEIGHT - newValue.getHeight()) / 2));
    }

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

    public void show(final double x, final double y, String score) {
        new Score().show(x, y, score, this);
    }

    public void showImg(final double x, final double y, Image image) {
        new Score().showImage(x, y, image, this);
    }

    public void showStartMenu() {
        isPauseMenu = false;
        Platform.runLater(() -> {
            buttonBox.getChildren().clear();
            buttonBox.getChildren().addAll(newGame, load, difficulty, settings, exit);
            buttonBox.toFront();
        });
    }

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

    private void positionButtonBox() {
        buttonBox.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> {
            double boxWidth = buttonBox.getWidth();
            double boxHeight = buttonBox.getHeight();
            buttonBox.setLayoutX((double) (SCENE_WIDTH / 2) - (boxWidth / 2));
            buttonBox.setLayoutY((double) (SCENE_HEIGHT / 2) - (boxHeight / 2));
        }));
    }

    public void hide() {
        Platform.runLater(() -> buttonBox.setVisible(false));
    }

    public void buttonClickSound() {
        sound.playSound("Button Click.mp3");
    }

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
            buttonBox.toFront();
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

    public void showSettings() {
        Platform.runLater(() -> {
            buttonBox.getChildren().clear();
            buttonBox.getChildren().addAll(soundButton, musicButton, fpsCounter, back);
            buttonBox.toFront();
        });
    }

    public void showDifficulty() {
        Platform.runLater(() -> {
            buttonBox.getChildren().clear();
            buttonBox.getChildren().addAll(easy, medium, hard, back);
            buttonBox.toFront();
        });
    }

    public void showPauseMenu() {
        isPauseMenu = true;
        Platform.runLater(() -> {
            buttonBox.getChildren().clear();
            buttonBox.getChildren().addAll(resume, save, mainMenu, settings, exit);
            buttonBox.setVisible(true);
            buttonBox.toFront();
        });
    }

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