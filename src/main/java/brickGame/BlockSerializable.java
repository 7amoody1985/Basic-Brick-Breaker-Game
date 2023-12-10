package brickGame;

import java.io.Serializable;

/**
 * The BlockSerializable class represents a serializable block in the game.
 * Each block has a row, column (j), and type.
 * This class is used when saving and loading the game state.
 */
public class BlockSerializable implements Serializable {
    public int row;
    public int j;
    public int type;

    /**
     * Constructs a new BlockSerializable object.
     *
     * @param row  the row of the block.
     * @param j    the column of the block.
     * @param type the type of the block.
     */
    public BlockSerializable(int row, int j, int type) {
        this.row = row;
        this.j = j;
        this.type = type;
    }
}
