package de.holube.pad;

import de.holube.pad.model.Board;
import de.holube.pad.model.Tile;
import de.holube.pad.solution.SolutionHandlerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class PuzzleADaySolver {

    private final Board board;

    private final Tile[] tiles;

    private final ForkJoinPool pool;

    private final SolutionHandlerFactory shf;

    public PuzzleADaySolver(Board board, Tile[] tiles, SolutionHandlerFactory shf, int parallelism) {
        this.board = board;
        this.tiles = tiles;
        this.shf = shf;
        this.pool = new ForkJoinPool(parallelism);
        System.out.println("Parallelism: " + parallelism);
    }

    public void solve() {
        List<byte[][]> tileCumBoards = tiles[0].getAllPositions();
        List<PaDTask> tasks = new ArrayList<>();

        for (byte[][] tileCumBoard : tileCumBoards) {
            Board potentialNextBoard = board.addTile(tileCumBoard, tiles[0]);
            if (potentialNextBoard != null) {
                tasks.add(new PaDTask(potentialNextBoard, tiles, 1, shf));
            }
        }

        for (int i = 0; i < tasks.size(); i++) {
            pool.invoke(tasks.get(i));
            System.out.println("Progress: " + (i + 1) + "/" + tasks.size());
        }
    }

}
