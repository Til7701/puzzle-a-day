package de.holube.pad;

import de.holube.pad.model.Board;
import de.holube.pad.model.Tile;
import de.holube.pad.solution.SolutionHandlerFactory;
import de.holube.pad.util.pool.CustomPool;
import de.holube.pad.util.pool.CustomTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PuzzleADaySolver {

    private final Board board;

    private final Tile[] tiles;

    private final CustomPool pool;

    private final SolutionHandlerFactory shf;

    public PuzzleADaySolver(Board board, Tile[] tiles, SolutionHandlerFactory shf) {
        this.board = board;
        this.tiles = tiles;
        this.shf = shf;
        final int parallelism = (int) (Runtime.getRuntime().availableProcessors() * 0.7);
        this.pool = new CustomPool(parallelism);
        System.out.println("Parallelism: " + parallelism);
    }

    public void solve() {
        List<byte[][]> tileCumBoards = tiles[0].getAllPositions();
        List<Board> boards = new ArrayList<>();

        for (byte[][] tileCumBoard : tileCumBoards) {
            Board potentialNextBoard = board.addTile(tileCumBoard, tiles[0]);
            if (potentialNextBoard != null) {
                boards.add(potentialNextBoard);
            }
        }

        CountDownLatch latch = new CountDownLatch(boards.size());
        List<CustomTask> tasks = new ArrayList<>();
        for (Board board : boards) {
            tasks.add(new InitialPaDTask(latch, boards.size(), board, tiles, 1, shf));
        }

        pool.invokeAll(tasks);
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
