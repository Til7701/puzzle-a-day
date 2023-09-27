package de.holube.pad.solution;

import de.holube.pad.model.Board;
import lombok.Getter;

import java.util.Arrays;
import java.util.IntSummaryStatistics;

public class DefaultStats extends AbstractStats {

    private final int[][] days = new int[12][31];

    @Getter
    private int min;
    @Getter
    private int max;
    @Getter
    private double average;


    public void calculateStats() {
        for (Board result : results) {
            int[] values = result.getSolutionStore().getValues();
            days[values[0] - 1][values[1] - 1] += 1;
        }

        IntSummaryStatistics stats = Arrays.stream(days)
                .flatMapToInt(Arrays::stream)
                .summaryStatistics();
        max = stats.getMax();
        min = stats.getMin();
        average = stats.getAverage();
    }

    public void printStats() {
        System.out.println("Total: " + totalSolutions);
        System.out.println("Average: " + getAverage());
        System.out.println("Min: " + getMin());
        System.out.println("Max: " + getMax());
        System.out.println(Arrays.deepToString(days));
    }

    @Override
    public void save() {
        
    }

}