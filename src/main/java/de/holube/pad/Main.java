package de.holube.pad;

import com.google.gson.Gson;
import de.holube.pad.model.*;
import de.holube.pad.solution.AbstractSolutionHandler;
import de.holube.pad.solution.DefaultSolutionHandlerFactory;
import de.holube.pad.solution.SolutionHandlerFactory;
import de.holube.pad.solution.YearSolutionHandlerFactory;
import de.holube.pad.stats.Stats;
import de.holube.pad.util.*;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, PositionedTileIdException, PositionedTileAmountException {
        String json = Files.readString(Path.of("config.json"));
        Gson gson = new Gson();
        Config config = gson.fromJson(json, Config.class);
        System.out.println("Config: \n" + json);

        Board board = switch (config.TARGET) {
            case DEFAULT -> new DefaultBoard();
            case YEAR -> new YearBoard();
        };
        final SolutionHandlerFactory shf = switch (config.TARGET) {
            case DEFAULT -> DefaultSolutionHandlerFactory.get();
            case YEAR -> YearSolutionHandlerFactory.get();
        };
        final Stats stats = shf.create().getStats();

        final List<Tile> tiles = new ArrayList<>();
        for (byte i = 0; i < config.ACTIVE_TILES.size(); i++) {
            tiles.add(new Tile(config.TILES.get(config.ACTIVE_TILES.get(i)), config.ACTIVE_TILES.get(i), i, RandomColor.getBright(), board));
        }

        final PositionedTile[][] positionedTiles = new PositionedTile[tiles.size()][];
        for (int i = 0; i < tiles.size(); i++) {
            PositionedTile[] positionedTilesFromTile = tiles.get(i).getAllPositions();
            positionedTiles[i] = positionedTilesFromTile;

            for (int j = 0; j < positionedTilesFromTile.length; j++) {
                if (positionedTilesFromTile[j].getId() != j)
                    throw new PositionedTileIdException();
            }
        }

        board = switch (config.TARGET) {
            case DEFAULT -> new DefaultBoard(positionedTiles);
            case YEAR -> new YearBoard(positionedTiles);
        };
        if (!PlausibilityCheck.check(board, tiles)) {
            System.out.println("Not Plausible!!");
            return;
        }

        BigInteger totalOptions = new BigInteger(String.valueOf(1));
        for (Tile tile : tiles) {
            totalOptions = totalOptions.multiply(BigInteger.valueOf(tile.getAllPositions().length));
        }
        System.out.println("Total number of possible boards: " + totalOptions);

        final int parallelism = config.PARALLELISM;
        Thread printingHook = new Thread(() -> {
            stats.calculateStats();
            stats.printStats();
            stats.save(json);
        });
        Runtime.getRuntime().addShutdownHook(printingHook);
        final long startTime = System.currentTimeMillis();
        final PuzzleADaySolver padSolver = new PuzzleADaySolver(board, positionedTiles, shf, parallelism);
        padSolver.solve();
        final long endTime = System.currentTimeMillis();
        final long time = endTime - startTime;
        System.out.println("done in: " + time + "ms");

        stats.calculateStats();
        stats.printStats();
        stats.save(json);

        AbstractSolutionHandler.getSaver().close();

        //Distance distance = new Distance(SolutionHandler.getStats().getResults());
        //distance.calculateDistances();

    }

}