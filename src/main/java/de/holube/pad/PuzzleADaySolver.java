package de.holube.pad;

import de.holube.pad.util.Buffer;

import java.util.concurrent.ForkJoinPool;

public class PuzzleADaySolver {

    private final Board board;

    private final Tile[] tiles;

    private final Buffer<Board> solutionBuffer;

    ForkJoinPool pool = new ForkJoinPool();

    public PuzzleADaySolver(Board board, Tile[] tiles, Buffer<Board> solutionBuffer) {
        this.board = board;
        this.tiles = tiles;
        this.solutionBuffer = solutionBuffer;
    }

    public void solve() {
        pool.invoke(new PaDTask(board, tiles, 0, solutionBuffer));
    }


}
