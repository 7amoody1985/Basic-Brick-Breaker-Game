package entities;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.Random;

/**
 * The Bonus class represents a bonus item in the game.
 * Each bonus has a position (x and y), a creation time, and a taken status.
 * The bonus is represented as a rectangle with an image fill.
 */
public class Bonus implements Serializable {
    public Rectangle choco;
    public double x;
    public double y;
    public long timeCreated;
    public boolean taken = false;

    /**
     * Constructs a new Bonus object at the given row and column.
     * Then, draws the bonus.
     *
     * @param row    the row of the bonus.
     * @param column the column of the bonus.
     */
    public Bonus(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + ((double) Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + ((double) Block.getHeight() / 2) - 15;

        draw();
    }

    /**
     * Draws the bonus at its position.
     * The bonus is represented as a rectangle with an image fill.
     */
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
}
