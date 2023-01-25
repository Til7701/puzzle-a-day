package de.holube.pad.util;

public interface Buffer<E> {

    void put(E element) throws InterruptedException;

    E get() throws InterruptedException;

    boolean isEmpty();
    
}
