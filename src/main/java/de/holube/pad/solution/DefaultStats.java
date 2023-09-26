package de.holube.pad.solution;

import de.holube.pad.model.Board;

public class DefaultStats extends AbstractStats {

    private final int[] days = new int[12 * 31];


    public void calculateStats() {
        for (Board result : results) {
            days[(result.getSolutionStore().getValues()[0] * 31) + result.getSolutionStore().getValues()[1] - 31 - 1] += 1;
        }
    }

    public void printStats() {
        System.out.println("Total: " + totalSolutions);
        System.out.println("Average: " + getAverage());
        System.out.println("Min: " + getMin());
        System.out.println("Max: " + getMax());
    }

    public double getAverage() {
        int sum = 0;
        for (int day : days) {
            sum += day;
        }
        return sum / (double) days.length;
    }

    public int getMin() {
        int min = Integer.MAX_VALUE;
        for (int day : days) {
            if (day < min)
                min = day;
        }
        return min;
    }

    public int getMax() {
        int max = 0;
        for (int day : days) {
            if (day > max)
                max = day;
        }
        return max;
    }

}
