package de.holube.pad;

import com.google.gson.Gson;
import de.holube.pad.model.Board;
import de.holube.pad.model.DefaultBoard;
import de.holube.pad.model.Tile;
import de.holube.pad.model.YearBoard;
import de.holube.pad.solution.AbstractSolutionHandler;
import de.holube.pad.solution.DefaultSolutionHandlerFactory;
import de.holube.pad.solution.SolutionHandlerFactory;
import de.holube.pad.solution.YearSolutionHandlerFactory;
import de.holube.pad.stats.Stats;
import de.holube.pad.util.Config;
import de.holube.pad.util.PlausibilityCheck;
import de.holube.pad.util.RandomColor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String json = Files.readString(Path.of("config.json"));
        Gson gson = new Gson();
        Config config = gson.fromJson(json, Config.class);

        final Board board = switch (config.TARGET) {
            case DEFAULT -> new DefaultBoard();
            case YEAR -> new YearBoard();
        };
        final SolutionHandlerFactory shf = switch (config.TARGET) {
            case DEFAULT -> DefaultSolutionHandlerFactory.get();
            case YEAR -> YearSolutionHandlerFactory.get();
        };
        final Stats stats = shf.create().getStats();

        final List<Tile> tiles = new ArrayList<>();
        for (String key : config.ACTIVE_TILES) {
            tiles.add(new Tile(config.TILES.get(key), key, RandomColor.getBright(), board));
        }


        if (!PlausibilityCheck.check(board, tiles)) {
            System.out.println("Not Plausible!!");
            return;
        }

        long totalOptions = 1;
        for (Tile tile : tiles) {
            totalOptions *= tile.getAllPositions().size();
        }
        System.out.println("Total number of possible boards: " + totalOptions);

        // final int parallelism = (int) (Runtime.getRuntime().availableProcessors() * 0.7);
        final int parallelism = config.PARALLELISM;
        Thread printingHook = new Thread(() -> {
            stats.calculateStats();
            stats.printStats();
            stats.save();
        });
        Runtime.getRuntime().addShutdownHook(printingHook);
        final long startTime = System.currentTimeMillis();
        final PuzzleADaySolver padSolver = new PuzzleADaySolver(board, tiles.toArray(new Tile[0]), shf, parallelism);
        padSolver.solve();
        final long endTime = System.currentTimeMillis();
        final long time = endTime - startTime;
        System.out.println("done in: " + time + "ms");

        stats.calculateStats();
        stats.printStats();
        stats.save();

        AbstractSolutionHandler.getSaver().close();

        //Distance distance = new Distance(SolutionHandler.getStats().getResults());
        //distance.calculateDistances();

    }

}