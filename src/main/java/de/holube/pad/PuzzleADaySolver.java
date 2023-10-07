package de.holube.pad;

import de.holube.pad.model.Board;
import de.holube.pad.model.PositionedTile;
import de.holube.pad.solution.SolutionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class PuzzleADaySolver {

    private final Board board;

    private final PositionedTile[][] positionedTiles;

    private final ForkJoinPool pool;

    private final SolutionHandler solutionHandler;

    public PuzzleADaySolver(Board board, PositionedTile[][] positionedTiles, SolutionHandler solutionHandler, int parallelism) {
        this.board = board;
        this.positionedTiles = positionedTiles;
        this.solutionHandler = solutionHandler;
        this.pool = new ForkJoinPool(parallelism);
        System.out.println("Parallelism: " + parallelism);
    }

    public void solve() {
        List<PaDTask> tasks = new ArrayList<>();

        for (PositionedTile positionedTile : positionedTiles[0]) {
            Board potentialNextBoard = board.addTile(positionedTile);
            if (potentialNextBoard != null) {
                tasks.add(new PaDTask(potentialNextBoard, positionedTiles, 1, solutionHandler));
            }
        }

        for (int i = 0; i < tasks.size(); i++) {
            pool.invoke(tasks.get(i));
            System.out.println("Progress: " + (i + 1) + "/" + tasks.size());
        }
    }

}
