package de.holube.pad.stats;

import de.holube.pad.model.Board;
import lombok.Getter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

public abstract class AbstractStats implements Stats {

    protected final Semaphore semaphore = new Semaphore(1);

    @Getter
    protected final List<Board> results = new ArrayList<>();
    protected int totalSolutions = 0;

    @Getter
    protected int min;
    @Getter
    protected int max;
    @Getter
    protected double average;

    protected abstract void addToDaysArray(int[] values);

    protected abstract String getDaysArrayString();

    public void addSolution(Board board) {
        try {
            semaphore.acquire();
            int[] values = board.getSolutionStore().getValues();
            addToDaysArray(values);
            totalSolutions++;
            if (totalSolutions % 1000 == 0) {
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
    public void save(String jsonConfig) {
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter("Stats_" + new Date().toString().replace(":", "-")));

            output.append("Min: ").append(String.valueOf(getMin())).append("\n")
                    .append("Max: ").append(String.valueOf(getMax())).append("\n")
                    .append("Average: ").append(String.valueOf(getAverage())).append("\n")
                    .append(getDaysArrayString()).append("\n")
                    .append(jsonConfig);
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

    @Override
    public void printStats() {
        System.out.println("Total: " + totalSolutions);
        System.out.println("Average: " + getAverage());
        System.out.println("Min: " + getMin());
        System.out.println("Max: " + getMax());
    }

}
