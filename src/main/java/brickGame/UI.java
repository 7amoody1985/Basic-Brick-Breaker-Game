package brickGame;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

public class UI {
    private Stage primaryStage;
    private Scene scene;
    public Pane root;
    private Label scoreLabel;
    private Label heartLabel;
    public Button newGame;
    private Rectangle rect;
    private Ball Ball;
    private boolean loadFromSave;
    private Breaker breaker;
    private BlockManager manager;
    Button load = null;
    public static final int SCENE_WIDTH = 500;
    public static final int SCENE_HEIGHT = 700;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public UI(Stage primaryStage) {
        this.rect = new Rectangle();
//        this.primaryStage = primaryStage;
//        this.Ball = Ball;
//        this.root = new Pane();
//        this.scene = new Scene(root, Game.SCENE_WIDTH, Game.SCENE_HEIGHT);
//        scene.getStylesheets().add("style.css");
//        this.breaker = breaker;
//        this.manager = manager;
//        this.loadFromSave = loadFromSave;
        this.primaryStage = primaryStage;
        this.load = new Button("Load Game");
        this.newGame = new Button("Start New Game");
        this.root = new Pane();
        this.scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add("style.css");
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
//        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
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

    public void updateBonus(BonusManager bonuses ) {
        for (Bonus choco : bonuses.chocos) {
            choco.choco.setY(choco.y);
        }
    }

    public Pane getRoot() {
        return root;
    }

    public void showMessage(String message, UI ui) {
        new Score().showMessage(message, ui);
    }

    public void showWin(UI ui) {
        new Score().showWin(ui);
    }

    public void showGameOver(Game game, UI ui) {
        new Score().showGameOver(game, ui);
    }

//    public void showLiveLost() {
//        new Score().show((double) UI.SCENE_WIDTH / 2, (double) UI.SCENE_HEIGHT / 2, -1, this);
//    }

    public void show(final double x, final double y, int score, final UI ui) {
        new Score().show(x, y, score, ui);
    }

    public void showScore(double x, double y, int score, Game game) {
        new Score().show(x, y, score, this);
    }

    public void setupButtons() {
        load.setTranslateX(220);
        load.setTranslateY(300);
        newGame.setTranslateX(220);
        newGame.setTranslateY(340);
    }
}

// new load game button
//load = new Button("Load Game");
//        newGame = new Button("Start New Game");
//        load.setTranslateX(220);
//        load.setTranslateY(300);
//        newGame.setTranslateX(220);
//        newGame.setTranslateY(340);


// more load game stuff
//if (!loadFromSave) {
//        if (level > 1 && level < 18) {
//        load.setVisible(false);
//        newGame.setVisible(false);
//        engine = createGameEngine();
//        engine.start();
//        }
//
//        load.setOnAction(event -> {
////                loadGame();
//
//        load.setVisible(false);
//        newGame.setVisible(false);
//        });
//
//        newGame.setOnAction(event -> {
//        engine = createGameEngine();
//        engine.start();
//
//        load.setVisible(false);
//        newGame.setVisible(false);
//        });
//        } else {
//        engine = createGameEngine();
//        engine.start();
//        loadFromSave = false;
//        }