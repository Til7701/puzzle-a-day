package de.holube.pad.model;

import de.holube.pad.util.SolutionStore;

import java.util.List;

public interface Board {

    static boolean isValid(byte[][] board) {
        for (byte[] row : board) {
            for (byte cell : row) {
                if (cell > 1)
                    return false;
            }
        }

        return true;
    }

    byte[][] getBoard();

    boolean isValid();

    boolean isValidSolution();

    Board addTile(PositionedTile positionedTile);

    List<PositionedTile> getPositionedTiles();

    String getPath();

    SolutionStore getSolutionStore();

    int getFreeSpaces();

    byte getMaxKey();


    int[][][] getBoardMeaning();

    byte[][] getBoardLayout();

}
