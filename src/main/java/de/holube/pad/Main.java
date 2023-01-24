package de.holube.pad;

import de.holube.pad.util.ArrayProvider;

public class Main {

    public static void main(String[] args) {
        final Board board = new Board(ArrayProvider.BOARD_LAYOUT, ArrayProvider.BOARD_MEANING);

        final PuzzleADaySolver padSolver = new PuzzleADaySolver(board, new Tile[1]);

    }

}