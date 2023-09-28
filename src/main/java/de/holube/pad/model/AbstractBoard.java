package de.holube.pad.model;

import de.holube.pad.util.SolutionStore;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode
public abstract class AbstractBoard implements Board {


    protected final byte[][] board;
    @Getter
    protected final List<Tile> tiles = new ArrayList<>();
    @Getter
    protected final List<byte[][]> tileCumArrays = new ArrayList<>();
    @Getter
    protected SolutionStore solutionStore;

    public AbstractBoard(byte[][] board, List<Tile> tiles, List<byte[][]> tileCumArrays, byte maxKey) {
        this.board = board;
        this.tiles.addAll(tiles);
        this.tileCumArrays.addAll(tileCumArrays);
        this.solutionStore = new SolutionStore(maxKey);
    }

    public boolean isValid() {
        return Board.isValid(getBoard());
    }

    public byte[][] getBoard() {
        byte[][] copy = new byte[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, board[i].length);
        }

        return copy;
    }

    public Board addTile(byte[][] tileCumBoard, Tile tile) {
        byte[][] newBoard = new byte[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                newBoard[i][j] = (byte) (board[i][j] + tileCumBoard[i][j]);
                if (newBoard[i][j] >= 2) {
                    return null;
                }
            }
        }

        List<Tile> newTiles = new ArrayList<>(tiles.size() + 1);
        newTiles.addAll(tiles);
        newTiles.add(tile);
        List<byte[][]> newTileCumArrays = new ArrayList<>(tileCumArrays.size() + 1);
        newTileCumArrays.addAll(tileCumArrays);
        newTileCumArrays.add(tileCumBoard);

        return createNewBoard(newBoard, newTiles, newTileCumArrays);
    }

    protected abstract Board createNewBoard(byte[][] newBoard, List<Tile> newTiles, List<byte[][]> newTileCumBoards);

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
