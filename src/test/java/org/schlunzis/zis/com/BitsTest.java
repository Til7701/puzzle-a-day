package org.schlunzis.zis.com;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BitsTest {

    @Test
    void toLongNullTest() {
        assertThrows(NullPointerException.class, () -> Bits.toLong(null));
        assertThrows(NullPointerException.class, () -> Bits.toLong(new int[][]{null}));
        assertThrows(NullPointerException.class, () -> Bits.toLong(new int[][]{new int[]{1}, null}));
    }

    @Test
    void toLongArgumentTest() {
        assertThrows(IllegalArgumentException.class, () -> Bits.toLong(new int[][]{{2}}));
        assertThrows(IllegalArgumentException.class, () -> Bits.toLong(new int[][]{{0, 2}}));
        assertThrows(IllegalArgumentException.class, () -> Bits.toLong(new int[][]{{-1}}));
    }

    @Test
    void toLongTest() {
        assertEquals(0b0, Bits.toLong(new int[][]{{0}}));
        assertEquals(0b1, Bits.toLong(new int[][]{{1}}));
        assertEquals(0b01, Bits.toLong(new int[][]{{0, 1}}));
        assertEquals(0b10, Bits.toLong(new int[][]{{1, 0}}));
        assertEquals(0b1010, Bits.toLong(new int[][]{{1, 0}, {1, 0}}));
        assertEquals(0b101100, Bits.toLong(new int[][]{{1, 0}, {1, 1}, {0, 0}}));
    }

}
