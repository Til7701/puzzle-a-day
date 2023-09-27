package de.holube.pad.solution;

import de.holube.pad.model.Board;
import lombok.Getter;

public class YearSolutionHandler extends AbstractSolutionHandler {

    @Getter
    private static final Stats stats = new DefaultStats();

    private static void print(int[][] array) {
        for (int[] ints : array) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    public void handleSolution(Board board) {
        stats.addSolution(board);

        //BufferedImage image = createImage(board);
        //save(board, image);
    }

}
