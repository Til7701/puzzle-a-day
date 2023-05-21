package de.holube.pad.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
public class Board {

    private final int[][] board;
    private final int[][][] boardMeaning;
    @Getter
    private final List<Tile> tiles = new ArrayList<>();
    @Getter
    private final List<int[][]> tileCumArrays = new ArrayList<>();

    @Getter
    private transient int day = 0;

    @Getter
    private transient int month = 0;

    public Board(int[][] board, int[][][] boardMeaning, List<Tile> tiles, List<int[][]> tileCumArrays) {
        this.board = board;
        this.boardMeaning = boardMeaning;
        this.tiles.addAll(tiles);
        this.tileCumArrays.addAll(tileCumArrays);
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
                    if (boardMeaning[0][i][j] == 1) {
                        if (day == 0) {
                            day = boardMeaning[1][i][j];
                        } else {
                            day = 0;
                            month = 0;
                            return false;
                        }
                    } else if (boardMeaning[0][i][j] == 0) { // check if month
                        if (month == 0) {
                            month = boardMeaning[1][i][j];
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

    public int[][] getBoard() {
        int[][] copy = new int[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, board[i].length);
        }

        return copy;
    }

    public Board addTile(int[][] tileCumBoard, Tile tile) {
        int[][] newBoard = new int[board.length][board[0].length];
        List<Tile> newTiles = new ArrayList<>(tiles);
        newTiles.add(tile);
        List<int[][]> newTileCumArrays = new ArrayList<>(tileCumArrays);
        newTileCumArrays.add(tileCumBoard);

        for (int i = 0; i < board.length; i++) {
            int[] row = board[i];
            for (int j = 0; j < row.length; j++) {
                newBoard[i][j] = board[i][j] + tileCumBoard[i][j];
            }
        }

        return new Board(newBoard, boardMeaning, newTiles, newTileCumArrays);
    }


}
