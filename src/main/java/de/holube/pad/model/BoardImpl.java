package de.holube.pad.model;

import de.holube.pad.util.Config;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

@EqualsAndHashCode(callSuper = true)
public class BoardImpl extends AbstractBoard {

    public BoardImpl(int[][] boardLayout, int[][][] boardMeaning) {
        this(new int[0], new PositionedTile[0][0], boardLayout, boardMeaning);
    }

    public BoardImpl(PositionedTile[][] positionedTiles, int[][] boardLayout, int[][][] boardMeaning) {
        this(new int[0], positionedTiles, boardLayout, boardMeaning);
    }

    public BoardImpl(int[] tileIndices, PositionedTile[][] newPositionedTiles, int[][] boardLayout, int[][][] boardMeaning) {
        super(tileIndices, newPositionedTiles,
                Arrays.stream(boardMeaning[0])
                .flatMapToInt(Arrays::stream)
                .summaryStatistics().getMax(), boardLayout, boardMeaning);
    }

    @Override
    protected Board createNewBoard(int[] tileIndices, PositionedTile[][] newPositionedTiles) {
        return new BoardImpl(tileIndices, newPositionedTiles, getBoardLayout(), getBoardMeaning());
    }

    @Override
    public int getMaxKey() {
        return Arrays.stream(getBoardMeaning()[0])
                .flatMapToInt(Arrays::stream)
                .summaryStatistics().getMax();
    }

}
