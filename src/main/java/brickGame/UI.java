package brickGame;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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
        scene.getStylesheets().add("style.css");
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setupScene(Game game, Breaker breaker, Ball ball, BlockManager manager, boolean loadFromSave) {
        root.getChildren().clear();
        scoreLabel = new Label("Score: " + game.score);
        Label levelLabel = new Label("Level: " + game.level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + game.heart);
        heartLabel.setTranslateX(SCENE_WIDTH - 80);
        if (!loadFromSave) {
            root.getChildren().addAll(breaker.rect, ball.ball, scoreLabel, heartLabel, levelLabel, newGame);
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

    public void show(final double x, final double y, int score) {
        new Score().show(x, y, score, this);
    }

    public void showScore(double x, double y, int score) {
        new Score().show(x, y, score, this);
    }

    public void setupButtons() {
        load.setTranslateX(220);
        load.setTranslateY(300);
        newGame.setTranslateX(220);
        newGame.setTranslateY(340);
    }
}