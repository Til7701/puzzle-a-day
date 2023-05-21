package de.holube.pad;

import de.holube.pad.util.SolutionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class PaDTask extends RecursiveTask<Boolean> {

    private final Board board;

    private final Tile[] tiles;

    private final int tileIndex;

    public PaDTask(Board board, Tile[] tiles, int tileIndex) {
        this.board = board;
        this.tiles = tiles;
        this.tileIndex = tileIndex;
    }

    @Override
    protected Boolean compute() {
        if (tileIndex >= tiles.length) {
            if (board.isValidSolution()) {
                new SolutionHandler().handleSolution(board);
                return true;
            }
            return false;
        }

        List<int[][]> tileCumBoards = tiles[tileIndex].getAllPositions();
        List<PaDTask> nextTasks = new ArrayList<>();

        for (int[][] tileCumBoard : tileCumBoards) {
            Board potentialNextBoard = board.addTile(tileCumBoard, tiles[tileIndex]);
            if (potentialNextBoard.isValid()) {
                nextTasks.add(new PaDTask(potentialNextBoard, tiles, tileIndex + 1));
            }
        }

        invokeAll(nextTasks);
        return false;
    }
}
