package de.holube.pad;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public final class StaticLongArrayMain {

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

    private static final int[][][] YEAR_TILES = {
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
            },
            {
                    {1, 1, 0},
                    {0, 1, 1},
                    {0, 1, 1},
            },
            {
                    {1, 1, 1},
                    {0, 1, 0}
            },
            {
                    {1, 1, 1},
                    {1, 0, 0}
            },
            {
                    {1, 1, 0},
                    {0, 1, 1}
            }
    };

    private static final int[][] YEAR_BOARD_LAYOUT = {
            {1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1}
    };

    private static final int[][][] YEAR_BOARD_MEANING = {
            {
                    {-1, -1, 0, 0, 0, 0, 0, 0, -1, -1, -1},
                    {2, 2, 0, 0, 0, 0, 0, 0, -1, 3, 3},
                    {2, 2, 1, 1, 1, 1, 1, 1, 1, 3, 3},
                    {2, 2, 1, 1, 1, 1, 1, 1, 1, 3, 3},
                    {2, 2, 1, 1, 1, 1, 1, 1, 1, 3, 3},
                    {2, 2, 1, 1, 1, 1, 1, 1, 1, 3, 3},
                    {-1, -1, 1, 1, 1, -1, -1, -1, -1, -1, -1}
            },
            {
                    {-1, -1, 1, 2, 3, 4, 5, 6, -1, -1, -1},
                    {1, 2, 7, 8, 9, 10, 11, 12, -1, 1, 2},
                    {3, 4, 1, 2, 3, 4, 5, 6, 7, 3, 4},
                    {5, 6, 8, 9, 10, 11, 12, 13, 14, 5, 6},
                    {7, 8, 15, 16, 17, 18, 19, 20, 21, 7, 8},
                    {9, 10, 22, 23, 24, 25, 26, 27, 28, 9, 10},
                    {-1, -1, 29, 30, 31, -1, -1, -1, -1, -1, -1}
            }
    };

    private static final int[][][] YEAR4_TILES = {
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
            },
            {
                    {1, 1, 0},
                    {0, 1, 1},
                    {0, 1, 1},
            },
            {
                    {1, 1, 1},
                    {0, 1, 0}
            },
            {
                    {1, 1, 1},
                    {1, 0, 0}
            },
            {
                    {1, 1, 0},
                    {0, 1, 1}
            },
            {
                    {1, 1, 1, 0},
                    {0, 1, 1, 1},
            },
            {
                    {1, 0, 0},
                    {1, 1, 0},
                    {0, 1, 1},
            },
            {
                    {1, 0, 1},
                    {1, 0, 1},
                    {1, 1, 1},
            }
    };

    private static final int[][] YEAR4_BOARD_LAYOUT = {
            {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    private static final int[][][] YEAR4_BOARD_MEANING = {
            {
                    {0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                    {0, 0, 0, 0, 0, 0, -1, 2, 2, 3, 3, 4, 4, 5, 5},
                    {1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5},
                    {1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5},
                    {1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5},
                    {1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5},
                    {1, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
            },
            {
                    {1, 2, 3, 4, 5, 6, -1, -1, -1},
                    {7, 8, 9, 10, 11, 12, -1, 1, 2, 1, 2, 1, 2, 1, 2},
                    {1, 2, 3, 4, 5, 6, 7, 3, 4, 3, 4, 3, 4, 3, 4},
                    {8, 9, 10, 11, 12, 13, 14, 5, 6, 5, 6, 5, 6, 5, 6},
                    {15, 16, 17, 18, 19, 20, 21, 7, 8, 7, 8, 7, 8, 7, 8},
                    {22, 23, 24, 25, 26, 27, 28, 9, 10, 9, 10, 9, 10, 9, 10},
                    {29, 30, 31, -1, -1, -1, -1, -1, -1}
            }
    };

    private static final int PARALLELISM = (int) (Runtime.getRuntime().availableProcessors() * 0.9);
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

    private static final int[][][] ORIGINAL_TILES = YEAR_TILES;
    private static final int[][] ORIGINAL_BOARD_LAYOUT = YEAR_BOARD_LAYOUT;
    private static final int[][][] ORIGINAL_BOARD_MEANING = YEAR_BOARD_MEANING;
    private static final int BOARD_CELL_COUNT = Arrays.stream(ORIGINAL_BOARD_LAYOUT).mapToInt(row -> row.length).sum();
    private static final int BITMASK_ARRAY_LENGTH = (int) Math.ceil(BOARD_CELL_COUNT / 64d);

    static {
        System.out.println("Board Cell Count: " + BOARD_CELL_COUNT);
        System.out.println("Bitmask Array Length: " + BITMASK_ARRAY_LENGTH);
    }

    private static final Tile[] TILES;
    private static final int TILE_COUNT;
    private static final long[] EMPTY_BOARD_BITMASK;
    private static final long[][] BOARD_MEANING_BITMASKS;
    private static final int[] MAX_MEANING_VALUES;

    static {
        int[][] meaningArray = ORIGINAL_BOARD_MEANING[0];
        int max = max(meaningArray);
        BOARD_MEANING_BITMASKS = new long[max + 1][BITMASK_ARRAY_LENGTH];
        for (int j = 0; j < meaningArray.length; j++) {
            for (int k = 0; k < meaningArray[j].length; k++) {
                int cell = meaningArray[j][k];
                if (cell >= 0) {
                    int bitToSet = (j * meaningArray[j].length + k);
                    setBitInBitmask(BOARD_MEANING_BITMASKS[cell], bitToSet);
                }
            }
        }

        MAX_MEANING_VALUES = new int[max + 1];
        for (int j = 0; j < meaningArray.length; j++) {
            for (int k = 0; k < meaningArray[j].length; k++) {
                int cell = meaningArray[j][k];
                if (cell >= 0) {
                    MAX_MEANING_VALUES[cell] = Math.max(MAX_MEANING_VALUES[cell], ORIGINAL_BOARD_MEANING[1][j][k]);
                }
            }
        }
    }

    private static final Deque<Task> TASK_QUEUE = new ConcurrentLinkedDeque<>();
    private static int TOTAL_TASKS;
    private static final AtomicInteger TASK_COUNTER = new AtomicInteger(0);

    /**
     * First dimension: Meaning Area Index
     * Second dimension: List Index
     * Third dimension: Two bitmasks
     * Fourth dimension 0: Bitmask; one for each cell in the meaning area that must be surrounded by tiles; all interesting cells are 1
     * Fourth dimension 1: Bitmask; one for each cell in the meaning area that must be empty and surrounded by tiles; all interesting cells are 1 except the center cell
     */
    private static final long[][][][] PRUNE_AND_EQUALS_AT_LEAST_TWO_BITMASKS;
    /**
     * First dimension: List Index
     * Second dimension: Two bitmasks
     * Third dimension 0: Bitmask; area of interest; all interesting cells are 1
     * Third dimension 1: Bitmask; pattern that is not allowed
     */
    private static final long[][][] BANNED_BITMASKS;
    /**
     * Patterns that are not allowed; used to generate banned bitmasks
     * First dimension: Pattern Index
     * Second dimension: Rows
     * Third dimension: Columns
     * Fourth dimension: Cell Value; -1 = don't care, 0 = empty, 1 = filled
     */
    private static final int[][][] BANNED_PATTERNS = new int[][][]{
            {
                    {-1, 1, 1, -1},
                    {1, 0, 0, 1},
                    {-1, 1, 1, -1},
            },
            {
                    {-1, 1, -1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {-1, 1, -1},
            },
            {
                    {-1, 1, 1, 1, -1},
                    {1, 0, 0, 0, 1},
                    {-1, 1, 1, 1, -1},
            },
            {
                    {-1, 1, -1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {-1, 1, -1},
            },
            {
                    {-1, 1, 1, 1, 1, -1},
                    {1, 0, 0, 0, 0, 1},
                    {-1, 1, 1, 1, 1, -1},
            },
            {
                    {-1, 1, -1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {-1, 1, -1},
            },
            {
                    {-1, 1, 1, 1, 1, 1, -1},
                    {1, 0, 0, 0, 0, 0, 1},
                    {-1, 1, 1, 1, 1, 1, -1},
            },
            {
                    {-1, 1, -1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {-1, 1, -1},
            },
            {
                    {-1, 1, 1, 1, 1, 1, 1, -1},
                    {1, 0, 0, 0, 1, 0, 0, 1},
                    {-1, 1, 1, 1, 1, 1, 1, -1},
            },
            {
                    {-1, 1, -1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {1, 0, 1},
                    {-1, 1, -1},
            },
            {
                    {-1, 1, 1, -1},
                    {1, 0, 0, 1},
                    {1, 0, 0, 1},
                    {-1, 1, 1, -1},
            },
            {
                    {-1, 1, -1, -1},
                    {1, 0, 1, -1},
                    {1, 0, 0, 1},
                    {-1, 1, 1, -1},
            },
            {
                    {-1, -1, 1, -1},
                    {-1, 1, 0, 1},
                    {1, 0, 0, 1},
                    {-1, 1, 1, -1},
            },
            {
                    {-1, 1, 1, -1},
                    {1, 0, 0, 1},
                    {-1, 1, 0, 1},
                    {-1, -1, 1, -1},
            },
            {
                    {-1, 1, 1, -1},
                    {1, 0, 0, 1},
                    {1, 0, 1, -1},
                    {-1, 1, -1, -1},
            }
    };
    private static final CountDownLatch END_LATCH = new CountDownLatch(PARALLELISM);

    static {
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < ORIGINAL_TILES.length; i++) {
            tiles.add(new Tile(
                    ORIGINAL_TILES[i],
                    i,
                    COLORS.get(i % COLORS.size())
            ));
        }
        tiles.sort((o1, o2) -> {
            int size1 = o1.base.length * o1.base[0].length;
            int size2 = o2.base.length * o2.base[0].length;
            return Integer.compare(size2, size1);
        });
        TILES = tiles.toArray(new Tile[0]);
        TILE_COUNT = TILES.length;

        EMPTY_BOARD_BITMASK = fromArray(ORIGINAL_BOARD_LAYOUT);

        PRUNE_AND_EQUALS_AT_LEAST_TWO_BITMASKS = new long[BOARD_MEANING_BITMASKS.length][][][];
        for (int i = 0; i < PRUNE_AND_EQUALS_AT_LEAST_TWO_BITMASKS.length; i++) {
            List<long[][]> bitmaskList = new ArrayList<>();
            int[][] tmpBoardArray = new int[ORIGINAL_BOARD_LAYOUT.length][ORIGINAL_BOARD_LAYOUT[0].length];
            for (int j = 0; j < ORIGINAL_BOARD_LAYOUT.length; j++) {
                for (int k = 0; k < ORIGINAL_BOARD_LAYOUT[i].length; k++) {
                    if (ORIGINAL_BOARD_MEANING[0][j][k] == i) {
                        setSurroundingCoordinates(tmpBoardArray, j, k, 1);
                        long[][] bitmasks = new long[2][];
                        bitmasks[0] = fromArray(tmpBoardArray);

                        long[] bitmask = fromArray(tmpBoardArray);
                        clearBitInBitmask(bitmask, (j * tmpBoardArray[j].length + k));
                        bitmasks[1] = bitmask;

                        bitmaskList.add(bitmasks);
                        setSurroundingCoordinates(tmpBoardArray, j, k, 0);
                    }
                }
            }
            PRUNE_AND_EQUALS_AT_LEAST_TWO_BITMASKS[i] = bitmaskList.toArray(new long[0][][]);
        }
    }

    // create banned bitmasks
    static {
        Set<long[][]> bannedBitmasks = new TreeSet<>((o1, o2) -> {
            int cmp = Arrays.compare(o1[0], o2[0]);
            if (cmp != 0) return cmp;
            return Arrays.compare(o1[1], o2[1]);
        });

        int[][] board = new int[ORIGINAL_BOARD_LAYOUT.length][ORIGINAL_BOARD_LAYOUT[0].length];
        for (int row = 0; row < ORIGINAL_BOARD_LAYOUT.length; row++) {
            for (int col = 0; col < ORIGINAL_BOARD_LAYOUT[row].length; col++) {
                for (int[][] pattern : BANNED_PATTERNS) {
                    if (row - 2 + pattern.length > ORIGINAL_BOARD_LAYOUT.length ||
                            col - 2 + pattern[0].length > ORIGINAL_BOARD_LAYOUT[row].length) {
                        continue;
                    }

                    long[][] bannedBitmask = createBannedBitmaskForPatternAt(pattern, row - 1, col - 1, board);
                    if (bannedBitmask != null) {
                        bannedBitmasks.add(bannedBitmask);
                    }
                }
            }
        }
        BANNED_BITMASKS = bannedBitmasks.toArray(new long[0][][]);
        System.out.println("Banned Bitmasks Count: " + BANNED_BITMASKS.length);
        for (int i = 0; i < BANNED_BITMASKS.length; i++) {
            System.out.println("Banned Bitmask " + i + ": ");
            System.out.println("Pattern Bitmask: " + bitmaskToBinaryString(BANNED_BITMASKS[i][0]));
            System.out.println("Area Bitmask:    " + bitmaskToBinaryString(BANNED_BITMASKS[i][1]));
        }
    }

    private static void resetBoard(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            System.arraycopy(ORIGINAL_BOARD_LAYOUT[i], 0, array[i], 0, array[i].length);
        }
    }

    private static long[][] createBannedBitmaskForPatternAt(int[][] pattern, int row, int col, int[][] board) {
        int meaningArea = -1;

        resetBoard(board);
        // create area of interest
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                if (pattern[i][j] == -1 || row + i < 0 || row + i >= board.length || col + j < 0 || col + j >= board[row + i].length) {
                    continue;
                }

                int boardRow = row + i;
                int boardCol = col + j;

                if (boardRow >= 0 && boardRow < board.length && boardCol >= 0 && boardCol < board[boardRow].length) {
                    if (board[boardRow][boardCol] == 1 && pattern[i][j] == 0) {
                        // conflict: trying to set empty cell on already filled cell
                        return null;
                    }
                    if (pattern[i][j] == 1) {
                        board[boardRow][boardCol] = 1;
                    } else if (pattern[i][j] == 0) {
                        int meaning = ORIGINAL_BOARD_MEANING[0][boardRow][boardCol];
                        if (meaningArea == -1) {
                            meaningArea = meaning;
                        } else if (meaningArea != meaning) {
                            // conflict: trying to set empty cell on different meaning area
                            return null;
                        }
                        board[boardRow][boardCol] = 1;
                    }
                } else {
                    // conflict: pattern goes out of board bounds
                    return null;
                }
            }
        }
        long[][] bannedBitmask = new long[2][];
        bannedBitmask[0] = fromArray(board);

        // create pattern bitmask
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                if (pattern[i][j] == 0 && row + i >= 0 && row + i < board.length && col + j >= 0 && col + j < board[row + i].length) {
                    int boardRow = row + i;
                    int boardCol = col + j;
                    board[boardRow][boardCol] = 0;
                }
            }
        }
        bannedBitmask[1] = fromArray(board);

        return bannedBitmask;
    }

    private static final long[][] PRUNE_COUNTER = new long[3][TILE_COUNT];
    private static final long[] NON_PRUNE_COUNTER = new long[TILE_COUNT];
    private static final Semaphore PRUNE_COUNTER_MUTEX = new Semaphore(1);

    private static void setSurroundingCoordinates(int[][] board, int row, int col, int value) {
        board[row][col] = value;
        if (row > 0) {
            board[row - 1][col] = value;
        }
        if (row < board.length - 1) {
            board[row + 1][col] = value;
        }
        if (col > 0) {
            board[row][col - 1] = value;
        }
        if (col < board[row].length - 1) {
            board[row][col + 1] = value;
        }
    }

    private StaticLongArrayMain() {
    }

    static void main() {
        test();
        plausibilityChecks();

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
        System.out.println("Empty Board Bitmask: " + bitmaskToBinaryString(EMPTY_BOARD_BITMASK));
        System.out.println("Board Meaning Bitmasks: ");
        for (long[] bitmask : BOARD_MEANING_BITMASKS) {
            System.out.println(bitmaskToBinaryString(bitmask));
        }
        installShutdownHook();
        System.gc();
        long startTime = System.currentTimeMillis();
        solve();
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
        printPruneCounters();
        printSolutionSummary();
        System.out.println(Arrays.toString(SOLUTIONS));
    }

    private static void printPruneCounters() {
        System.out.println("Prune Counters: ");
        for (int i = 0; i < PRUNE_COUNTER.length; i++) {
            System.out.println("Prune " + i + ": " + Arrays.toString(PRUNE_COUNTER[i]));
        }
        System.out.println("Non-Prune Counters: " + Arrays.toString(NON_PRUNE_COUNTER));
    }

    private static void installShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                END_LATCH.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            System.out.println("Shutdown Hook: Final Solutions Summary");
            printPruneCounters();
            printSolutionSummary();
            System.out.println(Arrays.toString(SOLUTIONS));
        }));
    }

    private static void plausibilityChecks() {
        int cellsInBoard = 0;
        for (int[] row : ORIGINAL_BOARD_LAYOUT) {
            for (int cell : row) {
                if (cell == 0) {
                    cellsInBoard++;
                }
            }
        }

        int cellsInTiles = 0;
        for (int[][] tile : ORIGINAL_TILES) {
            for (int[] row : tile) {
                for (int cell : row) {
                    if (cell == 1) {
                        cellsInTiles++;
                    }
                }
            }
        }

        int meaningCount = max(ORIGINAL_BOARD_MEANING[0]) + 1;

        if ((cellsInBoard - meaningCount) != cellsInTiles) {
            throw new IllegalStateException("Number of empty cells in board (" + cellsInBoard + ") does not match number of cells in tiles (" + cellsInTiles + "), considering meanings (" + meaningCount + ").");
        }
    }

    private static void solve() {
        prepareTasks();
        TOTAL_TASKS = TASK_QUEUE.size();

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
        long[] tmpBitmask = new long[BITMASK_ARRAY_LENGTH];
        for (int i = 0; i < TILES[0].allPositions().length; i++) {
            final PositionedTile positionedTile = TILES[0].allPositions()[i];
            for (int j = 0; j < TILES[1].allPositions().length; j++) {
                final PositionedTile positionedTile2 = TILES[1].allPositions()[j];
                bitmaskAnd(EMPTY_BOARD_BITMASK, positionedTile.bitmask(), tmpBitmask);
                if (bitmaskAndIsZeroSwitch(EMPTY_BOARD_BITMASK, positionedTile.bitmask()) && bitmaskAndIsZeroSwitch(positionedTile2.bitmask(), tmpBitmask)) {
                    int[] usedPositionedTileIds = new int[TILE_COUNT];
                    usedPositionedTileIds[0] = positionedTile.id();
                    usedPositionedTileIds[1] = positionedTile2.id();
                    long[] boardBitmask = constructBoardBitmask(usedPositionedTileIds, 1);
                    if (prune(1, boardBitmask, usedPositionedTileIds, boardBitmask, new long[BITMASK_ARRAY_LENGTH], new long[PRUNE_COUNTER.length][TILE_COUNT])) {
                        continue;
                    }
                    TASK_QUEUE.add(new Task(
                            usedPositionedTileIds,
                            2
                    ));
                }
            }
        }
        System.out.println("Prepared " + TASK_QUEUE.size() + " tasks.");
    }

    private record TaskRunner(
            int index
    ) implements Runnable {
        @Override
        public void run() {
            Task task;
            long[][] pruneCounters = new long[PRUNE_COUNTER.length][TILE_COUNT];
            long[] nonPruneCounters = new long[TILE_COUNT];
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                addPruneCounters(pruneCounters, nonPruneCounters);
                END_LATCH.countDown();
            }));
            while ((task = TASK_QUEUE.poll()) != null && !Thread.currentThread().isInterrupted()) {
                calculate(task, pruneCounters, nonPruneCounters);
                int completedTasks = TASK_COUNTER.incrementAndGet();
                System.out.println(completedTasks + " / " + TOTAL_TASKS + " tasks completed");
                addPruneCounters(pruneCounters, nonPruneCounters);
                printPruneCounters();
                printSolutionSummary();
            }
        }

        private void calculate(Task task, long[][] pruneCounters, long[] nonPruneCounters) {
            int[] usedPositionedTileIds = new int[TILE_COUNT];
            System.arraycopy(task.usedPositionedTileIds(), 0, usedPositionedTileIds, 0, usedPositionedTileIds.length);
            int tileIndex = task.startTileIndex();
            long[][] boardBitmasks = new long[TILE_COUNT][BITMASK_ARRAY_LENGTH];
            long[] boardBitmask = constructBoardBitmask(usedPositionedTileIds, tileIndex - 1);
            boardBitmasks[tileIndex - 1] = boardBitmask;
            long[] tmpBoardBitmask = new long[BITMASK_ARRAY_LENGTH];
            long[] tmpBitmask = new long[BITMASK_ARRAY_LENGTH];
            byte[][] tmpSolutionBoard = new byte[ORIGINAL_BOARD_LAYOUT.length][ORIGINAL_BOARD_LAYOUT[0].length];
            int[] tmpSolutionMeanings = new int[BOARD_MEANING_BITMASKS.length];

            calculateRecursive(tileIndex, boardBitmasks, usedPositionedTileIds, tmpBoardBitmask, tmpBitmask, pruneCounters, nonPruneCounters, tmpSolutionBoard, tmpSolutionMeanings);
        }

        private void calculateRecursive(int tileIndex, long[][] boardBitmasks, int[] usedPositionedTileIds, long[] tmpBoardBitmask, long[] tmpBitmask, long[][] pruneCounters, long[] nonPruneCounters, byte[][] tmpSolutionBoard, int[] tmpSolutionMeanings) {
            final long[] boardBitmask = boardBitmasks[tileIndex - 1];
            if (tileIndex == TILE_COUNT) {
                submitSolution(usedPositionedTileIds, boardBitmask, tmpSolutionBoard, tmpSolutionMeanings);
                return;
            }

            Tile tile = TILES[tileIndex];
            for (PositionedTile positionedTile : tile.allPositions()) {
                long[] positionedTileBitmask = positionedTile.bitmask();
                if (bitmaskAndIsZeroSwitch(boardBitmask, positionedTileBitmask)) {
                    bitmaskXorSwitch(boardBitmask, positionedTileBitmask, tmpBoardBitmask);
                    if (tileIndex < TILE_COUNT - 1 && prune(tileIndex, boardBitmask, usedPositionedTileIds, tmpBoardBitmask, tmpBitmask, pruneCounters)) {
                        continue;
                    }
                    nonPruneCounters[tileIndex]++;
                    usedPositionedTileIds[tileIndex] = positionedTile.id();
                    System.arraycopy(tmpBoardBitmask, 0, boardBitmasks[tileIndex], 0, BITMASK_ARRAY_LENGTH);
                    calculateRecursive(tileIndex + 1, boardBitmasks, usedPositionedTileIds, tmpBoardBitmask, tmpBitmask, pruneCounters, nonPruneCounters, tmpSolutionBoard, tmpSolutionMeanings);
                }
            }
        }
    }

    private static boolean prune(int tileIndex, long[] boardBitmask, int[] usedPositionedTileIds, long[] tmpBoardBitmask, long[] tmpBitmask, long[][] pruneCounters) {
        return pruneNoCellsEmptyInMeaningArea(tileIndex, boardBitmask, usedPositionedTileIds, tmpBoardBitmask, tmpBitmask, pruneCounters)
                || pruneNoTwoSingleCellsInMeaningArea(tileIndex, boardBitmask, usedPositionedTileIds, tmpBoardBitmask, tmpBitmask, pruneCounters)
                || pruneBannedBitmasks(tileIndex, boardBitmask, usedPositionedTileIds, tmpBoardBitmask, tmpBitmask, pruneCounters)
                ;
    }

    private static boolean pruneNoCellsEmptyInMeaningArea(int tileIndex, long[] boardBitmask, int[] usedPositionedTileIds, long[] tmpBoardBitmask, long[] tmpBitmask, long[][] pruneCounters) {
        for (long[] meaningBitmask : BOARD_MEANING_BITMASKS) {
            int oneCount = bitmaskAndXorCountOnesSwitch(tmpBoardBitmask, meaningBitmask, meaningBitmask);
            if (oneCount == 0) {
                pruneCounters[0][tileIndex]++;
                return true;
            }
        }
        return false;
    }

    private static boolean pruneNoTwoSingleCellsInMeaningArea(int tileIndex, long[] boardBitmask, int[] usedPositionedTileIds, long[] tmpBoardBitmask, long[] tmpBitmask, long[][] pruneCounters) {
        for (long[][][] andEqualsAtLeastTwoBitmasks : PRUNE_AND_EQUALS_AT_LEAST_TWO_BITMASKS) {
            int matchCount = 0;
            for (long[][] andEqualsAtLeastTwoBitmask : andEqualsAtLeastTwoBitmasks) {
                if (bitmaskAndEqualsSwitch(tmpBoardBitmask, andEqualsAtLeastTwoBitmask[0], andEqualsAtLeastTwoBitmask[1])) {
                    matchCount++;
                    if (matchCount >= 2) {
                        pruneCounters[1][tileIndex]++;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean pruneBannedBitmasks(int tileIndex, long[] boardBitmask, int[] usedPositionedTileIds, long[] tmpBoardBitmask, long[] tmpBitmask, long[][] pruneCounters) {
        for (long[][] bannedBitmask : BANNED_BITMASKS) {
            if (bitmaskAndEqualsSwitch(tmpBoardBitmask, bannedBitmask[0], bannedBitmask[1])) {
                pruneCounters[2][tileIndex]++;
                return true;
            }
        }
        return false;
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

    public static long[] fromArray(int[][] array) {
        long[] mask = new long[BITMASK_ARRAY_LENGTH];

        int bitIndex = 0;
        for (int[] row : array) {
            for (int cell : row) {
                if (cell != 0) {
                    setBitInBitmask(mask, bitIndex);
                }
                bitIndex++;
            }
        }
        return mask;
    }

    public static int[][] toArray(long[] bitmask, int rows, int columns) {
        final int[][] result = new int[rows][columns];
        final int relevantLength = rows * columns;

        for (int pos = 0; pos < relevantLength; pos++) {
            int arrayIndex = pos / 64;
            int bitIndex = pos % 64;
            int row = pos / columns;
            int col = pos % columns;

            long word = bitmask[arrayIndex];
            int bit = (int) ((word >>> (63 - bitIndex)) & 1L);
            result[row][col] = bit;
        }

        return result;
    }

    public static byte[][] toByteArray(long[] bitmask, byte[][] result) {
        int rows = result.length;
        int columns = result[0].length;
        final int relevantLength = rows * columns;

        for (int pos = 0; pos < relevantLength; pos++) {
            int arrayIndex = pos / 64;
            int bitIndex = pos % 64;
            int row = pos / columns;
            int col = pos % columns;

            long word = bitmask[arrayIndex];
            int bit = (int) ((word >>> (63 - bitIndex)) & 1L);
            result[row][col] = (byte) bit;
        }

        return result;
    }

    private static long[] constructBoardBitmask(int[] usedPositionedTileIds, int upToIndex) {
        long[] bitmask = EMPTY_BOARD_BITMASK;
        for (int i = 0; i <= upToIndex; i++) {
            PositionedTile positionedTile = TILES[i].allPositions()[usedPositionedTileIds[i]];
            bitmask = bitmaskOr(bitmask, positionedTile.bitmask(), new long[BITMASK_ARRAY_LENGTH]);
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
            PositionedTile[] allPositions
    ) {

        private Tile(int[][] base, int tileNumber, Color color) {
            System.out.println("Creating Tile: " + tileNumber);
            print(base);
            List<int[][]> baseRotated = getAllRotations(base);
            System.out.println("Number of Rotations: " + baseRotated.size());
            List<PositionedTile> ap = getAllPositions(baseRotated, tileNumber);
            this(base, tileNumber, color, ap.toArray(new PositionedTile[0]));
            System.out.println("Number of Boards: " + allPositions.length);

            // test if all positioned tiles are in the correct order with their ids
            for (int i = 0; i < allPositions.length; i++) {
                if (allPositions[i].id() != i) {
                    throw new IllegalStateException("PositionedTile id mismatch: expected " + i + " but got " + allPositions[i].id());
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

        private static List<PositionedTile> getAllPositions(List<int[][]> baseRotated, int tileNumber) {
            List<long[]> results = new ArrayList<>();

            for (int[][] tile : baseRotated) {
                results.addAll(getAllPositionsForTile(tile));
            }
            List<PositionedTile> tiles = new ArrayList<>(results.stream()
                    .distinct()
                    .map(bitmask -> new PositionedTile(
                            bitmask,
                            results.indexOf(bitmask)
                    ))
                    .toList());
            tiles.sort(Comparator.comparingInt(PositionedTile::id));
            return Collections.unmodifiableList(tiles);
        }

        private static List<long[]> getAllPositionsForTile(int[][] tile) {
            List<long[]> results = new ArrayList<>();
            int[][] boardArray = ORIGINAL_BOARD_LAYOUT;

            for (int i = 0; i < boardArray.length - tile.length + 1; i++) {
                for (int j = 0; j < boardArray[i].length - tile[0].length + 1; j++) {
                    int[][] newBoard = place(tile, boardArray, i, j);
                    if (isValid(newBoard)) {
                        removeBoard(newBoard, boardArray);
                        long[] boardBitmask = fromArray(newBoard);
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
                    ", allPositions=" + Arrays.stream(allPositions)
                    .map(StaticLongArrayMain.PositionedTile::toString)
                    .map(s -> System.lineSeparator() + s)
                    .reduce("", String::concat)
                    + '}';
        }
    }


    private record PositionedTile(
            long[] bitmask,
            int id
    ) {
        @Override
        public String toString() {
            return "PositionedTile{" +
                    "bitmask=" + bitmaskToBinaryString(bitmask) +
                    ", id=" + id +
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

    private static void submitSolution(int[] usedPositionedTileIds, long[] finalBoardBitmask, byte[][] tmpSolutionBoard, int[] tmpSolutionMeanings) {
        for (long[] meaningBitmask : BOARD_MEANING_BITMASKS) {
            long[] tmpBitmask = new long[BITMASK_ARRAY_LENGTH];
            long[] meaningBitmaskEntry = bitmaskXor(bitmaskAnd(finalBoardBitmask, meaningBitmask, tmpBitmask), meaningBitmask, tmpBitmask);
            int oneCount = bitmaskCountOnes(meaningBitmaskEntry);
            if (oneCount != 1) {
                return;
            }
        }
        toByteArray(finalBoardBitmask, tmpSolutionBoard);
        for (int i = 0; i < tmpSolutionBoard.length; i++) {
            for (int j = 0; j < tmpSolutionBoard[i].length; j++) {
                if (tmpSolutionBoard[i][j] == 0) {
                    int meaningIndex = ORIGINAL_BOARD_MEANING[0][i][j];
                    int meaningValue = ORIGINAL_BOARD_MEANING[1][i][j];
                    tmpSolutionMeanings[meaningIndex] = meaningValue;
                }
            }
        }

        addSolutionForMeanings(tmpSolutionMeanings);
        if (Arrays.equals(tmpSolutionMeanings, SOLUTION_TO_SAVE)) {
            System.out.println("Found solution for meanings " + Arrays.toString(SOLUTION_TO_SAVE));
            generateImageForBoard(usedPositionedTileIds);
        }
    }

    private static final long[] SOLUTIONS;
    private static final int[] SOLUTION_TO_SAVE = {12, 24, 2, 5};

    static {
        List<int[]> allPossibleMeanings = constructAllPossibleMeanings();
        SOLUTIONS = new long[allPossibleMeanings.size()];
        System.out.println("Initialized SOLUTIONS array with size: " + SOLUTIONS.length);
    }

    private static void addSolutionForMeanings(int[] tmpSolutionMeanings) {
        int index = 0;
        for (int i = 0; i < MAX_MEANING_VALUES.length; i++) {
            int meaningValue = tmpSolutionMeanings[i];
            int multiplier = 1;
            for (int j = 0; j < i; j++) {
                multiplier *= MAX_MEANING_VALUES[j];
            }
            index += (meaningValue - 1) * multiplier;
        }
        synchronized (SOLUTIONS) {
            SOLUTIONS[index]++;
        }
    }


    private static void printSolutionSummary() {
        long min = Long.MAX_VALUE;
        long max = 0;
        double sum = 0;
        for (long count : SOLUTIONS) {
            if (count > max) {
                max = count;
            }
            if (count < min) {
                min = count;
            }
            sum += count;
        }
        double average = sum / SOLUTIONS.length;
        System.out.println("Meanings Summary:");
        System.out.println("Min Solutions per Meaning: " + min);
        System.out.println("Max Solutions per Meaning: " + max);
        System.out.println("Average Solutions per Meaning: " + average);
    }

    private static List<int[]> constructAllPossibleMeanings() {
        return constructAllPossibleMeaningsForIndexRecursive(0);
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
        testSurroundingSetter(
                new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0}
                },
                new int[][]{
                        {0, 1, 0, 0, 0},
                        {1, 1, 1, 0, 0},
                        {0, 1, 0, 0, 0}
                },
                1, 1, 1
        );
    }

    private static void testLongArrayConversion(int[][] array) {
        System.out.println("Testing long-array conversion for array:");
        print(array);

        long[] bitmask = fromArray(array);
        System.out.println("Converted to bitmask: " + bitmaskToBinaryString(bitmask));
        int[][] newArray = toArray(bitmask, array.length, array[0].length);
        System.out.println("Converted back to array:");
        print(newArray);

        if (!Arrays.deepEquals(newArray, array)) {
            throw new IllegalStateException();
        }
    }

    private static void testSurroundingSetter(int[][] input, int[][] expected, int row, int col, int value) {
        System.out.println("Testing surrounding setter at (" + row + ", " + col + ") with value " + value);
        System.out.println("Input:");
        print(input);
        setSurroundingCoordinates(input, row, col, value);
        System.out.println("Result:");
        print(input);
        System.out.println("Expected:");
        print(expected);

        if (!Arrays.deepEquals(input, expected)) {
            throw new IllegalStateException("Surrounding setter test failed");
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
            PositionedTile positionedTile = StaticLongArrayMain.TILES[i].allPositions()[usedPositionedTileIds[i]];
            positionedTiles.add(positionedTile);
        }

        for (int i = 0; i < positionedTiles.size(); i++) {
            PositionedTile tile = positionedTiles.get(i);
            Color color = StaticLongArrayMain.TILES[i].color();
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
            PositionedTile positionedTile = StaticLongArrayMain.TILES[i].allPositions()[usedPositionedTileIds[i]];
            positionedTiles.add(positionedTile);
        }

        for (int i = 0; i < positionedTiles.size(); i++) {
            PositionedTile tile = positionedTiles.get(i);
            Color color = StaticLongArrayMain.TILES[i].color();
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

    private static String bitmaskToBinaryString(long[] bitmask) {
        StringBuilder sb = new StringBuilder();
        for (long word : bitmask) {
            String bits = Long.toBinaryString(word);
            sb.append("0".repeat(64 - bits.length()));
            sb.append(bits);
        }

        String full = sb.length() > BOARD_CELL_COUNT ? sb.substring(0, BOARD_CELL_COUNT) : sb.toString();

        StringBuilder formatted = new StringBuilder();
        int rowWidth = ORIGINAL_BOARD_LAYOUT[0].length;
        for (int i = 0; i < full.length(); i++) {
            formatted.append(full.charAt(i));
            if ((i + 1) % rowWidth == 0 && i != full.length() - 1) {
                formatted.append('_');
            }
        }
        return formatted.toString();
    }

    private static long[] bitmaskOr(long[] a, long[] b, long[] result) {
        for (int i = 0; i < BITMASK_ARRAY_LENGTH; i++) {
            result[i] = a[i] | b[i];
        }
        return result;
    }

    private static void bitmaskXorSwitch(long[] a, long[] b, long[] result) {
        switch (BITMASK_ARRAY_LENGTH) {
            case 1 -> result[0] = a[0] ^ b[0];
            case 2 -> {
                result[0] = a[0] ^ b[0];
                result[1] = a[1] ^ b[1];
            }
            default -> {
                for (int i = 0; i < BITMASK_ARRAY_LENGTH; i++) {
                    result[i] = a[i] ^ b[i];
                }
            }
        }
    }

    private static long[] bitmaskAnd(long[] a, long[] b, long[] result) {
        for (int i = 0; i < BITMASK_ARRAY_LENGTH; i++) {
            result[i] = a[i] & b[i];
        }
        return result;
    }

    private static long[] bitmaskXor(long[] a, long[] b, long[] result) {
        for (int i = 0; i < BITMASK_ARRAY_LENGTH; i++) {
            result[i] = a[i] ^ b[i];
        }
        return result;
    }

    private static void setBitInBitmask(long[] bitmask, int position) {
        int arrayIndex = position / 64;
        int bitIndex = position % 64;
        bitmask[arrayIndex] |= (1L << (63 - bitIndex));
    }

    private static void clearBitInBitmask(long[] bitmask, int position) {
        int arrayIndex = position / 64;
        int bitIndex = position % 64;
        bitmask[arrayIndex] &= ~(1L << (63 - bitIndex));
    }

    private static boolean bitmaskAndIsZeroSwitch(long[] a, long[] b) {
        return switch (BITMASK_ARRAY_LENGTH) {
            case 1 -> (a[0] & b[0]) == 0;
            case 2 -> (a[0] & b[0]) == 0 && (a[1] & b[1]) == 0;
            default -> {
                boolean result = true;
                for (int i = 0; i < BITMASK_ARRAY_LENGTH; i++) {
                    if ((a[i] & b[i]) != 0) {
                        result = false;
                        break;
                    }
                }
                yield result;
            }
        };
    }

    private static int bitmaskCountOnes(long[] bitmask) {
        int count = 0;
        for (long l : bitmask) {
            count += Long.bitCount(l);
        }
        return count;
    }

    private static int bitmaskAndXorCountOnesSwitch(long[] a, long[] b, long[] c) {
        return switch (BITMASK_ARRAY_LENGTH) {
            case 1 -> Long.bitCount((a[0] & b[0]) ^ c[0]);
            case 2 -> Long.bitCount((a[0] & b[0]) ^ c[0])
                    + Long.bitCount((a[1] & b[1]) ^ c[1]);
            default -> {
                int count = 0;
                for (int i = 0; i < BITMASK_ARRAY_LENGTH; i++) {
                    count += Long.bitCount((a[i] & b[i]) ^ c[i]);
                }
                yield count;
            }
        };
    }

    private static boolean bitmaskAndEqualsSwitch(long[] tmpBoardBitmask, long[] a, long[] b) {
        return switch (BITMASK_ARRAY_LENGTH) {
            case 1 -> (tmpBoardBitmask[0] & a[0]) == b[0];
            case 2 -> (tmpBoardBitmask[0] & a[0]) == b[0]
                    && (tmpBoardBitmask[1] & a[1]) == b[1];
            default -> {
                boolean result = true;
                for (int i = 0; i < BITMASK_ARRAY_LENGTH; i++) {
                    if ((tmpBoardBitmask[i] & a[i]) != b[i]) {
                        result = false;
                        break;
                    }
                }
                yield result;
            }
        };
    }

    private static void addPruneCounters(long[][] pruneCounters, long[] nonPruneCounters) {
        try {
            PRUNE_COUNTER_MUTEX.acquire();
            for (int i = 0; i < PRUNE_COUNTER.length; i++) {
                for (int j = 0; j < PRUNE_COUNTER[i].length; j++) {
                    PRUNE_COUNTER[i][j] += pruneCounters[i][j];
                    pruneCounters[i][j] = 0;
                }
            }
            for (int i = 0; i < NON_PRUNE_COUNTER.length; i++) {
                NON_PRUNE_COUNTER[i] += nonPruneCounters[i];
                nonPruneCounters[i] = 0;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            PRUNE_COUNTER_MUTEX.release();
        }
    }

}
