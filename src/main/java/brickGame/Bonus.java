package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import javafx.application.Platform;

public class Bonus implements Serializable {
    public Rectangle choco;
    public double x;
    public double y;
    public long timeCreated;
    public boolean taken = false;
    public boolean isGoldStatus = false;
    public long goldTime = 0;
    public final ArrayList<Bonus> chocos = new ArrayList<>();
    private CollisionManager collision;
    private Game game;
    private Ball ball;

    public Bonus(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + ((double) Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + ((double) Block.getHeight() / 2) - 15;

        draw();
    }

    private void draw() {
        choco = new Rectangle();
        choco.setWidth(30);
        choco.setHeight(30);
        choco.setX(x);
        choco.setY(y);

        String url;
        if (new Random().nextInt(20) % 2 == 0) {
            url = "bonus1.png";
        } else {
            url = "bonus2.png";
        }

        choco.setFill(new ImagePattern(new Image(url)));
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
