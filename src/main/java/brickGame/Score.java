package brickGame;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Score {
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

    public void showMessage(String message, final UI ui) {
        final Label label = new Label(message);
        label.setTranslateX(220);
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
    }

    public void showGameOver(final Game game, final UI ui) {
        Platform.runLater(() -> {
            Label label = new Label("Game Over :(");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = new Button("Restart");
            restart.setTranslateX(220);
            restart.setTranslateY(300);
            restart.setOnAction(event -> game.restartGame());

            ui.root.getChildren().addAll(label, restart);

        });
    }

    public void showWin(final UI ui) {
        Platform.runLater(() -> {
            Label label = new Label("You Win :)");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            ui.root.getChildren().addAll(label);

        });
    }
}
