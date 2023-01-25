package de.holube.pad.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayProvider {

    public static final int[][] BOARD_LAYOUT = new int[][]{
            {0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1},
    };
    public static final int[][][] BOARD_MEANING = new int[][][]{
            {
                    {0, 0, 0, 0, 0, 0, -1},
                    {0, 0, 0, 0, 0, 0, -1},
                    {1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, -1, -1, -1, -1}
            },
            {
                    {1, 2, 3, 4, 5, 6, -1},
                    {7, 8, 9, 10, 11, 12, -1},
                    {1, 2, 3, 4, 5, 6, 7},
                    {8, 9, 10, 11, 12, 13, 14},
                    {15, 16, 17, 18, 19, 20, 21},
                    {22, 23, 24, 25, 26, 27, 28},
                    {29, 30, 31, -1, -1, -1, -1}
            }
    };

    public static final int[][] TILE_S = new int[][]{
            {1, 0, 0},
            {1, 1, 1},
            {0, 0, 1}
    };

    public static final int[][] TILE_l = new int[][]{
            {1, 0, 0, 0},
            {1, 1, 1, 1}
    };

    public static final int[][] TILE_L = new int[][]{
            {1, 0, 0},
            {1, 0, 0},
            {1, 1, 1}
    };

    public static final int[][] TILE_t = new int[][]{
            {1, 0},
            {1, 1},
            {1, 0},
            {1, 0}
    };

    public static final int[][] TILE_O = new int[][]{
            {1, 1},
            {1, 1},
            {1, 1}
    };

    public static final int[][] TILE_s = new int[][]{
            {1, 0},
            {1, 1},
            {0, 1},
            {0, 1}
    };

    public static final int[][] TILE_P = new int[][]{
            {1, 1},
            {1, 1},
            {1, 0}
    };

    public static final int[][] TILE_C = new int[][]{
            {1, 1},
            {1, 0},
            {1, 1}
    };

}
