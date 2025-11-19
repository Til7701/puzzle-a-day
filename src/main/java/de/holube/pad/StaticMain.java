package de.holube.pad;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public final class StaticMain {

    private static final int[][][] DEFAULT_TILES = {
            {
                    {1, 0, 0},
                    {1, 1, 1},
                    {0, 0, 1},
            },
            {
                    {1, 0, 0, 0},
                    {1, 1, 1, 1},
            },
            {
                    {1, 0},
                    {1, 1},
                    {1, 0},
                    {1, 0},
            },
            {
                    {1, 1},
                    {1, 1},
                    {1, 1},
            },
            {
                    {1, 0},
                    {1, 1},
                    {0, 1},
                    {0, 1},
            },
            {
                    {1, 1},
                    {1, 1},
                    {1, 0},
            },
            {
                    {1, 1},
                    {1, 0},
                    {1, 1},
            }
    };

    private static final int[][] DEFAULT_BOARD_LAYOUT = {
            {0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1}
    };

    private static final int[][][] DEFAULT_BOARD_MEANING = {
            {
                    {0, 0, 0, 0, 0, 0, -1},
                    {0, 0, 0, 0, 0, 0, -1},
                    {1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, -1, -1, -1, -1}
            },
            {
                    {1, 2, 3, 4, 5, 6, -1},
                    {7, 8, 9, 10, 11, 12, -1},
                    {1, 2, 3, 4, 5, 6, 7},
                    {8, 9, 10, 11, 12, 13, 14},
                    {15, 16, 17, 18, 19, 20, 21},
                    {22, 23, 24, 25, 26, 27, 28},
                    {29, 30, 31, -1, -1, -1, -1}
            }
    };

    private static final int PARALLELISM = Runtime.getRuntime().availableProcessors() - 2;
    private static final List<Color> COLORS = List.of(
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.MAGENTA,
            Color.ORANGE,
            Color.CYAN,
            Color.PINK,
            Color.YELLOW
    );

    private static final int[][][] ORIGINAL_TILES = DEFAULT_TILES;
    private static final int[][] ORIGINAL_BOARD_LAYOUT = DEFAULT_BOARD_LAYOUT;
    private static final int[][][] ORIGINAL_BOARD_MEANING = DEFAULT_BOARD_MEANING;

    private static final List<Tile> TILES;
    private static final long EMPTY_BOARD_BITMASK;

    private static final Deque<Task> TASK_QUEUE = new ConcurrentLinkedDeque<>();

    static {
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < ORIGINAL_TILES.length; i++) {
            tiles.add(new Tile(
                    ORIGINAL_TILES[i],
                    i,
                    COLORS.get(i % COLORS.size())
            ));
        }
        TILES = Collections.unmodifiableList(tiles);

        EMPTY_BOARD_BITMASK = fromArray(ORIGINAL_BOARD_LAYOUT);
    }

    private StaticMain() {
    }

    static void main() {
        System.out.println("Original Tiles: " + Arrays.deepToString(ORIGINAL_TILES));
        System.out.println("Board Layout: " + Arrays.deepToString(ORIGINAL_BOARD_LAYOUT));
        System.out.println("Board Meaning: " + Arrays.deepToString(ORIGINAL_BOARD_MEANING));
        System.out.println("Tiles: " + TILES);
        System.out.println("Parallelism: " + PARALLELISM);
        solve();
    }

    private static void solve() {
        prepareTasks();

        List<TaskRunner> taskRunners = new ArrayList<>();
        for (int i = 0; i < PARALLELISM; i++) {
            taskRunners.add(new TaskRunner(
                    i
            ));
        }
        List<Thread> threads = new ArrayList<>();
        for (TaskRunner taskRunner : taskRunners) {
            Thread thread = Thread.ofPlatform()
                    .name("TaskRunner-" + taskRunner.index())
                    .unstarted(taskRunner);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void prepareTasks() {
        Tile firstTile = TILES.getFirst();
        for (int i = 0; i < firstTile.allPositions().size(); i++) {
            int[] usedPositionedTileIds = new int[StaticMain.TILES.size()];
            usedPositionedTileIds[0] = i;
            TASK_QUEUE.add(new Task(
                    usedPositionedTileIds,
                    1
            ));
        }
        System.out.println("Prepared " + TASK_QUEUE.size() + " tasks.");
    }

    private record TaskRunner(
            int index,
            int[] usedPositionedTileIds
    ) implements Runnable {
        TaskRunner(int index) {
            this(index, new int[StaticMain.TILES.size()]);
        }

        @Override
        public void run() {
            Task task;
            while ((task = TASK_QUEUE.poll()) != null && !Thread.currentThread().isInterrupted()) {
                calculate(task);
                System.out.println("TaskRunner " + index + " processed a task. Remaining tasks: " + TASK_QUEUE.size());
            }
        }

        private void calculate(Task task) {
            System.arraycopy(task.usedPositionedTileIds(), 0, usedPositionedTileIds, 0, usedPositionedTileIds.length);
            int tileIndex = task.startTileIndex();
            long boardBitmask = constructBoardBitmask(usedPositionedTileIds, tileIndex - 1);

            calculateRecursive(tileIndex, boardBitmask);
        }

        private void calculateRecursive(int tileIndex, long boardBitmask) {
            if (tileIndex >= StaticMain.TILES.size()) {
                submitSolution(usedPositionedTileIds);
                return;
            }

            Tile tile = StaticMain.TILES.get(tileIndex);
            for (StaticMain.PositionedTile positionedTile : tile.allPositions()) {
                if ((boardBitmask & positionedTile.bitmask()) == 0) {
                    long newBoardBitmask = boardBitmask | positionedTile.bitmask();
                    usedPositionedTileIds[tileIndex] = positionedTile.id();
                    calculateRecursive(tileIndex + 1, newBoardBitmask);
                    usedPositionedTileIds[tileIndex] = 0;
                }
            }
        }

        private static long constructBoardBitmask(int[] usedPositionedTileIds, int upToIndex) {
            long bitmask = EMPTY_BOARD_BITMASK;
            for (int i = 0; i <= upToIndex; i++) {
                StaticMain.PositionedTile positionedTile = StaticMain.TILES.get(i).allPositions().get(usedPositionedTileIds[i]);
                bitmask = bitmask | positionedTile.bitmask();
            }
            return bitmask;
        }
    }

    private static int[][] rotate90Clockwise(int[][] origin) {
        int[][] result = new int[origin[0].length][origin.length];

        for (int i = 0; i < origin.length; i++) {
            for (int j = 0; j < origin[i].length; j++) {
                int newJ = origin[i].length - j - 1;
                result[newJ][i] = origin[i][j];
            }
        }

        return result;
    }

    private static int[][] flip(int[][] origin) {
        int[][] result = new int[origin[0].length][origin.length];

        for (int i = 0; i < origin.length; i++) {
            for (int j = 0; j < origin[i].length; j++) {
                result[j][i] = origin[i][j];
            }
        }

        return result;
    }

    private static List<int[][]> getAllRotations(int[][] base) {
        Set<int[][]> results = new TreeSet<>((a1, a2) -> {
            if (a1.length > a2.length)
                return 1;
            if (a1.length < a2.length)
                return -1;
            if (a1[0].length > a2[0].length)
                return 1;
            if (a1[0].length < a2[0].length)
                return -1;

            for (int i = 0; i < a1.length; i++) {
                for (int j = 0; j < a1[i].length; j++) {
                    if (a1[i][j] > a2[i][j])
                        return 1;
                    if (a1[i][j] < a2[i][j])
                        return -1;
                }
            }

            return 0;
        });

        results.add(base);
        int[][] tmp = rotate90Clockwise(base);
        results.add(tmp);
        tmp = rotate90Clockwise(tmp);
        results.add(tmp);
        tmp = rotate90Clockwise(tmp);
        results.add(tmp);
        tmp = flip(base);
        results.add(tmp);
        tmp = rotate90Clockwise(tmp);
        results.add(tmp);
        tmp = rotate90Clockwise(tmp);
        results.add(tmp);
        tmp = rotate90Clockwise(tmp);
        results.add(tmp);
        return results.stream().toList();
    }

    public static long fromArray(int[][] array) {
        long mask = 0L;
        for (int[] row : array) {
            for (int cell : row) {
                mask = mask << 1;
                mask = mask | cell;
            }
        }
        return mask;
    }

    private record Tile(
            int[][] base,
            int tileNumber,
            Color color,
            List<PositionedTile> allPositions
    ) {

        private Tile(int[][] base, int tileNumber, Color color) {
            System.out.println("Creating Tile: " + tileNumber);
            print(base);
            List<int[][]> baseRotated = getAllRotations(base);
            System.out.println("Number of Rotations: " + baseRotated.size());
            this(base, tileNumber, color, getAllPositions(baseRotated, tileNumber));
            System.out.println("Number of Boards: " + allPositions.size());
        }

        private static void removeBoard(int[][] newBoard, int[][] boardArray) {
            for (int i = 0; i < newBoard.length; i++) {
                for (int j = 0; j < newBoard[i].length; j++) {
                    newBoard[i][j] -= boardArray[i][j];
                }
            }
        }

        private static int[][] place(int[][] tile, int[][] boardArray, int i, int j) {
            int[][] copy = new int[boardArray.length][boardArray[0].length];

            for (int m = 0; m < boardArray.length; m++) {
                System.arraycopy(boardArray[m], 0, copy[m], 0, boardArray[m].length);
            }

            for (int k = 0; k < tile.length; k++) {
                for (int l = 0; l < tile[k].length; l++) {
                    copy[i + k][j + l] += tile[k][l];
                }
            }
            return copy;
        }

        private static void print(int[][] array) {
            for (int[] row : array) {
                for (int cell : row) {
                    System.out.print(cell + " ");
                }
                System.out.println();
            }
        }

        private static List<StaticMain.PositionedTile> getAllPositions(List<int[][]> baseRotated, int tileNumber) {
            Set<StaticMain.PositionedTile> results = new HashSet<>();

            for (int[][] tile : baseRotated) {
                results.addAll(getAllPositionsForTile(tile, tileNumber));
            }
            List<StaticMain.PositionedTile> tiles = new ArrayList<>(results.stream().toList());
            tiles.sort(Comparator.comparingInt(StaticMain.PositionedTile::id));
            return tiles;
        }

        private static List<StaticMain.PositionedTile> getAllPositionsForTile(int[][] tile, int tileNumber) {
            int positionedTileCounter = 0;
            Set<StaticMain.PositionedTile> results = new HashSet<>();
            int[][] boardArray = ORIGINAL_BOARD_LAYOUT;

            for (int i = 0; i < boardArray.length - tile.length + 1; i++) {
                for (int j = 0; j < boardArray[i].length - tile[0].length + 1; j++) {
                    int[][] newBoard = place(tile, boardArray, i, j);
                    if (isValid(newBoard)) {
                        removeBoard(newBoard, boardArray);
                        long boardBitmask = fromArray(newBoard);
                        results.add(new StaticMain.PositionedTile(boardBitmask, positionedTileCounter++, tileNumber));
                    }
                }
            }

            return results.stream().toList();
        }

        private static boolean isValid(int[][] board) {
            for (int[] row : board) {
                for (int cell : row) {
                    if (cell > 1)
                        return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return "Tile{" +
                    "base=" + Arrays.toString(base) +
                    ", tileNumber=" + tileNumber +
                    ", color=" + color +
                    ", allPositions=" + allPositions +
                    '}';
        }
    }


    private record PositionedTile(
            long bitmask,
            int id,
            int tileNumber
    ) {
    }

    private record Task(
            int[] usedPositionedTileIds,
            int startTileIndex
    ) {
        @Override
        public String toString() {
            return "Task{" +
                    "usedPositionedTileIds=" + Arrays.toString(usedPositionedTileIds) +
                    ", startTileIndex=" + startTileIndex +
                    '}';
        }
    }

    private static void submitSolution(int[] usedPositionedTileIds) {
        System.out.println("Found potential solution: " + Arrays.toString(usedPositionedTileIds));
    }

}
