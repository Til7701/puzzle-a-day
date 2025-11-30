package de.holube.pad.stats;

import java.util.Arrays;
import java.util.IntSummaryStatistics;

public class YearStats extends AbstractStats {

    private final int[][][][] days = new int[10][10][12][31];

    @Override
    protected void addToDaysArray(int[] values) {
        days[values[0]][values[1]][values[2] - 1][values[3] - 1] += 1;
    }

    @Override
    protected String getDaysArrayString() {
        return Arrays.deepToString(days);
    }

    public void calculateStats() {
        IntSummaryStatistics stats = Arrays.stream(days)
                .flatMap(Arrays::stream)
                .flatMap(Arrays::stream)
                .flatMapToInt(Arrays::stream)
                .summaryStatistics();
        max = stats.getMax();
        min = stats.getMin();
        average = stats.getAverage();
    }

}
