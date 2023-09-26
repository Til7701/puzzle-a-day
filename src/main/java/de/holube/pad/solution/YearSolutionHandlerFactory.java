package de.holube.pad.solution;

public class YearSolutionHandlerFactory implements SolutionHandlerFactory {

    private static YearSolutionHandlerFactory obj;

    private YearSolutionHandlerFactory() {

    }

    public static SolutionHandlerFactory get() {
        if (obj == null) {
            obj = new YearSolutionHandlerFactory();
        }
        return obj;
    }

    @Override
    public SolutionHandler create() {
        return new YearSolutionHandler();
    }

}
