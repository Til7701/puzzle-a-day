package de.holube.pad.solution;

import de.holube.pad.model.Board;
import lombok.Getter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.Semaphore;

public abstract class AbstractStats implements Stats {

    protected final Semaphore semaphore = new Semaphore(1);

    @Getter
    protected final List<Board> results = new ArrayList<>();
    private final int[][][][] days = new int[10][10][12][31];
    protected int totalSolutions = 0;

    public void addSolution(Board board) {
        try {
            semaphore.acquire();
            /*if (results.contains(board)) {
                System.out.println("Duplicate detected! Month: " + board.getMonth() + " Day: " + board.getDay());
            }*/
            //results.add(board);
            int[] values = board.getSolutionStore().getValues();
            days[values[0]][values[1]][values[2] - 1][values[3] - 1] += 1;
            totalSolutions++;
            if (totalSolutions % 100 == 0) {
                System.out.println("Solutions received: " + totalSolutions);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    @Override
    public void save() {
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter("Stats_" + new Date().toString()));
            IntSummaryStatistics stats = Arrays.stream(days)
                    .flatMap(Arrays::stream)
                    .flatMap(Arrays::stream)
                    .flatMapToInt(Arrays::stream)
                    .summaryStatistics();
            output.append("Min: ").append(String.valueOf(stats.getMin())).append("\n")
                    .append("Max: ").append(String.valueOf(stats.getMax())).append("\n")
                    .append("Average: ").append(String.valueOf(stats.getAverage())).append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
