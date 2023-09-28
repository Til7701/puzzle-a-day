package de.holube.pad;

import de.holube.pad.model.Board;
import de.holube.pad.model.Tile;
import de.holube.pad.solution.SolutionHandlerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class InitialPaDTask extends PaDTask {

    private static final AtomicInteger progress = new AtomicInteger(0);
    private final int initialTaskNumber;
    private final CountDownLatch latch;

    public InitialPaDTask(CountDownLatch latch, int initialTaskNumber, Board board, Tile[] tiles, int tileIndex, SolutionHandlerFactory shf) {
        super(board, tiles, tileIndex, shf);
        this.initialTaskNumber = initialTaskNumber;
        this.latch = latch;
    }

    @Override
    public void compute() {
        super.compute();
        int p = progress.incrementAndGet();
        System.out.println("Progress: " + p + "/" + initialTaskNumber);
        latch.countDown();
    }
}
