package de.holube.pad.solution;

import de.holube.pad.model.Board;
import de.holube.pad.stats.DefaultStats;
import de.holube.pad.stats.Stats;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SolutionHandler {

    private final List<SolutionHandlerComponent> components = new ArrayList<>();

    private final Stats stats;

    public SolutionHandler(boolean saveToFile, boolean saveImages) {
        stats = new DefaultStats();

        if (saveToFile)
            components.add(new FileSolutionSaver());
        if (saveImages)
            components.add(new ImageSolutionSaver());
    }

    public void handleSolution(Board board) {
        for (SolutionHandlerComponent component : components) {
            component.save(board);
        }
        stats.addSolution(board);
    }

    public void close() throws IOException {
        for (SolutionHandlerComponent component : components) {
            component.close();
        }
    }

}
