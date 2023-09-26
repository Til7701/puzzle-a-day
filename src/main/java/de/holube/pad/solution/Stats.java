package de.holube.pad.solution;

import de.holube.pad.model.Board;

public interface Stats {

    void addSolution(Board board);

    void calculateStats();

    void printStats();

}
