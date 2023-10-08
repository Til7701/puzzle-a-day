package de.holube.pad;

import de.holube.pad.model.Board;
import de.holube.pad.model.PositionedTile;
import de.holube.pad.solution.SolutionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class PaDTask implements Callable<List<Board>> {

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
    public List<Board> call() {
        if (tileIndex >= positionedTiles.length) {
            if (board.isValidSolution()) {
                solutionHandler.handleSolution(board);
            }
            return Collections.emptyList();
        }

        List<Board> nextBoards = new ArrayList<>();

        for (PositionedTile positionedTile : positionedTiles[tileIndex]) {
            Board potentialNextBoard = board.addTile(positionedTile);
            if (potentialNextBoard != null) {
                nextBoards.add(potentialNextBoard);
            }
        }

        return nextBoards;
    }
}
