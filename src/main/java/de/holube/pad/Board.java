package de.holube.pad;

import lombok.Getter;

public class Board {

    private final int[][] board;
    private final int[][][] boardMeaning;

    @Getter
    private int day = 0;

    @Getter
    private int month = 0;

    public Board(int[][] board, int[][][] boardMeaning) {
        this.board = board;
        this.boardMeaning = boardMeaning;
    }

    public static boolean isValid(int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell > 1)
                    return false;
            }
        }

        return true;
    }

    public boolean isValid() {
        return isValid(board);
    }

    public boolean isValidSolution() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell > 1) {
                    day = 0;
                    month = 0;
                    return false;
                } else if (cell == 0) {

                }
            }
        }

        if (day != 0 && month != 0)
            return true;
        return false;
    }

}
