package brickGame;

import javafx.application.Platform;

import java.util.ArrayList;

public class BonusManager {
    public final ArrayList<Bonus> chocos = new ArrayList<>();
    private final Game game;
    private final Ball ball;
    private final UI ui;
    public boolean isGoldStatus = false;
    public long goldTime = 0;
    private CollisionManager collision;

    public BonusManager(Game game, Ball ball, UI ui) {
        this.game = game;
        this.ball = ball;
        this.ui = ui;
    }

    public void setCollision(CollisionManager collision) {
        this.collision = collision;
    }

    public void bonusFall() {
        for (Bonus choco : chocos) {
            if (choco.y > UI.SCENE_HEIGHT || choco.taken) {
                continue;
            }
            collision.checkBonusCollisions(choco);
            choco.y += ((game.time - choco.timeCreated) / 1000.000) + 1.000;
        }
    }

    public void caught(Bonus choco) {
        System.out.println("You Got it and +3 score for you");
        choco.taken = true;
        Platform.runLater(() -> choco.choco.setVisible(false));
        game.score += 3;
        ui.show(choco.x, choco.y, "+3");
    }

    public void goldBall() {
        if (game.time - goldTime > 5000) {
            ball.setBallImagePattern("ball.png");
            Platform.runLater(() -> ui.root.getStyleClass().remove("goldRoot"));
            isGoldStatus = false;
        }
    }
}