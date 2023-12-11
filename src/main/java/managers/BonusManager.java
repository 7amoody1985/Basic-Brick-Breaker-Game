package managers;

import game.Game;
import ui.UI;
import entities.Ball;
import entities.Bonus;
import javafx.application.Platform;

import java.util.ArrayList;

/**
 * The BonusManager class is responsible for managing the bonuses in the game.
 * It handles the creation, storage, and destruction of bonuses.
 * It also handles the collision detection between the bonuses and the ball.
 */
public class BonusManager {
    public final ArrayList<Bonus> chocos = new ArrayList<>();
    private final Game game;
    private final Ball ball;
    private final UI ui;
    public boolean isGoldStatus = false;
    public long goldTime = 0;
    private CollisionManager collision;

    /**
     * Constructs a new BonusManager object.
     *
     * @param game the game instance.
     * @param ball the ball instance.
     * @param ui   the UI instance.
     */
    public BonusManager(Game game, Ball ball, UI ui) {
        this.game = game;
        this.ball = ball;
        this.ui = ui;
    }

    /**
     * Sets the collision manager.
     *
     * @param collision the collision manager instance.
     */
    public void setCollision(CollisionManager collision) {
        this.collision = collision;
    }

    /**
     * Makes the bonuses fall.
     * Checks for collisions between the bonuses and the ball.
     */
    public void bonusFall() {
        for (Bonus choco : chocos) {
            if (choco.y > UI.SCENE_HEIGHT || choco.taken) {
                continue;
            }
            collision.checkBonusCollisions(choco);
            choco.y += ((game.time - choco.timeCreated) / 1000.000) + 1.000;
        }
    }

    /**
     * Handles the event when a bonus is caught.
     * Hides the bonus and increases the score.
     *
     * @param choco the caught bonus.
     */
    public void caught(Bonus choco) {
        System.out.println("You Got it and +3 score for you");
        choco.taken = true;
        Platform.runLater(() -> choco.choco.setVisible(false));
        game.score += 3;
        ui.show(choco.x, choco.y, "+3");
    }

    /**
     * Handles the gold ball status.
     */
    public void goldBall() {
        if (game.time - goldTime > 5000) {
            ball.setBallImagePattern("ball.png");
            Platform.runLater(() -> ui.root.getStyleClass().remove("goldRoot"));
            isGoldStatus = false;
        }
    }
}