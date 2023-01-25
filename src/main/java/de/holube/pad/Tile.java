package de.holube.pad;

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
    private final int[][] base;
    @Getter
    private final List<int[][]> allPositions;

    public Tile(int[][] base, String name, Color color, Board board) {
        this.name = name;
        this.color = color;
        this.base = base;
        System.out.println("Creating Tile: " + name);
        List<int[][]> baseRotated = getAllRotations(base);
        System.out.println("Number of Rotations: " + baseRotated.size());
        allPositions = getAllPositions(baseRotated, board);
        System.out.println("Number of Boards: " + allPositions.size());
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

    private static List<int[][]> getAllPositions(List<int[][]> baseRotated, Board board) {
        Set<int[][]> results = new HashSet<>();

        return results.stream().toList();
    }

    private static void print(int[][] array) {
        for (int[] ints : array) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    private static void printAll(List<int[][]> list) {
        for (int[][] ints : list) {
            print(ints);
            System.out.println();
        }
    }

}
