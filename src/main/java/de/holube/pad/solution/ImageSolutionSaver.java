package de.holube.pad.solution;

import de.holube.pad.model.Board;
import de.holube.pad.model.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageSolutionSaver implements SolutionSaver {

    protected static final String OUTPUT_FOLDER = "output";
    private static final int IMAGE_SIZE_PER_CELL = 50;
    private static final AtomicInteger fileName = new AtomicInteger(0);

    @Override
    public void save(Board board) {
        BufferedImage image = createImage(board);
        saveImage(board, image);
    }

    protected BufferedImage createImage(Board board) {
        byte[][] array = board.getBoard();
        int height = IMAGE_SIZE_PER_CELL * array.length;
        int width = IMAGE_SIZE_PER_CELL * array[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();

        java.util.List<Tile> tileBoards = board.getTiles();
        List<byte[][]> tileCumArrays = board.getTileCumArrays();

        for (int i = 0; i < tileBoards.size(); i++) {
            Tile tile = tileBoards.get(i);
            Color color = tile.getColor();
            byte[][] tileCumArray = tileCumArrays.get(i);
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

    protected void saveImage(Board board, BufferedImage image) {
        String separator = File.separator;
        final int name = fileName.incrementAndGet();
        File file = new File(OUTPUT_FOLDER + separator + board.getPath().replace("/", separator) + separator + name + ".png");
        if (!file.getParentFile().exists() && !file.mkdirs()) {
            System.out.println("Could not create output folder");
        } else {
            try {
                ImageIO.write(image, "png", file);
            } catch (IOException e) {
                System.out.println("Write error for " + file.getPath() + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void close() throws IOException {

    }

}
