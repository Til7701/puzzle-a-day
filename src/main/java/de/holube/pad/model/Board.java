package de.holube.pad.model;

import de.holube.pad.util.SolutionStore;

import java.util.List;

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

    boolean isValid();

    boolean isValidSolution();

    Board addTile(int[][] tileBoard, Tile tile);

    List<Tile> getTiles();

    List<int[][]> getTileCumArrays();

    String getPath();

    SolutionStore getSolutionStore();

    int getFreeSpaces();


}
