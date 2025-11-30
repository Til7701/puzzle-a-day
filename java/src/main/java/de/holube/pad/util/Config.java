package de.holube.pad.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Config {

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
    @Getter
    @Setter
    private String jsonSource;

    public BoardConfig getBoard() {
        return boards.get(activeBoard);
    }

    public List<int[][]> getTiles() {
        List<int[][]> arrays = new ArrayList<>(activeTiles.size());
        for (String activeTileKey : activeTiles) {
            arrays.add(tiles.get(activeTileKey));
        }
        return arrays;
    }

    @Getter
    public static class BoardConfig {

        private int[][] layout;

        private int[][][] meaning;

    }

}
