package de.holube.pad.util;

import de.holube.pad.Board;

import java.util.ArrayList;
import java.util.List;

public class Stats {

    private final int[] days = new int[12 * 31];
    List<Board> results = new ArrayList<>();

    private int totalSolutions = 0;

    public void addSolution(Board board) {
        if (results.contains(board)) {
            System.out.println("Duplicate detected! Month: " + board.getMonth() + " Day: " + board.getDay());
        }
        results.add(board);
        days[(board.getMonth() * 31) + board.getDay() - 31 - 1] += 1;
        totalSolutions++;
        if (totalSolutions % 100 == 0) {
            System.out.println("Solutions received: " + totalSolutions);
        }
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

    public void print() {
        System.out.println("Total: " + totalSolutions);
        System.out.println("Average: " + getAverage());
        System.out.println("Min: " + getMin());
        System.out.println("Max: " + getMax());
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 31; j++) {
                System.out.print(days[(i * 31) + j] + ", ");
            }
            System.out.println();
        }
    }

}
