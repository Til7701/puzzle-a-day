package de.holube.pad.solution;

import de.holube.pad.stats.DefaultStats;
import de.holube.pad.stats.Stats;

public class DefaultSolutionHandler extends AbstractSolutionHandler {

    private static final Stats stats = new DefaultStats();

    @Override
    public Stats getStats() {
        return stats;
    }

}
