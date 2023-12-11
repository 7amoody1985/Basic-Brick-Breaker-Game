package brickGame;

import game.Game;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import ui.Sound;
import ui.UI;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UITest {

    @Test
    void shouldStartNewGameWhenStartMenuHandlerIsCalled() {
        UI ui = new UI(new Stage(), new Sound(), new Game());
        ui.showStartMenu();
        assertTrue(ui.newGame.isVisible());
    }

    @Test
    void shouldShowSettingsWhenSettingsButtonIsClicked() {
        UI ui = new UI(new Stage(), new Sound(), new Game());
        ui.showStartMenu();
        ui.settings.fire();
        assertTrue(ui.soundButton.isVisible());
        assertTrue(ui.musicButton.isVisible());
        assertTrue(ui.fpsCounter.isVisible());
        assertTrue(ui.back.isVisible());
    }

    @Test
    void shouldShowDifficultyWhenDifficultyButtonIsClicked() {
        UI ui = new UI(new Stage(), new Sound(), new Game());
        ui.showStartMenu();
        ui.difficulty.fire();
        assertTrue(ui.easy.isVisible());
        assertTrue(ui.medium.isVisible());
        assertTrue(ui.hard.isVisible());
        assertTrue(ui.back.isVisible());
    }

    @Test
    void shouldShowPauseMenuWhenShowPauseMenuIsCalled() {
        UI ui = new UI(new Stage(), new Sound(), new Game());
        ui.showPauseMenu();
        assertTrue(ui.resume.isVisible());
        assertTrue(ui.save.isVisible());
        assertTrue(ui.mainMenu.isVisible());
        assertTrue(ui.settings.isVisible());
        assertTrue(ui.exit.isVisible());
    }
}
