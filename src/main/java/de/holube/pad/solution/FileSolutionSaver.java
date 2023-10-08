package de.holube.pad.solution;

import de.holube.pad.model.Board;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class FileSolutionSaver implements SolutionHandlerComponent, AutoCloseable {

    private final Writer output;
    private final Semaphore mutex = new Semaphore(1);

    public FileSolutionSaver() {
        this("Solutions_" + new Date().toString().replace(":", "-"));
    }

    public FileSolutionSaver(String filename) {
        try {
            output = new BufferedWriter(new FileWriter(filename));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Board board) {
        try {
            mutex.acquire();
            try {
                output.append(board.toString()).append(";");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
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
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

}
