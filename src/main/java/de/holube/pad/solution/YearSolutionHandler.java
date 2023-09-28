package de.holube.pad.solution;

import de.holube.pad.model.Board;
import lombok.Getter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

public class YearSolutionHandler extends AbstractSolutionHandler {

    private static final Writer output;
    @Getter
    private static final Stats stats = new DefaultStats();

    static {
        try {
            output = new BufferedWriter(new FileWriter("Solutions_" + new Date()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public YearSolutionHandler() {
    }

    private static void print(int[][] array) {
        for (int[] ints : array) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    public static void close() throws IOException {
        output.close();
    }

    public void handleSolution(Board board) {
        stats.addSolution(board);

        try {
            output.append(board.toString()).append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //BufferedImage image = createImage(board);
        //save(board, image);
    }

}
