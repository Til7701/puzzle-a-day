package de.holube.pad.model;

import lombok.EqualsAndHashCode;

import java.util.Arrays;

@EqualsAndHashCode(callSuper = true)
public class DefaultBoard extends AbstractBoard {

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
    protected static final int MAX_KEY;

    static {
        MAX_KEY = Arrays.stream(BOARD_MEANING[0])
                .flatMapToInt(Arrays::stream)
                .summaryStatistics().getMax();
    }

    public DefaultBoard() {
        this(new int[0], new PositionedTile[0][0]);
    }

    public DefaultBoard(PositionedTile[][] positionedTiles) {
        this(new int[0], positionedTiles);
    }

    public DefaultBoard(int[] tileIndices, PositionedTile[][] newPositionedTiles) {
        super(tileIndices, newPositionedTiles, MAX_KEY);
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
    protected Board createNewBoard(int[] tileIndices, PositionedTile[][] newPositionedTiles) {
        return new DefaultBoard(tileIndices, newPositionedTiles);
    }

    @Override
    public int getMaxKey() {
        return MAX_KEY;
    }

}
