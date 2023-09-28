package de.holube.pad.solution;

import de.holube.pad.stats.Stats;
import de.holube.pad.stats.YearStats;

public class YearSolutionHandler extends AbstractSolutionHandler {

    private static final Stats stats = new YearStats();

    @Override
    public Stats getStats() {
        return stats;
    }

}
