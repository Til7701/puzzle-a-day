package de.holube.pad;

import de.holube.pad.util.ArrayProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final Board board = new Board(ArrayProvider.BOARD_LAYOUT, ArrayProvider.BOARD_MEANING, new ArrayList<>(), new ArrayList<>());

        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(ArrayProvider.TILE_S, "S", Color.RED, board));
        tiles.add(new Tile(ArrayProvider.TILE_l, "l", Color.BLUE, board));
        tiles.add(new Tile(ArrayProvider.TILE_L, "L", Color.YELLOW, board));
        tiles.add(new Tile(ArrayProvider.TILE_t, "t", Color.CYAN, board));
        tiles.add(new Tile(ArrayProvider.TILE_O, "O", Color.GREEN, board));
        tiles.add(new Tile(ArrayProvider.TILE_s, "s", Color.GRAY, board));
        tiles.add(new Tile(ArrayProvider.TILE_P, "P", Color.PINK, board));
        tiles.add(new Tile(ArrayProvider.TILE_C, "C", Color.ORANGE, board));

        int totalOptions = 1;
        for (Tile tile : tiles) {
            totalOptions *= tile.getAllPositions().size();
        }
        System.out.println("Total number of possible boards: " + totalOptions);

        long startTime = System.currentTimeMillis();
        final PuzzleADaySolver padSolver = new PuzzleADaySolver(board, tiles.toArray(new Tile[0]));
        padSolver.solve();
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("done in: " + time + "ms");
    }

}