package de.holube.pad.model;

import de.holube.pad.util.SolutionStore;

public interface Board {

    static boolean isValid(int[][] board) {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell > 1)
                    return false;
            }
        }

        return true;
    }

    int[][] getBoard();

    int[] getTileIndices();

    boolean isValid();

    boolean isValidSolution();

    Board addTile(PositionedTile positionedTile);

    PositionedTile[] getPositionedTiles();

    String getPath();

    SolutionStore getSolutionStore();

    int getLayoutFreeSpaces();


    int getMaxKey();

    int[][][] getBoardMeaning();

    int[][] getBoardLayout();

}
