package de.holube.pad;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tile {

    private final int[][] base;

    private final List<int[][]> baseRotated;

    private final List<int[][]> allPositions;

    public Tile(int[][] base) {
        this.base = base;
        baseRotated = getAllRotations(base);
        allPositions = getAllPositions(baseRotated);
    }

    private List<int[][]> getAllRotations(int[][] base) {
        Set<int[][]> results = new HashSet<>();

        results.add(base);


        return results.stream().toList();
    }

    private List<int[][]> getAllPositions(List<int[][]> baseRotated) {
        Set<int[][]> results = new HashSet<>();


        return results.stream().toList();
    }

}
