package org.schlunzis.zis.com;

import java.util.Objects;

public class Bits {

    private Bits() {
    }

    /**
     * Converts a 2D array of 0s and 1s to a long bitmask. The array is read row by row from left to right. The first
     * row is the most significant bits. The first cell in a row is the most significant bit in that row.
     *
     * @param array 2D array of 0s and 1s
     * @return long bitmask representing the array
     */
    public static long toLong(int[][] array) {
        Objects.requireNonNull(array);
        long mask = 0L;
        for (int[] row : array) {
            Objects.requireNonNull(row);
            for (int cell : row) {
                mask = mask << 1;
                if (cell == 0 || cell == 1) {
                    mask = mask | cell;
                } else {
                    throw new IllegalArgumentException("Array must only contain 0 or 1");
                }
            }
        }
        return mask;
    }


    public static long[] toLongArray(int[][] array) {
        Objects.requireNonNull(array);
        long[] mask = new long[(array.length * array[0].length) / 64];
        int counter = 0;
        int index = 0;
        for (int[] row : array) {
            for (int cell : row) {
                mask[index] = mask[index] << 1;
                if (cell == 0 || cell == 1) {
                    mask[index] = mask[index] | cell;
                } else {
                    throw new IllegalArgumentException("Array must only contain 0 or 1");
                }
                counter++;
                if (counter == 64) {
                    counter = 0;
                    index++;
                }
            }
        }
        return mask;
    }

    public static int[][] toIntArray(long bitmask, int rows, int columns) {
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

}
