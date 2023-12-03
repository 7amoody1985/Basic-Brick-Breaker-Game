package brickGame;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class BonusManager {
    private List<Bonus> bonuses;
    public boolean isGoldStatus = false;
    public long goldTime = 0;
    public final ArrayList<Bonus> chocos = new ArrayList<>();
    private CollisionManager collision;
    private Game game;
    private Ball ball;

    public BonusManager(Game game) {
        this.game = game;
        this.bonuses = new ArrayList<>();
    }

    public void addBonus(Bonus bonus) {
        this.bonuses.add(bonus);
    }

    public void removeBonus(Bonus bonus) {
        this.bonuses.remove(bonus);
    }

    public List<Bonus> getBonuses() {
        return this.bonuses;
    }

    public void clearBonuses() {
        this.bonuses.clear();
    }

    public void bonusFall() {
        for (Bonus choco : chocos) {
            if (choco.y > Game.SCENE_HEIGHT || choco.taken) {
                continue;
            }
            collision.checkBonusCollisions(choco);
            choco.y += ((game.time - choco.timeCreated) / 1000.000) + 1.000;
        }
    }

    public void caught(Bonus choco) {
        System.out.println("You Got it and +3 score for you");
        choco.taken = true;
        choco.choco.setVisible(false);
        game.score += 3;
        new Score().show(choco.x, choco.y, 3, game);
    }
    public void goldBall() {
        if (game.time - goldTime > 5000) {
            ball.setBallImagePattern("ball.png");
            Platform.runLater(() -> game.root.getStyleClass().remove("goldRoot"));
            isGoldStatus = false;
        }
    }
}