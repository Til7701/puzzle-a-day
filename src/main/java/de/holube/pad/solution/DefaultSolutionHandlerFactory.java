package de.holube.pad.solution;

public class DefaultSolutionHandlerFactory implements SolutionHandlerFactory {

    private static DefaultSolutionHandlerFactory obj;

    private DefaultSolutionHandlerFactory() {

    }

    public static SolutionHandlerFactory get() {
        if (obj == null) {
            obj = new DefaultSolutionHandlerFactory();
        }
        return obj;
    }

    @Override
    public SolutionHandler create() {
        return new DefaultSolutionHandler();
    }

}
