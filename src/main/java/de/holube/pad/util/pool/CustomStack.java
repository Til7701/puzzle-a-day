package de.holube.pad.util.pool;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.Semaphore;

public class CustomStack {

    private final Deque<CustomTask> stack = new ArrayDeque<>();
    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore items = new Semaphore(0);

    public void push(CustomTask task) {
        try {
            mutex.acquire();
            addElement(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
    }

    public CustomTask pop() {
        try {
            items.acquire();
            mutex.acquire();
            return stack.pop();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } finally {
            mutex.release();
        }
        return null;
    }

    public void pushAll(Collection<CustomTask> tasks) {
        try {
            mutex.acquire();
            for (CustomTask task : tasks)
                addElement(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
    }

    private void addElement(CustomTask task) {
        stack.push(task);
        items.release();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

}
