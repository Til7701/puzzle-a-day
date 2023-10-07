package de.holube.pad.solution;

import de.holube.pad.model.Board;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class FileSolutionSaver implements SolutionHandlerComponent {

    private static final Writer output;
    private static final Semaphore mutex = new Semaphore(1);

    static {
        try {
            output = new BufferedWriter(new FileWriter("Solutions_" + new Date().toString().replace(":", "-")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void save(Board board) {
        try {
            mutex.acquire();
            try {
                output.append(board.toString()).append("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
    }

    @Override
    public void close() throws IOException {
        try {
            mutex.acquire();
            output.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
    }

}
