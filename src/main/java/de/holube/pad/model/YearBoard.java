package de.holube.pad.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class YearBoard extends AbstractBoard {

    @Getter
    public static final int[][] BOARD_LAYOUT = new int[][]{
            {1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1},
    };

    @Getter
    public static final int[][][] BOARD_MEANING = new int[][][]{
            {
                    {-1, -1, 2, 2, 2, 2, 2, 2, -1, -1, -1},
                    {0, 0, 2, 2, 2, 2, 2, 2, -1, 1, 1},
                    {0, 0, 3, 3, 3, 3, 3, 3, 3, 1, 1},
                    {0, 0, 3, 3, 3, 3, 3, 3, 3, 1, 1},
                    {0, 0, 3, 3, 3, 3, 3, 3, 3, 1, 1},
                    {0, 0, 3, 3, 3, 3, 3, 3, 3, 1, 1},
                    {-1, -1, 3, 3, 3, -1, -1, -1, -1, -1, -1}
            },
            {
                    {-1, -1, 1, 2, 3, 4, 5, 6, -1, -1, -1},
                    {0, 1, 7, 8, 9, 10, 11, 12, -1, 0, 1},
                    {2, 3, 1, 2, 3, 4, 5, 6, 7, 2, 3},
                    {4, 5, 8, 9, 10, 11, 12, 13, 14, 4, 5},
                    {6, 7, 15, 16, 17, 18, 19, 20, 21, 6, 7},
                    {8, 9, 22, 23, 24, 25, 26, 27, 28, 8, 9},
                    {-1, -1, 29, 30, 31, -1, -1, -1, -1, -1, -1}
            }
    };

    protected static final int MAX_KEY = 3;

    public YearBoard() {
        this(BOARD_LAYOUT, new ArrayList<>(), new ArrayList<>());
    }

    public YearBoard(int[][] board, List<Tile> tiles, List<int[][]> tileCumArrays) {
        super(board, tiles, tileCumArrays, MAX_KEY);
    }

    public int[][] getBoardLayout() {
        return BOARD_LAYOUT;
    }

    public int[][][] getBoardMeaning() {
        return BOARD_MEANING;
    }

}
