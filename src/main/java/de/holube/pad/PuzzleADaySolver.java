package de.holube.pad;

import de.holube.pad.model.Board;
import de.holube.pad.model.Tile;
import de.holube.pad.solution.SolutionHandlerFactory;

import java.util.concurrent.ForkJoinPool;

public class PuzzleADaySolver {

    private final Board board;

    private final Tile[] tiles;

    private final ForkJoinPool pool = new ForkJoinPool(10);

    private final SolutionHandlerFactory shf;

    public PuzzleADaySolver(Board board, Tile[] tiles, SolutionHandlerFactory shf) {
        this.board = board;
        this.tiles = tiles;
        this.shf = shf;
    }

    public void solve() {
        pool.invoke(new PaDTask(board, tiles, 0, shf));
    }

}
