package de.holube.pad;

import java.util.concurrent.ForkJoinPool;

public class PuzzleADaySolver {

    private final Board board;

    private final Tile[] tiles;

    ForkJoinPool pool = new ForkJoinPool(16);

    public PuzzleADaySolver(Board board, Tile[] tiles) {
        this.board = board;
        this.tiles = tiles;
    }

    public void solve() {
        pool.invoke(new PaDTask(board, tiles, 0));
    }

}
