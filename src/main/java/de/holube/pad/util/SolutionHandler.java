package de.holube.pad.util;

import de.holube.pad.Board;
import de.holube.pad.Tile;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SolutionHandler {

    private static final int IMAGE_SIZE_PER_CELL = 50;
    private static final String OUTPUT_FOLDER = "output";
    private static final AtomicInteger fileName = new AtomicInteger(0);
    @Getter
    private static final Stats stats = new Stats();

    public SolutionHandler() {
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

    public void handleSolution(Board board) {
        stats.addSolution(board);

        BufferedImage image = createImage(board);
        save(board, image);
    }

    private BufferedImage createImage(Board board) {
        int[][] array = board.getBoard();
        int height = IMAGE_SIZE_PER_CELL * array.length;
        int width = IMAGE_SIZE_PER_CELL * array[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();

        List<Tile> tileBoards = board.getTiles();
        List<int[][]> tileCumArrays = board.getTileCumArrays();

        for (int i = 0; i < tileBoards.size(); i++) {
            Tile tile = tileBoards.get(i);
            Color color = tile.getColor();
            int[][] tileCumArray = tileCumArrays.get(i);
            graphics2D.setColor(color);

            for (int j = 0; j < tileCumArray.length; j++) {
                for (int k = 0; k < tileCumArray[0].length; k++) {
                    if (tileCumArray[j][k] == 1) {
                        graphics2D.fillRect(
                                k * IMAGE_SIZE_PER_CELL, j * IMAGE_SIZE_PER_CELL,
                                IMAGE_SIZE_PER_CELL, IMAGE_SIZE_PER_CELL
                        );
                    }
                }
            }
        }

        return image;
    }

    private void save(Board board, BufferedImage image) {
        String separator = File.separator;
        final int name = fileName.incrementAndGet();
        File monthFolder = new File(OUTPUT_FOLDER + separator + board.getMonth());
        monthFolder.mkdir();
        File dayFolder = new File(OUTPUT_FOLDER + separator + board.getMonth() + separator + board.getDay());
        dayFolder.mkdir();
        File file = new File(OUTPUT_FOLDER + separator + board.getMonth() + separator + board.getDay() + separator + name + ".png");
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            System.out.println("Write error for " + file.getPath() + ": " + e.getMessage());
        }
    }

}
