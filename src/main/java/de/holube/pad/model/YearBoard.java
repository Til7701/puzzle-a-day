package de.holube.pad.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YearBoard extends AbstractBoard {

    public static final byte[][] BOARD_LAYOUT = new byte[][]{
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

    protected static final byte MAX_KEY;

    static {
        MAX_KEY = (byte) Arrays.stream(BOARD_MEANING[0])
                .flatMapToInt(Arrays::stream)
                .summaryStatistics().getMax();
    }

    public YearBoard() {
        this(BOARD_LAYOUT, new ArrayList<>());
    }

    public YearBoard(byte[][] board, List<PositionedTile> tiles) {
        super(board, tiles, MAX_KEY);
    }

    @Override
    protected Board createNewBoard(byte[][] newBoard, List<PositionedTile> newTiles) {
        return new YearBoard(newBoard, newTiles);
    }

    @Override
    public byte[][] getBoardLayout() {
        return BOARD_LAYOUT;
    }

    @Override
    public int[][][] getBoardMeaning() {
        return BOARD_MEANING;
    }

    @Override
    public byte getMaxKey() {
        return MAX_KEY;
    }

}
