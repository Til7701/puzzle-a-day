package de.holube.pad.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class PositionedTile {

    @Getter
    private final Tile parent;

    @Getter
    private final byte[][] cumulativeBoard;

    PositionedTile(Tile parent, byte[][] cumulativeBoard) {
        this.parent = parent;
        this.cumulativeBoard = cumulativeBoard;
    }

}
