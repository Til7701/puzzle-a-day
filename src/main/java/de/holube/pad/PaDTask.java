package de.holube.pad;

import de.holube.pad.model.Board;
import de.holube.pad.model.PositionedTile;
import de.holube.pad.solution.SolutionHandlerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class PaDTask extends RecursiveTask<Boolean> {

    private final Board board;

    private final PositionedTile[][] positionedTiles;

    private final int tileIndex;

    private final SolutionHandlerFactory shf;

    public PaDTask(Board board, PositionedTile[][] positionedTiles, int tileIndex, SolutionHandlerFactory shf) {
        this.board = board;
        this.positionedTiles = positionedTiles;
        this.tileIndex = tileIndex;
        this.shf = shf;
    }

    @Override
    protected Boolean compute() {
        if (tileIndex >= positionedTiles.length) {
            if (board.isValidSolution()) {
                shf.create().handleSolution(board);
                return true;
            }
            return false;
        }

        List<PaDTask> nextTasks = new ArrayList<>();

        for (PositionedTile positionedTile : positionedTiles[tileIndex]) {
            Board potentialNextBoard = board.addTile(positionedTile);
            if (potentialNextBoard != null) {
                nextTasks.add(new PaDTask(potentialNextBoard, positionedTiles, tileIndex + 1, shf));
            }
        }

        invokeAll(nextTasks);
        return false;
    }
}
