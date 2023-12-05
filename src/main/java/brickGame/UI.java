package brickGame;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    public Pane root;
    public Button newGame;
    VBox buttonBox = new VBox();
    Button load;
    private Label scoreLabel;
    private Label heartLabel;

    public UI(Stage primaryStage) {
        this.rect = new Rectangle();
        this.primaryStage = primaryStage;
        this.load = new Button("Load Game");
        this.newGame = new Button("Start New Game");
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
        if (!loadFromSave) {
            root.getChildren().addAll(breaker.rect, ball.ball, scoreLabel, heartLabel, levelLabel, buttonBox);
        } else {
            root.getChildren().addAll(breaker.rect, ball.ball, scoreLabel, heartLabel, levelLabel);
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

    public void setupButtons() {
        buttonBox.getChildren().addAll(newGame, load);
        buttonBox.setPadding(new javafx.geometry.Insets(125, 70, 125, 70));
        buttonBox.setSpacing(20); // Set spacing between buttons
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.getStyleClass().add("buttonBox");
        newGame.getStyleClass().add("button");
        load.getStyleClass().add("button");

        Platform.runLater(() -> {
            double boxWidth = buttonBox.getBoundsInParent().getWidth();
            double boxHeight = buttonBox.getBoundsInParent().getHeight();
            buttonBox.setLayoutX((double) (SCENE_WIDTH / 2) - (boxWidth / 2));
            buttonBox.setLayoutY((double) (SCENE_HEIGHT / 2) - (boxHeight / 2));
        });

        root.getChildren().add(buttonBox);
    }

    public void hide() {
        load.setVisible(false);
        newGame.setVisible(false);
        buttonBox.setVisible(false);
    }
}