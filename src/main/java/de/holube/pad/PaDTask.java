package de.holube.pad;

import de.holube.pad.model.Board;
import de.holube.pad.model.Tile;
import de.holube.pad.solution.SolutionHandlerFactory;
import de.holube.pad.util.pool.CustomTask;

import java.util.ArrayList;
import java.util.List;

public class PaDTask extends CustomTask {

    private final Board board;

    private final Tile[] tiles;

    private final int tileIndex;

    private final SolutionHandlerFactory shf;

    public PaDTask(Board board, Tile[] tiles, int tileIndex, SolutionHandlerFactory shf) {
        this.board = board;
        this.tiles = tiles;
        this.tileIndex = tileIndex;
        this.shf = shf;
    }

    @Override
    public void compute() {
        if (tileIndex >= tiles.length) {
            if (board.isValidSolution()) {
                shf.create().handleSolution(board);
                return;
            }
            return;
        }

        List<byte[][]> tileCumBoards = tiles[tileIndex].getAllPositions();
        List<CustomTask> nextTasks = new ArrayList<>();

        for (byte[][] tileCumBoard : tileCumBoards) {
            Board potentialNextBoard = board.addTile(tileCumBoard, tiles[tileIndex]);
            if (potentialNextBoard != null) {
                nextTasks.add(new PaDTask(potentialNextBoard, tiles, tileIndex + 1, shf));
            }
        }

        getPool().invokeAll(nextTasks);
    }
}
