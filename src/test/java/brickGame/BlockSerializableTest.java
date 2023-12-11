package brickGame;

import io.BlockSerializable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlockSerializableTest {
    @Test
    void shouldCreateBlockSerializableWithCorrectValues() {
        BlockSerializable blockSerializable = new BlockSerializable(1, 2, 3);
        assertEquals(1, blockSerializable.row);
        assertEquals(2, blockSerializable.j);
        assertEquals(3, blockSerializable.type);
    }
}
