package de.holube.pad.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
public abstract class AbstractBoard implements Board {

    protected final int[][] board;

    @Getter
    protected final List<Tile> tiles = new ArrayList<>();
    @Getter
    protected final List<int[][]> tileCumArrays = new ArrayList<>();

    public AbstractBoard(int[][] board, List<Tile> tiles, List<int[][]> tileCumArrays) {
        this.board = board;
        this.tiles.addAll(tiles);
        this.tileCumArrays.addAll(tileCumArrays);
    }

    public boolean isValid() {
        return Board.isValid(getBoard());
    }

    public int[][] getBoard() {
        int[][] copy = new int[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, board[i].length);
        }

        return copy;
    }

}
