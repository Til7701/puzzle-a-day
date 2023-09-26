package de.holube.pad.model;

import de.holube.pad.util.SolutionStore;
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

    @Getter
    protected SolutionStore solutionStore;

    public AbstractBoard(int[][] board, List<Tile> tiles, List<int[][]> tileCumArrays, int maxKey) {
        this.board = board;
        this.tiles.addAll(tiles);
        this.tileCumArrays.addAll(tileCumArrays);
        this.solutionStore = new SolutionStore(maxKey);
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

    public Board addTile(int[][] tileCumBoard, Tile tile) {
        int[][] newBoard = new int[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                newBoard[i][j] = board[i][j] + tileCumBoard[i][j];
                if (newBoard[i][j] >= 2) {
                    return null;
                }
            }
        }

        List<Tile> newTiles = new ArrayList<>(tiles.size() + 1);
        newTiles.addAll(tiles);
        newTiles.add(tile);
        List<int[][]> newTileCumArrays = new ArrayList<>(tileCumArrays.size() + 1);
        newTileCumArrays.addAll(tileCumArrays);
        newTileCumArrays.add(tileCumBoard);

        return new DefaultBoard(newBoard, newTiles, newTileCumArrays);
    }

    @Override
    public boolean isValidSolution() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    if (!solutionStore.add(getBoardMeaning()[0][i][j], getBoardMeaning()[1][i][j])) {
                        solutionStore.reset();
                        return false;
                    }
                } else if (board[i][j] > 1) {
                    solutionStore.reset();
                    return false;
                }
            }
        }

        if (solutionStore.isComplete()) {
            return true;
        }
        solutionStore.reset();
        return false;
    }

    public String getPath() {
        int[] solution = solutionStore.getValues();
        StringBuilder path = new StringBuilder();
        for (int s : solution) {
            path.append(s).append("/");
        }
        return path.toString();
    }

    protected abstract int[][] getBoardLayout();

    protected abstract int[][][] getBoardMeaning();

    public int getFreeSpaces() {
        int result = 0;
        for (int[] ints : board) {
            for (int anInt : ints) {
                if (anInt == 0)
                    result++;
            }
        }
        return result;
    }

}
