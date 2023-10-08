package de.holube.pad.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BitMaskUtil {

    public static long fromArray(int[][] array) {
        long mask = 0;
        for (int[] row : array) {
            for (int cell : row) {
                mask = mask | cell;
                mask = mask << 1;
            }
        }
        return mask;
    }

}
