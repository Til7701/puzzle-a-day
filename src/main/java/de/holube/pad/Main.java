package de.holube.pad;

import de.holube.pad.util.ArrayProvider;
import de.holube.pad.util.Buffer;
import de.holube.pad.util.SemBuffer;
import de.holube.pad.util.SolutionHandlerThread;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final Board board = new Board(ArrayProvider.BOARD_LAYOUT, ArrayProvider.BOARD_MEANING, new ArrayList<>(), new ArrayList<>());

        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(ArrayProvider.TILE_S, "S", Color.RED, board));
        tiles.add(new Tile(ArrayProvider.TILE_l, "l", Color.RED, board));
        tiles.add(new Tile(ArrayProvider.TILE_L, "L", Color.RED, board));
        tiles.add(new Tile(ArrayProvider.TILE_t, "t", Color.RED, board));
        tiles.add(new Tile(ArrayProvider.TILE_O, "O", Color.RED, board));
        tiles.add(new Tile(ArrayProvider.TILE_s, "s", Color.RED, board));
        tiles.add(new Tile(ArrayProvider.TILE_P, "P", Color.RED, board));
        tiles.add(new Tile(ArrayProvider.TILE_C, "C", Color.RED, board));

        int totalOptions = 1;
        for (Tile tile : tiles) {
            totalOptions *= tile.getAllPositions().size();
        }
        System.out.println("Total number of possible boards: " + totalOptions);

        Buffer<Board> solutionBuffer = new SemBuffer<>(100);
        SolutionHandlerThread thread = new SolutionHandlerThread(solutionBuffer);
        thread.start();

        final PuzzleADaySolver padSolver = new PuzzleADaySolver(board, tiles.toArray(new Tile[0]), solutionBuffer);
        padSolver.solve();
    }

}