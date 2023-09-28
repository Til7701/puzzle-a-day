package de.holube.pad.util;

import lombok.Getter;

import java.util.Arrays;

public class SolutionStore {

    @Getter
    private final int[] values;

    public SolutionStore(int maxKey) {
        values = new int[maxKey + 1];
        reset();
    }

    public boolean add(int key, int value) {
        if (key < 0)
            return true;
        if (values[key] > -1)
            return false;
        values[key] = value;
        return true;
    }

    public boolean isComplete() {
        for (int v : values) {
            if (v == -1) {
                return false;
            }
        }
        return true;
    }

    public void reset() {
        Arrays.fill(values, -1);
    }

}
