package de.holube.pad;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tile {

    private final int[][] base;

    private final List<int[][]> baseRotated;

    private final List<int[][]> allPositions;

    public Tile(int[][] base, Board board) {
        this.base = base;
        baseRotated = getAllRotations(base);
        for (int[][] array : baseRotated) {
            print(array);
            System.out.println();
        }
        allPositions = getAllPositions(baseRotated, board);
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

    private List<int[][]> getAllRotations(int[][] base) {
        Set<int[][]> results = new HashSet<>();

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

    private List<int[][]> getAllPositions(List<int[][]> baseRotated, Board board) {
        Set<int[][]> results = new HashSet<>();

        return results.stream().toList();
    }

    private void print(int[][] array) {
        for (int[] ints : array) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

}
