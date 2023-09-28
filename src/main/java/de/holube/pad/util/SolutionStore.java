package de.holube.pad.util;

import lombok.Getter;

import java.util.Arrays;

public class SolutionStore {

    @Getter
    private final byte[] values;

    public SolutionStore(byte maxKey) {
        values = new byte[maxKey + 1];
        reset();
    }

    public boolean add(byte key, byte value) {
        if (key < 0)
            return true;
        if (values[key] > -1)
            return false;
        values[key] = value;
        return true;
    }

    public boolean isComplete() {
        for (byte v : values) {
            if (v == -1) {
                return false;
            }
        }
        return true;
    }

    public void reset() {
        Arrays.fill(values, (byte) -1);
    }

}
