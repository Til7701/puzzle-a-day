package de.holube.pad.model;

import de.holube.pad.util.SolutionStore;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;

@EqualsAndHashCode
public abstract class AbstractBoard implements Board {

    @Getter
    protected final int[] tileIndices;

    protected final PositionedTile[][] positionedTiles;
    @Getter
    protected final SolutionStore solutionStore;

    public AbstractBoard(int[] tileIndices, PositionedTile[][] positionedTiles, int maxKey) {
        this.tileIndices = tileIndices;
        this.positionedTiles = positionedTiles;
        this.solutionStore = new SolutionStore(maxKey);
    }

    public boolean isValid() {
        return Board.isValid(getBoard());
    }

    private int getValueOfIndex(int i, int j) {
        int tmp = getBoardLayout()[i][j];
        for (int k = 0; k < tileIndices.length; k++) {
            tmp += positionedTiles[k][tileIndices[k]].getCumulativeBoard()[i][j];
        }
        return tmp;
    }

    public Board addTile(PositionedTile positionedTile) {
        int[] newTileIndices = new int[tileIndices.length + 1];

        System.arraycopy(tileIndices, 0, newTileIndices, 0, tileIndices.length);
        newTileIndices[newTileIndices.length - 1] = positionedTile.getId();

        for (int i = 0; i < getBoardLayout().length; i++) {
            for (int j = 0; j < getBoardMeaning()[0].length; j++) {
                if (getValueOfIndex(i, j) >= 2) {
                    return null;
                }
            }
        }

        return createNewBoard(newTileIndices, positionedTiles);
    }

    protected abstract Board createNewBoard(int[] tileIndices, PositionedTile[][] newPositionedTiles);

    public int[][] getBoard() {
        final int[][] board = new int[getBoardLayout().length][getBoardMeaning()[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = getValueOfIndex(i, j);
            }
        }

        return board;
    }

    public PositionedTile[] getPositionedTiles() {
        PositionedTile[] result = new PositionedTile[tileIndices.length];
        for (byte i = 0; i < result.length; i++) {
            result[i] = positionedTiles[i][tileIndices[i]];
        }

        return result;
    }

    @Override
    public boolean isValidSolution() {
        for (int i = 0; i < getBoardLayout().length; i++) {
            for (int j = 0; j < getBoardLayout()[0].length; j++) {
                int tmp = getValueOfIndex(i, j);
                if (tmp == 0) {
                    if (!solutionStore.add(getBoardMeaning()[0][i][j], getBoardMeaning()[1][i][j])) {
                        solutionStore.reset();
                        return false;
                    }
                } else if (tmp > 1) {
                    solutionStore.reset();
                    return false;
                }
            }
        }

        if (solutionStore.isComplete()) {
            return true;
        }
        solutionStore.reset();
        return false;
    }

    public String getPath() {
        int[] solution = solutionStore.getValues();
        StringBuilder path = new StringBuilder();
        for (int s : solution) {
            path.append(s).append("/");
        }
        return path.toString();
    }

    public int getLayoutFreeSpaces() {
        int result = 0;
        for (int[] row : getBoardLayout()) {
            for (int cell : row) {
                if (cell == 0)
                    result++;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        //StringBuilder builder = new StringBuilder();

        SolutionStore solutionStore = getSolutionStore();
        return Arrays.toString(solutionStore.getValues());
        /* builder.append(Arrays.toString(solutionStore.getValues())).append("&");

        List<int[][]> tileArrays = getTileCumArrays();
        for (int[][] array : tileArrays) {
            builder.append(Arrays.deepToString(array)).append("#");
        }

        return builder.toString();*/
    }

}
