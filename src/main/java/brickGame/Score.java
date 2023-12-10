package brickGame;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The Score class is responsible for displaying score and other messages on the game screen.
 * It handles the creation and animation of labels and images on the screen.
 */
public class Score {
    /**
     * Displays a score label at the given position.
     * The label is animated to scale up and fade out.
     *
     * @param x     the x-coordinate of the label.
     * @param y     the y-coordinate of the label.
     * @param score the score to display.
     * @param ui    the UI instance.
     */
    public void show(final double x, final double y, String score, final UI ui) {
        final Label label = new Label(score);
        label.getStyleClass().add("plusScore");
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() -> ui.root.getChildren().add(label));

        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                final int scale = i;
                try {
                    Platform.runLater(() -> {
                        label.setScaleX(scale);
                        label.setScaleY(scale);
                        label.setOpacity((20 - scale) / 20.0);
                    });
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Displays an image at the given position.
     * The image is animated to scale up and fade out.
     *
     * @param x     the x-coordinate of the image.
     * @param y     the y-coordinate of the image.
     * @param image the image to display.
     * @param ui    the UI instance.
     */
    public void showImage(final double x, final double y, Image image, final UI ui) {
        ImageView heart = new ImageView(image);
        heart.setFitHeight(10);
        heart.setFitWidth(10);
        heart.setTranslateX(x);
        heart.setTranslateY(y);

        Platform.runLater(() -> ui.root.getChildren().add(heart));

        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                final int scale = i;
                try {
                    Platform.runLater(() -> {
                        heart.setScaleX(scale);
                        heart.setScaleY(scale);
                        heart.setOpacity((20 - scale) / 20.0);
                    });
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Displays a message in the center of the screen.
     * The message is animated to scale up and fade out.
     *
     * @param message the message to display.
     * @param ui      the UI instance.
     */
    public void showMessage(String message, final UI ui) {
        final Label label = new Label(message);
        label.setTranslateX((double) UI.SCENE_WIDTH / 2);
        label.setTranslateY(340);

        Platform.runLater(() -> ui.root.getChildren().add(label));

        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                final int scale = i;
                try {
                    Platform.runLater(() -> {
                        label.setScaleX(Math.abs(scale - 10));
                        label.setScaleY(Math.abs(scale - 10));
                        label.setOpacity((20 - scale) / 20.0);
                    });
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        Platform.runLater(() -> ui.buttonBox.toFront());
    }
}
