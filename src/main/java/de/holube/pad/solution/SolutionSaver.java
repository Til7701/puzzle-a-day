package de.holube.pad.solution;

import de.holube.pad.model.Board;

import java.io.IOException;

public interface SolutionSaver {

    void save(Board board);

    void close() throws IOException;

}
