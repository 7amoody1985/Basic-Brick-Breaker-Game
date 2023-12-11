package ui;

import game.Game;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreTest {
    @Test
    void shouldShowScoreAtCorrectPosition() {
        UI ui = new UI(new Stage(), new Sound(), new Game());
        Score score = new Score();
        score.show(100, 200, "10", ui);
        Label label = (Label) ui.root.getChildren().get(ui.root.getChildren().size() - 1);
        assertEquals(100, label.getTranslateX());
        assertEquals(200, label.getTranslateY());
        assertEquals("10", label.getText());
    }

    @Test
    void shouldShowImageAtCorrectPosition() {
        UI ui = new UI(new Stage(), new Sound(), new Game());
        Score score = new Score();
        Image image = new Image("file:resources/heart.png");
        score.showImage(100, 200, image, ui);
        ImageView imageView = (ImageView) ui.root.getChildren().get(ui.root.getChildren().size() - 1);
        assertEquals(100, imageView.getTranslateX());
        assertEquals(200, imageView.getTranslateY());
        assertEquals(image, imageView.getImage());
    }

    @Test
    void shouldShowMessageAtCorrectPosition() {
        UI ui = new UI(new Stage(), new Sound(), new Game());
        Score score = new Score();
        score.showMessage("Test", ui);
        Label label = (Label) ui.root.getChildren().get(ui.root.getChildren().size() - 1);
        assertEquals((double) UI.SCENE_WIDTH / 2, label.getTranslateX());
        assertEquals(340, label.getTranslateY());
        assertEquals("Test", label.getText());
    }
}
