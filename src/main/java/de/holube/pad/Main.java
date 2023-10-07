package de.holube.pad;

import com.google.gson.Gson;
import de.holube.pad.model.Board;
import de.holube.pad.model.PositionedTile;
import de.holube.pad.model.Tile;
import de.holube.pad.solution.SolutionHandler;
import de.holube.pad.stats.Stats;
import de.holube.pad.util.Config;
import de.holube.pad.util.PlausibilityCheck;
import de.holube.pad.util.PositionedTileIdException;
import de.holube.pad.util.RandomColor;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, PositionedTileIdException {
        final Config config = loadConfig();
        final int parallelism = config.getParallelism();

        Board board = new Board(config.getBoard().getLayout(), config.getBoard().getMeaning());

        final SolutionHandler solutionHandler = new SolutionHandler(config.isSaveSolutions(), config.isSaveImages());

        final List<Tile> tiles = createTiles(config, board);
        final PositionedTile[][] positionedTiles = createPositionedTiles(tiles);

        board = new Board(positionedTiles, config.getBoard().getLayout(), config.getBoard().getMeaning());

        if (!PlausibilityCheck.check(board, tiles)) {
            System.out.println("Not Plausible!!");
            return;
        }

        calculateTotalOptions(tiles);

        createShutdownHook(config, solutionHandler.getStats());

        final long startTime = System.currentTimeMillis();
        final PuzzleADaySolver padSolver = new PuzzleADaySolver(board, positionedTiles, solutionHandler, parallelism);
        padSolver.solve();
        final long endTime = System.currentTimeMillis();
        final long time = endTime - startTime;
        System.out.println("done in: " + time + "ms");

        solutionHandler.close();

        Stats stats = solutionHandler.getStats();
        stats.calculateStats();
        stats.printStats();
        stats.save(config.getJsonSource());
    }

    private static Config loadConfig() throws IOException {
        String json = Files.readString(Path.of("config.json"));
        System.out.println("Read Config: \n" + json);
        Gson gson = new Gson();
        Config config = gson.fromJson(json, Config.class);
        config.setJsonSource(json);
        return config;
    }

    private static List<Tile> createTiles(Config config, Board board) {
        final List<Tile> tiles = new ArrayList<>();
        for (byte i = 0; i < config.getTiles().size(); i++) {
            tiles.add(new Tile(config.getTiles().get(i), i, RandomColor.getBright(), board));
        }
        return tiles;
    }

    private static PositionedTile[][] createPositionedTiles(List<Tile> tiles) throws PositionedTileIdException {
        final PositionedTile[][] positionedTiles = new PositionedTile[tiles.size()][];
        for (int i = 0; i < tiles.size(); i++) {
            PositionedTile[] positionedTilesFromTile = tiles.get(i).getAllPositions();
            positionedTiles[i] = positionedTilesFromTile;

            for (int j = 0; j < positionedTilesFromTile.length; j++) {
                if (positionedTilesFromTile[j].getId() != j)
                    throw new PositionedTileIdException();
            }
        }
        return positionedTiles;
    }

    private static void calculateTotalOptions(List<Tile> tiles) {
        BigInteger totalOptions = new BigInteger(String.valueOf(1));
        for (Tile tile : tiles) {
            totalOptions = totalOptions.multiply(BigInteger.valueOf(tile.getAllPositions().length));
        }
        System.out.println("Total number of possible boards: " + totalOptions);
    }

    private static void createShutdownHook(Config config, Stats stats) {
        Thread hook = new Thread(() -> {
            stats.calculateStats();
            stats.printStats();
            stats.save(config.getJsonSource());
        });
        Runtime.getRuntime().addShutdownHook(hook);
    }

}