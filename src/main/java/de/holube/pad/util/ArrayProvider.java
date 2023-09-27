package de.holube.pad.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayProvider {

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

    // Non Default Tiles
    public static final int[][] TILE_H = new int[][]{
            {1, 0},
            {1, 1},
            {1, 1},
            {0, 1}
    };

    public static final int[][] TILE_I = new int[][]{
            {1, 1, 1, 1, 1}
    };

    public static final int[][] TILE_4 = new int[][]{
            {1, 1, 1, 1}
    };

    public static final int[][] TILE_3 = new int[][]{
            {1, 1, 1}
    };

    public static final int[][] TILE_2 = new int[][]{
            {1, 1}
    };

    public static final int[][] TILE_1 = new int[][]{
            {1}
    };

    public static final int[][] TILE_2x = new int[][]{
            {1, 1},
            {1, 1}
    };

    public static final int[][] TILE_p = new int[][]{
            {1, 0, 0, 0, 0},
            {1, 1, 1, 1, 1}
    };

    public static final int[][] TILE_T = new int[][]{
            {0, 1, 0},
            {0, 1, 0},
            {1, 1, 1}
    };

}
