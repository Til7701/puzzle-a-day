package de.holube.pad.solution;

import de.holube.pad.model.Board;

public interface SolutionHandler {

    void handleSolution(Board board);

    Stats getStats();

}
