package de.holube.pad.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Config {

    public Target TARGET;
    public List<String> ACTIVE_TILES;
    public Map<String, int[][]> TILES;

    public enum Target {
        DEFAULT,
        YEAR
    }

}
