package de.holube.pad.util.pool;

import java.util.Collection;

public class CustomPool {

    private final CustomThread[] threads;
    private final CustomStack stack = new CustomStack();


    public CustomPool(int parallelism) {
        threads = new CustomThread[parallelism];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new CustomThread(stack);
            threads[i].start();
        }
    }

    public void invokeAll(Collection<CustomTask> tasks) {
        for (CustomTask task : tasks) {
            task.setPool(this);
        }
        stack.pushAll(tasks);
    }

    public void invokeAndWait(CustomTask task) {
        task.setPool(this);
        stack.push(task);
        
    }

}
