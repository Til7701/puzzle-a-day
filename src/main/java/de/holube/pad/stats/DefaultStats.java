package de.holube.pad.stats;

import java.util.Arrays;
import java.util.IntSummaryStatistics;

public class DefaultStats extends AbstractStats {

    private final int[][] days = new int[12][31];

    @Override
    protected void addToDaysArray(byte[] values) {
        days[values[0] - 1][values[1] - 1] += 1;
    }

    @Override
    protected String getDaysArrayString() {
        return Arrays.deepToString(days);
    }

    public void calculateStats() {
        IntSummaryStatistics stats = Arrays.stream(days)
                .flatMapToInt(Arrays::stream)
                .summaryStatistics();
        max = stats.getMax();
        min = stats.getMin();
        average = stats.getAverage();
    }

}
