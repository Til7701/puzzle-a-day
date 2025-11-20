package de.holube.pad;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

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
            },
            {
                    {1, 0, 0},
                    {1, 0, 0},
                    {1, 1, 1},
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
    //private static final int PARALLELISM = 1;
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
    private static final int BOARD_CELL_COUNT = Arrays.stream(ORIGINAL_BOARD_LAYOUT).mapToInt(row -> row.length).sum();

    private static final List<Tile> TILES;
    private static final int TILE_COUNT;
    private static final long EMPTY_BOARD_BITMASK;
    private static final long[] BOARD_MEANING_BITMASKS;

    static {
        int[][] meaningArray = ORIGINAL_BOARD_MEANING[0];
        int max = max(meaningArray);
        BOARD_MEANING_BITMASKS = new long[max + 1];
        for (int j = 0; j < meaningArray.length; j++) {
            for (int k = 0; k < meaningArray[j].length; k++) {
                int cell = meaningArray[j][k];
                if (cell >= 0) {
                    BOARD_MEANING_BITMASKS[cell] = BOARD_MEANING_BITMASKS[cell] | (1L << (meaningArray.length * meaningArray[j].length - (j * meaningArray[j].length + k) - 1));
                }
            }
        }
    }

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
        TILE_COUNT = TILES.size();

        EMPTY_BOARD_BITMASK = fromArray(ORIGINAL_BOARD_LAYOUT);
    }

    private StaticMain() {
    }

    static void main() {
        test();

        System.out.println("Original Tiles: ");
        for (int i = 0; i < ORIGINAL_TILES.length; i++) {
            System.out.println("Tile " + i + ": ");
            print(ORIGINAL_TILES[i]);
        }
        System.out.println("Board Layout: ");
        print(ORIGINAL_BOARD_LAYOUT);
        System.out.println("Board Meaning: ");
        print(ORIGINAL_BOARD_MEANING[0]);
        System.out.println("Board Meaning Values: ");
        print(ORIGINAL_BOARD_MEANING[1]);
        System.out.println("Tiles: ");
        for (Tile tile : TILES) {
            System.out.println(tile);
        }
        System.out.println("Parallelism: " + PARALLELISM);
        System.out.println("Empty Board Bitmask: " + longToBinaryString(EMPTY_BOARD_BITMASK));
        System.out.println("Board Meaning Bitmasks: ");
        for (long bitmask : BOARD_MEANING_BITMASKS) {
            System.out.println(longToBinaryString(bitmask));
        }
        long startTime = System.currentTimeMillis();
        solve();
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
        printSolutionSummary();
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
            usedPositionedTileIds[0] = firstTile.allPositions.get(i).id();
            TASK_QUEUE.add(new Task(
                    usedPositionedTileIds,
                    1
            ));
        }
        System.out.println("Prepared " + TASK_QUEUE.size() + " tasks.");
    }

    private record TaskRunner(
            int index
    ) implements Runnable {
        @Override
        public void run() {
            Task task;
            while ((task = TASK_QUEUE.poll()) != null && !Thread.currentThread().isInterrupted()) {
                calculate(task);
//                System.out.println(Thread.currentThread().getName() + " processed a task. Remaining tasks: " + TASK_QUEUE.size());
            }
        }

        private void calculate(Task task) {
            int[] usedPositionedTileIds = new int[StaticMain.TILES.size()];
            System.arraycopy(task.usedPositionedTileIds(), 0, usedPositionedTileIds, 0, usedPositionedTileIds.length);
            int tileIndex = task.startTileIndex();
            long boardBitmask = constructBoardBitmask(usedPositionedTileIds, tileIndex - 1);

            calculateRecursive(tileIndex, boardBitmask, usedPositionedTileIds);
        }

        private void calculateRecursive(int tileIndex, long boardBitmask, int[] usedPositionedTileIds) {
            if (tileIndex == TILE_COUNT) {
                submitSolution(usedPositionedTileIds, boardBitmask);
                return;
            }

            Tile tile = StaticMain.TILES.get(tileIndex);
            tileLoop:
            for (StaticMain.PositionedTile positionedTile : tile.allPositionsArray) {
                long positionedTileBitmask = positionedTile.bitmask();
                if ((boardBitmask & positionedTileBitmask) == 0) {
                    long newBoardBitmask = boardBitmask | positionedTileBitmask;
                    for (long meaningBitmask : BOARD_MEANING_BITMASKS) {
                        long meaningBitmaskEntry = (newBoardBitmask & meaningBitmask) ^ meaningBitmask;
                        int oneCount = Long.bitCount(meaningBitmaskEntry);
                        if (oneCount == 0) {
                            continue tileLoop;
                        }
                    }
                    usedPositionedTileIds[tileIndex] = positionedTile.id();

//                    System.out.println(longToBinaryString(positionedTileBitmask));
//                    System.out.println(longToBinaryString(boardBitmask));
//                    System.out.println(longToBinaryString(newBoardBitmask));
//                    generateImageForBoard(usedPositionedTileIds, tileIndex + 1);

                    calculateRecursive(tileIndex + 1, newBoardBitmask, usedPositionedTileIds);
                }
            }
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
        long mask = 0;
        for (int[] row : array) {
            for (int cell : row) {
                mask = mask << 1;
                mask = mask | cell;
            }
        }
        return mask;
    }

    public static int[][] toArray(long bitmask, int rows, int columns) {
        final long firstBit = 1L << 63;
        final int[][] result = new int[rows][columns];
        final int relevantLength = rows * columns;

        bitmask = bitmask << (64 - relevantLength);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = (int) ((bitmask & firstBit) >>> 63);
                bitmask = bitmask << 1;
            }
        }

        return result;
    }

    private static long constructBoardBitmask(int[] usedPositionedTileIds, int upToIndex) {
        long bitmask = EMPTY_BOARD_BITMASK;
        for (int i = 0; i <= upToIndex; i++) {
            StaticMain.PositionedTile positionedTile = StaticMain.TILES.get(i).allPositions().get(usedPositionedTileIds[i]);
            bitmask = bitmask | positionedTile.bitmask();
        }
        return bitmask;
    }

    private static int max(int[][] array) {
        int max = Integer.MIN_VALUE;
        for (int[] row : array) {
            for (int cell : row) {
                if (cell > max) {
                    max = cell;
                }
            }
        }
        return max;
    }

    private static void print(int[][] array) {
        for (int[] row : array) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    private static String arrayToString(int[][] array) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : array) {
            for (int cell : row) {
                sb.append(cell).append(" ");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private record Tile(
            int[][] base,
            int tileNumber,
            Color color,
            List<PositionedTile> allPositions,
            PositionedTile[] allPositionsArray
    ) {

        private Tile(int[][] base, int tileNumber, Color color) {
            System.out.println("Creating Tile: " + tileNumber);
            print(base);
            List<int[][]> baseRotated = getAllRotations(base);
            System.out.println("Number of Rotations: " + baseRotated.size());
            List<PositionedTile> ap = getAllPositions(baseRotated, tileNumber);
            this(base, tileNumber, color, ap, ap.toArray(new PositionedTile[0]));
            System.out.println("Number of Boards: " + allPositions.size());

            // test if all positioned tiles are in the correct order with their ids
            for (int i = 0; i < allPositions.size(); i++) {
                if (allPositions.get(i).id() != i) {
                    throw new IllegalStateException("PositionedTile id mismatch: expected " + i + " but got " + allPositions.get(i).id());
                }
            }
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

        private static List<StaticMain.PositionedTile> getAllPositions(List<int[][]> baseRotated, int tileNumber) {
            List<Long> results = new ArrayList<>();

            for (int[][] tile : baseRotated) {
                results.addAll(getAllPositionsForTile(tile));
            }
            List<StaticMain.PositionedTile> tiles = new ArrayList<>(results.stream()
                    .distinct()
                    .map(bitmask -> new StaticMain.PositionedTile(
                            bitmask,
                            results.indexOf(bitmask),
                            tileNumber
                    ))
                    .toList());
            tiles.sort(Comparator.comparingInt(StaticMain.PositionedTile::id));
            return Collections.unmodifiableList(tiles);
        }

        private static List<Long> getAllPositionsForTile(int[][] tile) {
            List<Long> results = new ArrayList<>();
            int[][] boardArray = ORIGINAL_BOARD_LAYOUT;

            for (int i = 0; i < boardArray.length - tile.length + 1; i++) {
                for (int j = 0; j < boardArray[i].length - tile[0].length + 1; j++) {
                    int[][] newBoard = place(tile, boardArray, i, j);
                    if (isValid(newBoard)) {
                        removeBoard(newBoard, boardArray);
                        long boardBitmask = fromArray(newBoard);
                        results.add(boardBitmask);
                    }
                }
            }

            return List.copyOf(results);
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
                    "base=" + System.lineSeparator() + arrayToString(base) +
                    ", tileNumber=" + tileNumber +
                    ", color=" + color +
                    ", allPositions=" + allPositions.stream()
                    .map(StaticMain.PositionedTile::toString)
                    .map(s -> System.lineSeparator() + s)
                    .reduce("", String::concat)
                    + '}';
        }
    }


    private record PositionedTile(
            long bitmask,
            int id,
            int tileNumber
    ) {
        @Override
        public String toString() {
            return "PositionedTile{" +
                    "bitmask=" + longToBinaryString(bitmask) +
                    ", id=" + id +
                    ", tileNumber=" + tileNumber +
                    '}';
        }
    }

    private record Task(
            int[] usedPositionedTileIds,
            int startTileIndex
    ) {
        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Task task = (Task) o;
            return startTileIndex == task.startTileIndex && Objects.deepEquals(usedPositionedTileIds, task.usedPositionedTileIds);
        }

        @Override
        public int hashCode() {
            return Objects.hash(Arrays.hashCode(usedPositionedTileIds), startTileIndex);
        }

        @Override
        public String toString() {
            return "Task{" +
                    "usedPositionedTileIds=" + Arrays.toString(usedPositionedTileIds) +
                    ", startTileIndex=" + startTileIndex +
                    '}';
        }
    }

    private static void submitSolution(int[] usedPositionedTileIds, long finalBoardBitmask) {
        for (long meaningBitmask : BOARD_MEANING_BITMASKS) {
            long meaningBitmaskEntry = (finalBoardBitmask & meaningBitmask) ^ meaningBitmask;
            int oneCount = Long.bitCount(meaningBitmaskEntry);
            if (oneCount != 1) {
                return;
            }
        }
        int[][] solutionBoardArray = toArray(finalBoardBitmask, ORIGINAL_BOARD_LAYOUT.length, ORIGINAL_BOARD_LAYOUT[0].length);
        int[] solutionMeanings = new int[BOARD_MEANING_BITMASKS.length];
        for (int i = 0; i < solutionBoardArray.length; i++) {
            for (int j = 0; j < solutionBoardArray[i].length; j++) {
                if (solutionBoardArray[i][j] == 0) {
                    int meaningIndex = ORIGINAL_BOARD_MEANING[0][i][j];
                    int meaningValue = ORIGINAL_BOARD_MEANING[1][i][j];
                    solutionMeanings[meaningIndex] = meaningValue;
                }
            }
        }
        SOLUTIONS.add(new Solution(
                Arrays.copyOf(usedPositionedTileIds, usedPositionedTileIds.length),
                solutionMeanings
        ));
    }

    private record Solution(
            int[] usedPositionedTileIds,
            int[] meaningValues
    ) {
        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Solution solution = (Solution) o;
            return Objects.deepEquals(meaningValues, solution.meaningValues) && Objects.deepEquals(usedPositionedTileIds, solution.usedPositionedTileIds);
        }

        @Override
        public int hashCode() {
            return Objects.hash(Arrays.hashCode(usedPositionedTileIds), Arrays.hashCode(meaningValues));
        }

        @Override
        public String toString() {
            return "Solution{" +
                    "usedPositionedTileIds=" + Arrays.toString(usedPositionedTileIds) +
                    ", meaningValues=" + Arrays.toString(meaningValues) +
                    '}';
        }
    }

    private static final Deque<Solution> SOLUTIONS = new ConcurrentLinkedDeque<>();

    private static void printSolutionSummary() {
        System.out.println("Total Solutions Found: " + SOLUTIONS.size());
        Map<int[], Integer> meaningsCount = new TreeMap<>((o1, o2) -> {
            for (int i = 0; i < o1.length; i++) {
                int compared = Integer.compare(o1[i], o2[i]);
                if (compared != 0) {
                    return compared;
                }
            }
            return 0;
        });
        List<int[]> allPossibleMeanings = constructAllPossibleMeanings();
        for (int[] meaning : allPossibleMeanings) {
            meaningsCount.put(meaning, 0);
        }
        for (Solution solution : SOLUTIONS) {
            int[] key = solution.meaningValues();
            Integer count = meaningsCount.get(key);
            if (count == null) {
                throw new IllegalStateException("Unknown meaning found in solution: " + Arrays.toString(key));
            }
            meaningsCount.put(key, count + 1);
        }
        int min = 0;
        int max = 0;
        double sum = 0;
        for (Map.Entry<int[], Integer> entry : meaningsCount.entrySet()) {
            int count = entry.getValue();
            if (count > max) {
                max = count;
            }
            if (count < min || min == 0) {
                min = count;
            }
            sum += count;
        }
        double average = sum / meaningsCount.size();
        System.out.println("Meanings Summary:");
        System.out.println("Total Different Meanings: " + meaningsCount.size());
        System.out.println("Min Solutions per Meaning: " + min);
        System.out.println("Max Solutions per Meaning: " + max);
        System.out.println("Average Solutions per Meaning: " + average);
    }

    private static List<int[]> constructAllPossibleMeanings() {
        List<int[]> results = constructAllPossibleMeaningsForIndexRecursive(0);

        return results;
    }

    private static List<int[]> constructAllPossibleMeaningsForIndexRecursive(int meaningIndex) {
        List<int[]> results = new ArrayList<>();
        int maxMeaningValue = 0;
        int[][] ints = ORIGINAL_BOARD_MEANING[1];
        for (int j = 0; j < ints.length; j++) {
            int[] row = ints[j];
            for (int i = 0; i < row.length; i++) {
                int cell = row[i];
                if (ORIGINAL_BOARD_MEANING[0][j][i] == meaningIndex && cell > maxMeaningValue) {
                    maxMeaningValue = cell;
                }
            }
        }

        for (int meaningValue = 1; meaningValue <= maxMeaningValue; meaningValue++) {
            if (meaningIndex == BOARD_MEANING_BITMASKS.length - 1) {
                int[] meaningArray = new int[BOARD_MEANING_BITMASKS.length];
                meaningArray[meaningIndex] = meaningValue;
                results.add(meaningArray);
            } else {
                List<int[]> subResults = constructAllPossibleMeaningsForIndexRecursive(meaningIndex + 1);
                for (int[] subResult : subResults) {
                    int[] meaningArray = new int[BOARD_MEANING_BITMASKS.length];
                    meaningArray[meaningIndex] = meaningValue;
                    System.arraycopy(subResult, meaningIndex + 1, meaningArray, meaningIndex + 1, subResult.length - (meaningIndex + 1));
                    results.add(meaningArray);
                }
            }
        }

        return results;
    }

    public static void test() {
        testLongArrayConversion(new int[][]{
                {1, 0, 0},
                {0, 0, 1}
        });
        testLongArrayConversion(new int[][]{
                {0, 0, 1, 0},
                {1, 0, 0, 0},
                {0, 0, 1, 0}
        });
        testLongArrayConversion(new int[][]{
                {1, 1, 1, 1},
                {0, 0, 0, 1}
        });
    }

    private static void testLongArrayConversion(int[][] array) {
        System.out.println("Testing long-array conversion for array:");
        print(array);

        long bitmask = fromArray(array);
        System.out.println("Converted to bitmask: " + longToBinaryString(bitmask));
        int[][] newArray = toArray(bitmask, array.length, array[0].length);
        System.out.println("Converted back to array:");
        print(newArray);

        if (!Arrays.deepEquals(newArray, array)) {
            throw new IllegalStateException();
        }
    }

    private static final String OUTPUT_FOLDER = "output";
    private static final int IMAGE_SIZE_PER_CELL = 50;
    private static final AtomicInteger fileName = new AtomicInteger(0);

    private static void generateImageForBoard(int[] usedPositionedTileIds) {
        int[][] array = toArray(constructBoardBitmask(usedPositionedTileIds, usedPositionedTileIds.length - 1), ORIGINAL_BOARD_LAYOUT.length, ORIGINAL_BOARD_LAYOUT[0].length);
        int height = IMAGE_SIZE_PER_CELL * array.length;
        int width = IMAGE_SIZE_PER_CELL * array[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();

        List<PositionedTile> positionedTiles = new ArrayList<>();
        for (int i = 0; i < usedPositionedTileIds.length; i++) {
            PositionedTile positionedTile = StaticMain.TILES.get(i).allPositions().get(usedPositionedTileIds[i]);
            positionedTiles.add(positionedTile);
        }

        for (int i = 0; i < positionedTiles.size(); i++) {
            PositionedTile tile = positionedTiles.get(i);
            Color color = StaticMain.TILES.get(i).color();
            int[][] tileCumArray = toArray(tile.bitmask(), ORIGINAL_BOARD_LAYOUT.length, ORIGINAL_BOARD_LAYOUT[0].length);
            graphics2D.setColor(color);

            for (int j = 0; j < tileCumArray.length; j++) {
                for (int k = 0; k < tileCumArray[0].length; k++) {
                    if (tileCumArray[j][k] == 1) {
                        graphics2D.fillRect(
                                k * IMAGE_SIZE_PER_CELL, j * IMAGE_SIZE_PER_CELL,
                                IMAGE_SIZE_PER_CELL, IMAGE_SIZE_PER_CELL
                        );
                    }
                }
            }
        }

        saveImage(image);
    }

    private static void generateImageForBoard(int[] usedPositionedTileIds, int upToIndex) {
        int[][] array = toArray(constructBoardBitmask(usedPositionedTileIds, usedPositionedTileIds.length - 1), ORIGINAL_BOARD_LAYOUT.length, ORIGINAL_BOARD_LAYOUT[0].length);
        int height = IMAGE_SIZE_PER_CELL * array.length;
        int width = IMAGE_SIZE_PER_CELL * array[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();

        List<PositionedTile> positionedTiles = new ArrayList<>();
        for (int i = 0; i < upToIndex; i++) {
            PositionedTile positionedTile = StaticMain.TILES.get(i).allPositions().get(usedPositionedTileIds[i]);
            positionedTiles.add(positionedTile);
        }

        for (int i = 0; i < positionedTiles.size(); i++) {
            PositionedTile tile = positionedTiles.get(i);
            Color color = StaticMain.TILES.get(i).color();
            int[][] tileCumArray = toArray(tile.bitmask(), ORIGINAL_BOARD_LAYOUT.length, ORIGINAL_BOARD_LAYOUT[0].length);
            graphics2D.setColor(color);

            for (int j = 0; j < tileCumArray.length; j++) {
                for (int k = 0; k < tileCumArray[0].length; k++) {
                    if (tileCumArray[j][k] == 1) {
                        graphics2D.fillRect(
                                k * IMAGE_SIZE_PER_CELL, j * IMAGE_SIZE_PER_CELL,
                                IMAGE_SIZE_PER_CELL, IMAGE_SIZE_PER_CELL
                        );
                    }
                }
            }
        }

        saveImage(image);
    }

    private static void saveImage(BufferedImage image) {
        String separator = File.separator;
        final int name = fileName.incrementAndGet();
        File file = new File(OUTPUT_FOLDER + separator + name + ".png");
        if (!file.getParentFile().exists() && !file.mkdirs()) {
            System.out.println("Could not create output folder");
        } else {
            try {
                ImageIO.write(image, "png", file);
            } catch (IOException e) {
                System.out.println("Write error for " + file.getPath() + ": " + e.getMessage());
            }
        }
    }

    private static String longToBinaryString(long value) {
        StringBuilder sb = new StringBuilder(Long.toBinaryString(value));
        while (sb.length() < BOARD_CELL_COUNT) {
            sb.insert(0, '0');
        }
        String string = sb.toString();

        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            formatted.append(string.charAt(i));
            if ((i + 1) % ORIGINAL_BOARD_LAYOUT[0].length == 0 && i != string.length() - 1) {
                formatted.append('_');
            }
        }
        return formatted.toString();
    }

}
