package de.holube.pad.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class DefaultBoard extends AbstractBoard {

    @Getter
    public static final int[][] BOARD_LAYOUT = new int[][]{
            {0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1},
    };

    @Getter
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

    @Getter
    private transient int day = 0;

    @Getter
    private transient int month = 0;

    public DefaultBoard() {
        this(BOARD_LAYOUT, new ArrayList<>(), new ArrayList<>());
    }

    public DefaultBoard(int[][] board, List<Tile> tiles, List<int[][]> tileCumArrays) {
        super(board, tiles, tileCumArrays);
    }

    public boolean isValidSolution() {
        for (int i = 0; i < board.length; i++) {
            int[] row = board[i];
            for (int j = 0; j < row.length; j++) {
                int cell = row[j];
                if (cell > 1) {
                    day = 0;
                    month = 0;
                    return false;
                } else if (cell == 0) {
                    // check if day
                    if (BOARD_MEANING[0][i][j] == 1) {
                        if (day == 0) {
                            day = BOARD_MEANING[1][i][j];
                        } else {
                            day = 0;
                            month = 0;
                            return false;
                        }
                    } else if (BOARD_MEANING[0][i][j] == 0) { // check if month
                        if (month == 0) {
                            month = BOARD_MEANING[1][i][j];
                        } else {
                            month = 0;
                            day = 0;
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        if (day != 0 && month != 0)
            return true;
        day = 0;
        month = 0;
        return false;
    }


}
