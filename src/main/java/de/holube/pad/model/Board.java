package de.holube.pad.model;

import de.holube.pad.util.SolutionStore;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;

@EqualsAndHashCode
public class Board {

    @Getter
    protected final int[] tileIndices;

    protected final PositionedTile[][] positionedTiles;

    @Getter
    protected final SolutionStore solutionStore;

    @Getter
    private final int[][] boardLayout;
    @Getter
    private final int[][][] boardMeaning;

    private final long bitmask;

    public Board(int[][] boardLayout, int[][][] boardMeaning) {
        this(new int[0], new PositionedTile[0][0], boardLayout, boardMeaning);
    }

    public Board(PositionedTile[][] positionedTiles, int[][] boardLayout, int[][][] boardMeaning) {
        this(new int[0], positionedTiles, boardLayout, boardMeaning);
    }

    public Board(int[] tileIndices, PositionedTile[][] newPositionedTiles, int[][] boardLayout, int[][][] boardMeaning) {
        this(tileIndices, newPositionedTiles,
                Arrays.stream(boardMeaning[0])
                        .flatMapToInt(Arrays::stream)
                        .summaryStatistics().getMax(), boardLayout, boardMeaning);
    }

    public Board(int[] tileIndices, PositionedTile[][] positionedTiles, int maxKey, int[][] boardLayout, int[][][] boardMeaning) {
        this.tileIndices = tileIndices;
        this.positionedTiles = positionedTiles;
        this.solutionStore = new SolutionStore(maxKey);
        this.boardLayout = boardLayout;
        this.boardMeaning = boardMeaning;

        long tmp = 0;
        for (int i = 0; i < tileIndices.length; i++) {
            tmp = tmp | positionedTiles[i][tileIndices[i]].getBitmask();
        }
        this.bitmask = tmp;
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

    private int getValueOfIndex(int i, int j) {
        return getValueOfIndex(i, j, tileIndices);
    }

    private int getValueOfIndex(int i, int j, int[] tileIndices) {
        int tmp = boardLayout[i][j];
        for (int k = 0; k < tileIndices.length; k++) {
            tmp += positionedTiles[k][tileIndices[k]].getCumulativeBoard()[i][j];
        }
        return tmp;
    }

    public Board addTile(PositionedTile positionedTile) {
        if ((bitmask & positionedTile.getBitmask()) != 0) {
            return null;
        }

        int[] newTileIndices = new int[tileIndices.length + 1];
        System.arraycopy(tileIndices, 0, newTileIndices, 0, tileIndices.length);
        newTileIndices[newTileIndices.length - 1] = positionedTile.getId();

        return new Board(newTileIndices, positionedTiles, boardLayout, boardMeaning);
    }

    public int[][] getBoard() {
        final int[][] board = new int[boardLayout.length][boardLayout[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = boardLayout[i][j];
                for (int k = 0; k < tileIndices.length; k++) {
                    board[i][j] += positionedTiles[k][tileIndices[k]].getCumulativeBoard()[i][j];
                }
            }
        }
        return board;
    }

    public PositionedTile[] getPositionedTiles() {
        PositionedTile[] result = new PositionedTile[tileIndices.length];
        for (byte i = 0; i < result.length; i++) {
            result[i] = positionedTiles[i][tileIndices[i]];
        }

        return result;
    }

    public boolean isValidSolution() {
        int[][] tmpBoard = getBoard();
        for (int i = 0; i < boardLayout.length; i++) {
            for (int j = 0; j < boardLayout[0].length; j++) {
                if (tmpBoard[i][j] == 0 && !solutionStore.add(boardMeaning[0][i][j], boardMeaning[1][i][j])) {
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

    public int getLayoutFreeSpaces() {
        int result = 0;
        for (int[] row : getBoardLayout()) {
            for (int cell : row) {
                if (cell == 0)
                    result++;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        String result = Arrays.toString(tileIndices);
        return result.substring(1, result.length() - 1);
    }

    public int getMaxKey() {
        return Arrays.stream(boardMeaning[0])
                .flatMapToInt(Arrays::stream)
                .summaryStatistics().getMax();
    }

}
