package de.holube.pad.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BitMaskUtil {

    public static long[] fromArray(int[][] array) {
        long[] mask = new long[(array.length * array[0].length) / 64];
        int counter = 0;
        int index = 0;
        for (int[] row : array) {
            for (int cell : row) {
                mask[index] = mask[index] << 1;
                mask[index] = mask[index] | cell;
                counter++;
                if (counter == 64) {
                    counter = 0;
                    index++;
                }
            }
        }
        return mask;
    }

    public static int[][] toArray(long[] bitmask, int rows, int columns) {
        final long firstBit = 1L << 63;
        final int[][] result = new int[rows][columns];
        final int relevantLength = rows * columns;

        int counter = 0;
        int index = 0;

        bitmask = bitmask << (64 - relevantLength);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = (int) ((bitmask & firstBit) >>> 63);
                bitmask = bitmask << 1;
            }
        }

        return result;
    }

    public static void test() {
        int[][] array = new int[][]{
                {1, 0, 0},
                {0, 0, 1}
        };
        long[] bitmask = fromArray(array);

        if (bitmask[0] != 33) {
            throw new IllegalStateException();
        }

        array = toArray(bitmask, array.length, array[0].length);

        if (array[0][0] != 1) {
            throw new IllegalStateException();
        }
        if (array[0][1] != 0) {
            throw new IllegalStateException();
        }
        if (array[1][2] != 1) {
            throw new IllegalStateException();
        }
    }

}
