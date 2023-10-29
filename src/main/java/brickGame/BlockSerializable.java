package brickGame;

import java.io.Serializable;

public class BlockSerializable implements Serializable {
    public int row;
    public int j;
    public int type;

    public BlockSerializable(int row, int j, int type) {
        this.row = row;
        this.j = j;
        this.type = type;
    }
}
