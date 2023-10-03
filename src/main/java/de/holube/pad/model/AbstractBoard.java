package de.holube.pad.model;

import de.holube.pad.util.SolutionStore;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode
public abstract class AbstractBoard implements Board {

    @Getter
    protected final byte[][] board;
    @Getter
    protected final List<PositionedTile> positionedTiles = new ArrayList<>();
    @Getter
    protected final SolutionStore solutionStore;

    public AbstractBoard(byte[][] board, List<PositionedTile> positionedTiles, byte maxKey) {
        this.board = board;
        this.positionedTiles.addAll(positionedTiles);
        this.solutionStore = new SolutionStore(maxKey);
    }

    public boolean isValid() {
        return Board.isValid(getBoard());
    }

    public Board addTile(PositionedTile positionedTile) {
        byte[][] newBoard = new byte[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                newBoard[i][j] = (byte) (board[i][j] + positionedTile.getCumulativeBoard()[i][j]);
                if (newBoard[i][j] >= 2) {
                    return null;
                }
            }
        }

        List<PositionedTile> newTiles = new ArrayList<>(positionedTiles.size() + 1);
        newTiles.addAll(positionedTiles);
        newTiles.add(positionedTile);

        return createNewBoard(newBoard, newTiles);
    }

    protected abstract Board createNewBoard(byte[][] newBoard, List<PositionedTile> newPositionedTiles);

    @Override
    public boolean isValidSolution() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    if (!solutionStore.add((byte) getBoardMeaning()[0][i][j], (byte) getBoardMeaning()[1][i][j])) {
                        solutionStore.reset();
                        return false;
                    }
                } else if (board[i][j] > 1) {
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
        byte[] solution = solutionStore.getValues();
        StringBuilder path = new StringBuilder();
        for (byte s : solution) {
            path.append(s).append("/");
        }
        return path.toString();
    }

    public int getFreeSpaces() {
        int result = 0;
        for (byte[] bytes : board) {
            for (byte v : bytes) {
                if (v == 0)
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
