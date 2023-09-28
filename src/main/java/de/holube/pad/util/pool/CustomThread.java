package de.holube.pad.util.pool;

public class CustomThread extends Thread {

    private final CustomStack stack;

    public CustomThread(CustomStack stack) {
        this.stack = stack;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                stack.pop().compute();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

}
