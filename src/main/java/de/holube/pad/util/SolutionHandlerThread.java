package de.holube.pad.util;

import de.holube.pad.Board;
import de.holube.pad.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SolutionHandlerThread extends Thread {

    private static final int IMAGE_SIZE_PER_CELL = 50;
    private static final String OUTPUT_FOLDER = "output";

    private final Buffer<Board> solutionBuffer;
    private final Stats stats = new Stats();
    private int fileName = 0;

    public SolutionHandlerThread(Buffer<Board> solutionBuffer) {
        this.solutionBuffer = solutionBuffer;

        File folder = new File(OUTPUT_FOLDER);
        folder.mkdir();
    }

    private static void print(int[][] array) {
        for (int[] ints : array) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                Board board = solutionBuffer.get();
                stats.addSolution(board);

                BufferedImage image = createImage(board);
                save(board, image);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private BufferedImage createImage(Board board) {
        int[][] array = board.getBoard();
        int height = IMAGE_SIZE_PER_CELL * array.length;
        int width = IMAGE_SIZE_PER_CELL * array[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        List<Tile> tileBoards = board.getTiles();
        List<int[][]> tileCumArrays = board.getTileCumArrays();

        for (int i = 0; i < tileBoards.size(); i++) {
            Tile tile = tileBoards.get(i);
            Color color = tile.getColor();
            int[][] tileCumArray = tileCumArrays.get(i);
            Graphics2D graphics2D = image.createGraphics();
            graphics2D.setColor(color);

            for (int j = 0; j < tileCumArray.length; j++) {
                for (int k = 0; k < tileCumArray[0].length; k++) {
                    if (tileCumArray[j][k] == 1) {
                        graphics2D.fillRect(
                                j * IMAGE_SIZE_PER_CELL, k * IMAGE_SIZE_PER_CELL,
                                IMAGE_SIZE_PER_CELL, IMAGE_SIZE_PER_CELL
                        );
                    }
                }
            }
        }

        return image;
    }

    private void save(Board board, BufferedImage image) {
        File monthFolder = new File(String.valueOf(board.getMonth()));
        monthFolder.mkdir();
        File dayFolder = new File(board.getMonth() + "\\" + board.getDay());
        dayFolder.mkdir();
        File file = new File(board.getMonth() + "\\" + board.getDay() + "\\" + fileName + ".png");
        fileName++;
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            System.out.println("Write error for " + file.getPath() + ": " + e.getMessage());
        }
    }

}
