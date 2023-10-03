package de.holube.pad.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Config {

    public final Target TARGET;
    public final List<String> ACTIVE_TILES;
    public final Map<String, int[][]> TILES;

    public final boolean SAVE_SOLUTIONS;
    public final boolean SAVE_IMAGES;
    public final int PARALLELISM;

    public enum Target {
        DEFAULT,
        YEAR
    }

}
