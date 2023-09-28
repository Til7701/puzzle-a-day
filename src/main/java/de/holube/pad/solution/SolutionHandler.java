package de.holube.pad.solution;

import de.holube.pad.model.Board;
import de.holube.pad.stats.Stats;

import java.io.IOException;

public interface SolutionHandler {

    void handleSolution(Board board);

    Stats getStats();

    void close() throws IOException;

}
