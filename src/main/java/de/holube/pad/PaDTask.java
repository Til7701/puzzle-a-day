package de.holube.pad;

import de.holube.pad.util.Buffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class PaDTask extends RecursiveTask<Boolean> {

    private final Board board;

    private final Tile[] tiles;

    private final int tileIndex;

    private final Buffer<Board> solutionBuffer;

    public PaDTask(Board board, Tile[] tiles, int tileIndex, Buffer<Board> solutionBuffer) {
        this.board = board;
        this.tiles = tiles;
        this.tileIndex = tileIndex;
        this.solutionBuffer = solutionBuffer;
    }

    @Override
    protected Boolean compute() {
        try {
            if (tileIndex >= tiles.length) {
                if (board.isValidSolution()) {
                    try {
                        solutionBuffer.put(board);
                        return true;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                return false;
            }

            List<int[][]> tileCumBoards = tiles[tileIndex].getAllPositions();
            List<PaDTask> nextTasks = new ArrayList<>();

            for (int[][] tileCumBoard : tileCumBoards) {
                Board potentialNextBoard = board.addTile(tileCumBoard, tiles[tileIndex]);
                if (potentialNextBoard.isValid()) {
                    nextTasks.add(new PaDTask(potentialNextBoard, tiles, tileIndex + 1, solutionBuffer));
                }
            }

            invokeAll(nextTasks);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
