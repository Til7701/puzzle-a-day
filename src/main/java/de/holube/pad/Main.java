package de.holube.pad;

import de.holube.pad.util.ArrayProvider;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final Board board = new Board(ArrayProvider.BOARD_LAYOUT, ArrayProvider.BOARD_MEANING);

        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(ArrayProvider.TILE_s, board));

        final PuzzleADaySolver padSolver = new PuzzleADaySolver(board, tiles.toArray(new Tile[0]));

    }

}