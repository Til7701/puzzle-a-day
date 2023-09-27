package de.holube.pad.util;

import de.holube.pad.model.Board;
import de.holube.pad.model.Tile;

import java.util.List;

public class PlausibilityCheck {

    public static boolean check(Board board, List<Tile> tiles) {
        return tileSpaces(board, tiles);
    }

    private static boolean tileSpaces(Board board, List<Tile> tiles) {
        int tileOccupiedSpaces = 0;
        for (Tile tile : tiles) {
            tileOccupiedSpaces += tile.getOccupiedSpaces();
        }

        int boardFreeSpaces = board.getFreeSpaces();

        int difference = Math.abs(boardFreeSpaces - (tileOccupiedSpaces + board.getMaxKey() + 1));

        System.out.println("Board Spaces: " + boardFreeSpaces);
        System.out.println("Tile Spaces: " + tileOccupiedSpaces);
        System.out.println("Difference: " + difference);

        return difference == 0;
    }

}
