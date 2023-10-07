package de.holube.pad.solution;

import de.holube.pad.model.Board;

import java.io.IOException;

interface SolutionHandlerComponent {

    void save(Board board);

    void close() throws IOException;

}
