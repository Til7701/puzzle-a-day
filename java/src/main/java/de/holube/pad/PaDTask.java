package de.holube.pad;

import de.holube.pad.model.Board;
import de.holube.pad.model.PositionedTile;
import de.holube.pad.solution.SolutionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class PaDTask extends RecursiveTask<Boolean> {

    private final Board board;

    private final PositionedTile[][] positionedTiles;

    private final int tileIndex;

    private final SolutionHandler solutionHandler;

    public PaDTask(Board board, PositionedTile[][] positionedTiles, int tileIndex, SolutionHandler solutionHandler) {
        this.board = board;
        this.positionedTiles = positionedTiles;
        this.tileIndex = tileIndex;
        this.solutionHandler = solutionHandler;
    }

    @Override
    protected Boolean compute() {
        if (tileIndex >= positionedTiles.length) {
            if (board.isValidSolution()) {
                solutionHandler.handleSolution(board);
                return true;
            }
            return false;
        }

        List<PaDTask> nextTasks = new ArrayList<>();

        for (PositionedTile positionedTile : positionedTiles[tileIndex]) {
            Board potentialNextBoard = board.addTile(positionedTile);
            if (potentialNextBoard != null) {
                nextTasks.add(new PaDTask(potentialNextBoard, positionedTiles, tileIndex + 1, solutionHandler));
            }
        }

        invokeAll(nextTasks);
        return false;
    }
}
