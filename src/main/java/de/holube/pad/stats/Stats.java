package de.holube.pad.stats;

import de.holube.pad.model.Board;

public interface Stats {

    void addSolution(Board board);

    void calculateStats();

    void printStats();

    void save(String jsonConfig);

    int getMin();

    int getMax();

    double getAverage();

}
