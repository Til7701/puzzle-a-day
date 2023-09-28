package de.holube.pad.util.pool;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class CustomTask {

    @Setter(AccessLevel.PACKAGE)
    @Getter
    private CustomPool pool;

    public abstract void compute();

}
