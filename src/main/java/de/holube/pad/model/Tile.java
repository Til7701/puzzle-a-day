package de.holube.pad.model;

import lombok.Getter;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Tile {

    @Getter
    private final String name;
    @Getter
    private final Color color;

    @Getter
    private final byte[][] base;
    @Getter
    private final List<PositionedTile> allPositions;

    public Tile(byte[][] base, String name, Color color, Board board) {
        this.name = name;
        this.color = color;
        this.base = base;
        System.out.println("Creating Tile: " + name);
        print(base);
        List<byte[][]> baseRotated = getAllRotations(base);
        System.out.println("Number of Rotations: " + baseRotated.size());
        allPositions = getAllPositions(baseRotated, board);
        System.out.println("Number of Boards: " + allPositions.size());
    }

    private static byte[][] rotate90Clockwise(byte[][] origin) {
        byte[][] result = new byte[origin[0].length][origin.length];

        for (int i = 0; i < origin.length; i++) {
            for (int j = 0; j < origin[i].length; j++) {
                int newJ = origin[i].length - j - 1;
                result[newJ][i] = origin[i][j];
            }
        }

        return result;
    }

    private static byte[][] flip(byte[][] origin) {
        byte[][] result = new byte[origin[0].length][origin.length];

        for (int i = 0; i < origin.length; i++) {
            for (int j = 0; j < origin[i].length; j++) {
                result[j][i] = origin[i][j];
            }
        }

        return result;
    }

    private static List<byte[][]> getAllRotations(byte[][] base) {
        Set<byte[][]> results = new TreeSet<>((a1, a2) -> {
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
        byte[][] tmp = rotate90Clockwise(base);
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

    private static void removeBoard(byte[][] newBoard, byte[][] boardArray) {
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                newBoard[i][j] -= boardArray[i][j];
            }
        }
    }

    private static byte[][] place(byte[][] tile, byte[][] boardArray, int i, int j) {
        byte[][] copy = new byte[boardArray.length][boardArray[0].length];

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

    private static void print(byte[][] array) {
        for (byte[] bytes : array) {
            for (byte b : bytes) {
                System.out.print(b + " ");
            }
            System.out.println();
        }
    }

    private List<PositionedTile> getAllPositions(List<byte[][]> baseRotated, Board board) {
        Set<PositionedTile> results = new HashSet<>();

        for (byte[][] tile : baseRotated) {
            results.addAll(getAllPositionsForTile(tile, board));
        }

        return results.stream().toList();
    }

    private List<PositionedTile> getAllPositionsForTile(byte[][] tile, Board board) {
        Set<PositionedTile> results = new HashSet<>();
        byte[][] boardArray = board.getBoard();

        for (int i = 0; i < boardArray.length - tile.length + 1; i++) {
            for (int j = 0; j < boardArray[i].length - tile[0].length + 1; j++) {
                byte[][] newBoard = place(tile, boardArray, i, j);
                if (Board.isValid(newBoard)) {
                    removeBoard(newBoard, boardArray);
                    results.add(new PositionedTile(this, newBoard));
                }
            }
        }

        return results.stream().toList();
    }

    public int getOccupiedSpaces() {
        int result = 0;
        for (byte[] bytes : base) {
            for (byte b : bytes) {
                if (b == 1)
                    result++;
            }
        }
        return result;
    }

}
