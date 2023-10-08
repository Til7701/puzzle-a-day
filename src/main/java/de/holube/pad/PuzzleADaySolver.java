package de.holube.pad;

import de.holube.pad.model.Board;
import de.holube.pad.model.PositionedTile;
import de.holube.pad.solution.SolutionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class PuzzleADaySolver {

    private final ExecutorService pool;

    private final List<Board> boards;
    private final int tileIndex;
    private final PositionedTile[][] positionedTiles;
    private final SolutionHandler solutionHandler;

    public PuzzleADaySolver(ExecutorService pool, List<Board> boards, int tileIndex, PositionedTile[][] positionedTiles, SolutionHandler solutionHandler) {
        this.boards = boards;
        this.tileIndex = tileIndex;
        this.positionedTiles = positionedTiles;
        this.solutionHandler = solutionHandler;
        this.pool = pool;
    }

    public List<Board> solve() throws ExecutionException, InterruptedException {
        List<Board> nextBoards = new ArrayList<>();
        List<Future<List<Board>>> futures = new ArrayList<>();

        for (Board board : boards) {
            futures.add(pool.submit(new PaDTask(board, positionedTiles, tileIndex, solutionHandler)));
        }

        for (Future<List<Board>> future : futures) {
            nextBoards.addAll(future.get());
        }

        return nextBoards;
    }

}
