package managers;

import entities.Ball;
import entities.Bonus;
import game.Game;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import ui.Sound;
import ui.UI;

import static org.junit.jupiter.api.Assertions.*;

public class BonusManagerTest {
    @Test
    void shouldCreateBonusManagerWithEmptyChocos() {
        BonusManager bonusManager = new BonusManager(new Game(), new Ball(new Game()), new UI(new Stage(), new Sound(), new Game()));
        assertTrue(bonusManager.chocos.isEmpty());
    }

    @Test
    void shouldNotBeGoldStatusWhenCreated() {
        BonusManager bonusManager = new BonusManager(new Game(), new Ball(new Game()), new UI(new Stage(), new Sound(), new Game()));
        assertFalse(bonusManager.isGoldStatus);
    }

    @Test
    void shouldIncreaseYWhenBonusFall() {
        BonusManager bonusManager = new BonusManager(new Game(), new Ball(new Game()), new UI(new Stage(), new Sound(), new Game()));
        Bonus bonus = new Bonus(1, 1);
        bonusManager.chocos.add(bonus);
        double initialY = bonus.y;
        bonusManager.bonusFall();
        assertTrue(bonus.y > initialY);
    }

    @Test
    void shouldNotIncreaseYWhenBonusIsTaken() {
        BonusManager bonusManager = new BonusManager(new Game(), new Ball(new Game()), new UI(new Stage(), new Sound(), new Game()));
        Bonus bonus = new Bonus(1, 1);
        bonus.taken = true;
        bonusManager.chocos.add(bonus);
        double initialY = bonus.y;
        bonusManager.bonusFall();
        assertEquals(initialY, bonus.y);
    }

    @Test
    void shouldSetBonusAsTakenWhenCaught() {
        BonusManager bonusManager = new BonusManager(new Game(), new Ball(new Game()), new UI(new Stage(), new Sound(), new Game()));
        Bonus bonus = new Bonus(1, 1);
        bonusManager.chocos.add(bonus);
        bonusManager.caught(bonus);
        assertTrue(bonus.taken);
    }

    @Test
    void shouldIncreaseScoreWhenBonusIsCaught() {
        Game game = new Game();
        BonusManager bonusManager = new BonusManager(game, new Ball(game), new UI(new Stage(), new Sound(), game));
        Bonus bonus = new Bonus(1, 1);
        bonusManager.chocos.add(bonus);
        int initialScore = game.score;
        bonusManager.caught(bonus);
        assertEquals(initialScore + 3, game.score);
    }

    @Test
    void shouldNotBeGoldStatusWhenGoldBallTimeIsMoreThanFiveSeconds() {
        Game game = new Game();
        BonusManager bonusManager = new BonusManager(game, new Ball(game), new UI(new Stage(), new Sound(), game));
        bonusManager.isGoldStatus = true;
        bonusManager.goldTime = game.time - 6000;
        bonusManager.goldBall();
        assertFalse(bonusManager.isGoldStatus);
    }
}
