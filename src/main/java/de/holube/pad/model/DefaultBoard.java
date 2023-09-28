package de.holube.pad.model;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class DefaultBoard extends AbstractBoard {

    public static final byte[][] BOARD_LAYOUT = new byte[][]{
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
    protected static final byte MAX_KEY;

    static {
        MAX_KEY = (byte) Arrays.stream(BOARD_MEANING[0])
                .flatMapToInt(Arrays::stream)
                .summaryStatistics().getMax();
    }

    public DefaultBoard() {
        this(BOARD_LAYOUT, new ArrayList<>(), new ArrayList<>());
    }

    public DefaultBoard(byte[][] board, List<Tile> tiles, List<byte[][]> tileCumArrays) {
        super(board, tiles, tileCumArrays, MAX_KEY);
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
    protected Board createNewBoard(byte[][] newBoard, List<Tile> newTiles, List<byte[][]> newTileCumBoards) {
        return new DefaultBoard(newBoard, newTiles, newTileCumBoards);
    }

    @Override
    public byte getMaxKey() {
        return MAX_KEY;
    }

}
