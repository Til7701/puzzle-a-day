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
    private final String ACTIVE_BOARD;
    private final Map<String, BoardConfig> BOARDS;
    private final List<String> ACTIVE_TILES;
    private final Map<String, int[][]> TILES;

    private final boolean SAVE_SOLUTIONS;
    private final boolean SAVE_IMAGES;
    private final int PARALLELISM;

    public BoardConfig getBoard() {
        return BOARDS.get(ACTIVE_BOARD);
    }

    public List<int[][]> getTiles() {
     return TILES.entrySet().stream()
        .filter(e -> ACTIVE_TILES.contains(e.getKey()))
        .map(Map.Entry::getValue)
        .toList();
    }

    public boolean isSaveSolutions() {
        return SAVE_SOLUTIONS;
    }

    public boolean isSaveImages() {
        return SAVE_IMAGES;
    }

    public int getParallelism() {
        return PARALLELISM;
    }

    @Getter
    public static class BoardConfig {

        private int[][] LAYOUT;

        private int[][][] MEANING;

    }

}
