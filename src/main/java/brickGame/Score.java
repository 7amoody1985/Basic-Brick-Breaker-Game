package brickGame;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Score {
    public void show(final double x, final double y, int score, final UI ui) {
        String sign;
        if (score >= 0) {
            sign = "+";
        } else {
            sign = "";
        }
        final Label label = new Label(sign + score);
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
                    Thread.sleep(15);
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
