package de.holube.pad.solution;

import de.holube.pad.model.Board;
import de.holube.pad.stats.Stats;

public interface SolutionHandler {

    void handleSolution(Board board);

    Stats getStats();

}
