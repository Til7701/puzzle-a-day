package de.holube.pad.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class PositionedTile {

    @Getter
    private final Tile parent;

    @Getter
    private final int[][] cumulativeBoard;

    @Getter
    private final int id;
    @Getter
    private final int tileNumber;

    PositionedTile(Tile parent, int[][] cumulativeBoard, int id, int tileNumber) {
        this.parent = parent;
        this.cumulativeBoard = cumulativeBoard;
        this.id = id;
        this.tileNumber = tileNumber;
    }

}
