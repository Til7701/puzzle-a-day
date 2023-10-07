package de.holube.pad.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Config {

    @Getter
    @Setter
    private String jsonSource;
    private final String activeBoard;
    private final Map<String, BoardConfig> boards;
    private final List<String> activeTiles;
    private final Map<String, int[][]> tiles;

    @Getter
    private final boolean saveSolutions;
    @Getter
    private final boolean saveImages;
    @Getter
    private final int parallelism;

    public BoardConfig getBoard() {
        return boards.get(activeBoard);
    }

    public List<int[][]> getTiles() {
     return tiles.entrySet().stream()
        .filter(e -> activeTiles.contains(e.getKey()))
        .map(Map.Entry::getValue)
        .toList();
    }

    @Getter
    public static class BoardConfig {

        private int[][] layout;

        private int[][][] meaning;

    }

}
