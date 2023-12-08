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
    public static final int SCENE_WIDTH = 500;
    public static final int SCENE_HEIGHT = 700;
    private final Stage primaryStage;
    private final Scene scene;
    private final Rectangle rect;
    private final Sound sound;
    public Pane root;
    public Button newGame;
    public Button load;
    public Button settings;
    public Button exit;
    public Button back;
    public ToggleButton fpsCounter;
    public ToggleButton soundButton;
    public ToggleButton musicButton;
    VBox buttonBox = new VBox();
    private Label scoreLabel;
    private Label heartLabel;
    private Label fpsLabel;

    public UI(Stage primaryStage) {
        this.rect = new Rectangle();
        this.primaryStage = primaryStage;
        this.load = new Button("Load Game");
        this.newGame = new Button("Start New Game");
        this.settings = new Button("Settings");
        this.fpsCounter = new ToggleButton("FPS Counter: OFF");
        this.soundButton = new ToggleButton("Sound: ON");
        this.musicButton = new ToggleButton("Music: ON");
        this.sound = new Sound();
        this.back = new Button("Back");
        this.exit = new Button("Exit");
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
        root.getChildren().clear();
        scoreLabel = new Label("Score: " + game.score);
        Label levelLabel = new Label("Level: " + game.level);
        levelLabel.setTranslateX(220);
        heartLabel = new Label("Heart : " + game.heart);
        heartLabel.setTranslateX(SCENE_WIDTH - 80);
        fpsLabel = new Label();
        fpsLabel.setVisible(false);
        fpsLabel.setTranslateY(17);
        fpsLabel.setText("FPS: ");

        if (!loadFromSave) {
            root.getChildren().addAll(breaker.rect, ball.ball, scoreLabel, heartLabel, levelLabel, buttonBox, fpsLabel);
        } else {
            root.getChildren().addAll(breaker.rect, ball.ball, scoreLabel, heartLabel, levelLabel, fpsLabel);
        }
        for (Block block : manager.getBlocks()) {
            root.getChildren().add(block.rect);
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
        scoreLabel.setText("Score: " + score);
    }

    public void updateHeart(int heart) {
        heartLabel.setText("Heart : " + heart);
    }

    public void updateBreaker(Breaker breaker) {
        rect.setX(breaker.xBreak);
        rect.setY(breaker.yBreak);
    }

    public void updateBall(Ball ball) {
        ball.ball.setCenterX(ball.xBall);
        ball.ball.setCenterY(ball.yBall);
    }

    public void updateBonus(BonusManager bonuses) {
        for (Bonus choco : bonuses.chocos) {
            choco.choco.setY(choco.y);
        }
    }

    public void updateFPS(int fps) {
        Platform.runLater(() -> fpsLabel.setText("FPS: " + fps));
    }

    public void showMessage(String message) {
        new Score().showMessage(message, this);
    }

    public void showWin() {
        new Score().showWin(this);
    }

    public void showGameOver(Game game) {
        new Score().showGameOver(game, this);
    }

    public void show(final double x, final double y, String score) {
        new Score().show(x, y, score, this);
    }

    public void showImg(final double x, final double y, Image image) {
        new Score().showImage(x, y, image, this);
    }

    public void showStartMenu() {
        buttonBox.getChildren().clear();
        buttonBox.getChildren().addAll(newGame, load, settings, exit);
    }

    public void setupButtons() {
        showStartMenu();
        buttonBox.setPadding(new javafx.geometry.Insets(70, 70, 70, 70));
        buttonBox.setSpacing(20); // Set spacing between buttons
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.getStyleClass().add("buttonBox");
        newGame.getStyleClass().add("button");
        load.getStyleClass().add("button");

        positionButtonBox();

        root.getChildren().add(buttonBox);

        startMenuHandler();
    }

    private void positionButtonBox() {
        Platform.runLater(() -> {
            double boxWidth = buttonBox.getBoundsInParent().getWidth();
            double boxHeight = buttonBox.getBoundsInParent().getHeight();
            buttonBox.setLayoutX((double) (SCENE_WIDTH / 2) - (boxWidth / 2));
            buttonBox.setLayoutY((double) (SCENE_HEIGHT / 2) - (boxHeight / 2));
        });
    }

    public void hide() {
        load.setVisible(false);
        newGame.setVisible(false);
        buttonBox.setVisible(false);
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
                soundButton.setText("Sound: OFF");
            } else {
                sound.toggleSound();
                soundButton.setText("Sound: ON");
            }
            buttonClickSound();
        });

        musicButton.setOnAction(event -> {
            buttonClickSound();
            if (musicButton.isSelected()) {
                sound.musicOff();
                musicButton.setText("Music: OFF");
            } else {
                sound.musicOn();
                musicButton.setText("Music: ON");
            }
        });

        fpsCounter.setOnAction(event -> {
            buttonClickSound();
            if (fpsCounter.isSelected()) {
                fpsLabel.setVisible(true);
                fpsCounter.setText("FPS Counter: ON");
            } else {
                fpsLabel.setVisible(false);
                fpsCounter.setText("FPS Counter: OFF");
            }
        });

        back.setOnAction(event -> {
            buttonClickSound();
            showStartMenu();
        });

        exit.setOnAction(event -> {
            buttonClickSound();
            System.exit(0);
        });
    }

    public void showSettings() {
        buttonBox.getChildren().clear();
        buttonBox.getChildren().addAll(soundButton, musicButton, fpsCounter, back);
    }
}