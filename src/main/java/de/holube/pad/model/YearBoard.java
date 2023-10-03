package de.holube.pad.model;

import java.util.Arrays;

public class YearBoard extends AbstractBoard {

    public static final int[][] BOARD_LAYOUT = new int[][]{
            {1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1},
    };

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

    protected static final int MAX_KEY;

    static {
        MAX_KEY = Arrays.stream(BOARD_MEANING[0])
                .flatMapToInt(Arrays::stream)
                .summaryStatistics().getMax();
    }

    public YearBoard() {
        this(new int[0], new PositionedTile[0][0]);
    }

    public YearBoard(PositionedTile[][] positionedTiles) {
        this(new int[0], positionedTiles);
    }

    public YearBoard(int[] tileIndices, PositionedTile[][] newPositionedTiles) {
        super(tileIndices, newPositionedTiles, MAX_KEY);
    }

    @Override
    protected Board createNewBoard(int[] tileIndices, PositionedTile[][] newPositionedTiles) {
        return new YearBoard(tileIndices, newPositionedTiles);
    }

    @Override
    public int[][] getBoardLayout() {
        return BOARD_LAYOUT;
    }

    @Override
    public int[][][] getBoardMeaning() {
        return BOARD_MEANING;
    }

    @Override
    public int getMaxKey() {
        return MAX_KEY;
    }

}
