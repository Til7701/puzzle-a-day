package de.holube.pad.solution;

import de.holube.pad.model.Board;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public abstract class AbstractStats implements Stats {

    protected final Semaphore semaphore = new Semaphore(1);

    @Getter
    protected final List<Board> results = new ArrayList<>();
    protected int totalSolutions = 0;

    public void addSolution(Board board) {
        try {
            semaphore.acquire();
            /*if (results.contains(board)) {
                System.out.println("Duplicate detected! Month: " + board.getMonth() + " Day: " + board.getDay());
            }*/
            results.add(board);
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

}
