package de.holube.pad;

import de.holube.pad.model.Board;
import de.holube.pad.model.Tile;
import de.holube.pad.solution.SolutionHandlerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class PaDTask extends RecursiveTask<Boolean> {

    private final Board board;

    private final Tile[] tiles;

    private final int tileIndex;

    private final SolutionHandlerFactory shf;

    public PaDTask(Board board, Tile[] tiles, int tileIndex, SolutionHandlerFactory shf) {
        this.board = board;
        this.tiles = tiles;
        this.tileIndex = tileIndex;
        this.shf = shf;
    }

    @Override
    protected Boolean compute() {
        if (tileIndex >= tiles.length) {
            if (board.isValidSolution()) {
                shf.create().handleSolution(board);
                return true;
            }
            return false;
        }

        List<byte[][]> tileCumBoards = tiles[tileIndex].getAllPositions();
        List<PaDTask> nextTasks = new ArrayList<>();

        for (byte[][] tileCumBoard : tileCumBoards) {
            Board potentialNextBoard = board.addTile(tileCumBoard, tiles[tileIndex]);
            if (potentialNextBoard != null) {
                nextTasks.add(new PaDTask(potentialNextBoard, tiles, tileIndex + 1, shf));
            }
        }

        invokeAll(nextTasks);
        return false;
    }
}
