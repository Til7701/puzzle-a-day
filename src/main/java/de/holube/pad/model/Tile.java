package de.holube.pad.model;

import lombok.Getter;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Tile {

    @Getter
    private final String name;
    @Getter
    private final int tileNumber;
    @Getter
    private final Color color;

    @Getter
    private final int[][] base;
    @Getter
    private final PositionedTile[] allPositions;

    private int positionedTileCounter = 0;

    public Tile(int[][] base, String name, int tileNumber, Color color, Board board) {
        this.name = name;
        this.tileNumber = tileNumber;
        this.color = color;
        this.base = base;
        System.out.println("Creating Tile: " + name);
        print(base);
        List<int[][]> baseRotated = getAllRotations(base);
        System.out.println("Number of Rotations: " + baseRotated.size());
        allPositions = getAllPositions(baseRotated, board).toArray(new PositionedTile[0]);
        System.out.println("Number of Boards: " + allPositions.length);
    }

    private static int[][] rotate90Clockwise(int[][] origin) {
        int[][] result = new int[origin[0].length][origin.length];

        for (int i = 0; i < origin.length; i++) {
            for (int j = 0; j < origin[i].length; j++) {
                int newJ = origin[i].length - j - 1;
                result[newJ][i] = origin[i][j];
            }
        }

        return result;
    }

    private static int[][] flip(int[][] origin) {
        int[][] result = new int[origin[0].length][origin.length];

        for (int i = 0; i < origin.length; i++) {
            for (int j = 0; j < origin[i].length; j++) {
                result[j][i] = origin[i][j];
            }
        }

        return result;
    }

    private static List<int[][]> getAllRotations(int[][] base) {
        Set<int[][]> results = new TreeSet<>((a1, a2) -> {
            if (a1.length > a2.length)
                return 1;
            if (a1.length < a2.length)
                return -1;
            if (a1[0].length > a2[0].length)
                return 1;
            if (a1[0].length < a2[0].length)
                return -1;

            for (int i = 0; i < a1.length; i++) {
                for (int j = 0; j < a1[i].length; j++) {
                    if (a1[i][j] > a2[i][j])
                        return 1;
                    if (a1[i][j] < a2[i][j])
                        return -1;
                }
            }

            return 0;
        });

        results.add(base);
        int[][] tmp = rotate90Clockwise(base);
        results.add(tmp);
        tmp = rotate90Clockwise(tmp);
        results.add(tmp);
        tmp = rotate90Clockwise(tmp);
        results.add(tmp);
        tmp = flip(base);
        results.add(tmp);
        tmp = rotate90Clockwise(tmp);
        results.add(tmp);
        tmp = rotate90Clockwise(tmp);
        results.add(tmp);
        tmp = rotate90Clockwise(tmp);
        results.add(tmp);

        return results.stream().toList();
    }

    private static void removeBoard(int[][] newBoard, int[][] boardArray) {
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                newBoard[i][j] -= boardArray[i][j];
            }
        }
    }

    private static int[][] place(int[][] tile, int[][] boardArray, int i, int j) {
        int[][] copy = new int[boardArray.length][boardArray[0].length];

        for (int m = 0; m < boardArray.length; m++) {
            System.arraycopy(boardArray[m], 0, copy[m], 0, boardArray[m].length);
        }

        for (int k = 0; k < tile.length; k++) {
            for (int l = 0; l < tile[k].length; l++) {
                copy[i + k][j + l] += tile[k][l];
            }
        }
        return copy;
    }

    private static void print(int[][] array) {
        for (int[] row : array) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    private List<PositionedTile> getAllPositions(List<int[][]> baseRotated, Board board) {
        Set<PositionedTile> results = new HashSet<>();

        for (int[][] tile : baseRotated) {
            results.addAll(getAllPositionsForTile(tile, board));
        }
        List<PositionedTile> tiles = new ArrayList<>(results.stream().toList());
        tiles.sort(Comparator.comparingInt(PositionedTile::getId));
        return tiles;
    }

    private List<PositionedTile> getAllPositionsForTile(int[][] tile, Board board) {
        Set<PositionedTile> results = new HashSet<>();
        int[][] boardArray = board.getBoard();

        for (int i = 0; i < boardArray.length - tile.length + 1; i++) {
            for (int j = 0; j < boardArray[i].length - tile[0].length + 1; j++) {
                int[][] newBoard = place(tile, boardArray, i, j);
                if (Board.isValid(newBoard)) {
                    removeBoard(newBoard, boardArray);
                    results.add(new PositionedTile(this, newBoard, positionedTileCounter++, tileNumber));
                }
            }
        }

        return results.stream().toList();
    }

    public int getOccupiedSpaces() {
        int result = 0;
        for (int[] row : base) {
            for (int cell : row) {
                if (cell == 1)
                    result++;
            }
        }
        return result;
    }

}
