package de.holube.pad;

import de.holube.pad.model.Board;
import de.holube.pad.model.Tile;
import de.holube.pad.model.YearBoard;
import de.holube.pad.solution.SolutionHandlerFactory;
import de.holube.pad.solution.Stats;
import de.holube.pad.solution.YearSolutionHandler;
import de.holube.pad.solution.YearSolutionHandlerFactory;
import de.holube.pad.util.ArrayProvider;
import de.holube.pad.util.PlausibilityCheck;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
       /* final Board board = new DefaultBoard();
        final SolutionHandlerFactory shf = DefaultSolutionHandlerFactory.get();
        final Stats stats = DefaultSolutionHandler.getStats();*/

        final Board board = new YearBoard();
        final SolutionHandlerFactory shf = YearSolutionHandlerFactory.get();
        final Stats stats = YearSolutionHandler.getStats();

        List<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile(ArrayProvider.TILE_S, "S", Color.RED, board));
        tiles.add(new Tile(ArrayProvider.TILE_l, "l", Color.BLUE, board));
        tiles.add(new Tile(ArrayProvider.TILE_L, "L", Color.YELLOW, board));
        tiles.add(new Tile(ArrayProvider.TILE_t, "t", Color.CYAN, board));
        tiles.add(new Tile(ArrayProvider.TILE_O, "O", Color.GREEN, board));
        tiles.add(new Tile(ArrayProvider.TILE_s, "s", Color.GRAY, board));
        tiles.add(new Tile(ArrayProvider.TILE_P, "P", Color.PINK, board));
        tiles.add(new Tile(ArrayProvider.TILE_C, "C", Color.ORANGE, board));

        tiles.add(new Tile(ArrayProvider.TILE_H, "H", Color.WHITE, board));
        //tiles.add(new Tile(ArrayProvider.TILE_I, "I", Color.LIGHT_GRAY, board));
        //tiles.add(new Tile(ArrayProvider.TILE_4, "4", Color.LIGHT_GRAY, board));
        tiles.add(new Tile(ArrayProvider.TILE_p, "p", Color.DARK_GRAY, board));
        tiles.add(new Tile(ArrayProvider.TILE_T, "T", Color.LIGHT_GRAY, board));
        tiles.add(new Tile(ArrayProvider.TILE_1, "1", Color.RED, board));

        if (!PlausibilityCheck.check(board, tiles)) {
            System.out.println("Not Plausible!!");
            return;
        }

        int totalOptions = 1;
        for (Tile tile : tiles) {
            totalOptions *= tile.getAllPositions().size();
        }
        System.out.println("Total number of possible boards: " + totalOptions);

        long startTime = System.currentTimeMillis();
        final PuzzleADaySolver padSolver = new PuzzleADaySolver(board, tiles.toArray(new Tile[0]), shf);
        padSolver.solve();
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("done in: " + time + "ms");

        stats.calculateStats();
        stats.printStats();

        //Distance distance = new Distance(SolutionHandler.getStats().getResults());
        //distance.calculateDistances();

    }

}