package de.holube.pad;

import de.holube.pad.model.Board;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Distance {

    private final List<Board> boards;

    public Distance(List<Board> boards) {
        this.boards = boards;
    }

    public void calculateDistances() {

    }

    private static class BoardWrapper {

        @Getter
        private final Board board;

        @Getter
        private final Map<Board, Integer> distances = new HashMap<>();

        public BoardWrapper(Board board) {
            this.board = board;
        }

        public void addDistance(Board other, int distance) {
            distances.put(other, distance);
        }

    }

    private static class Day {

        @Getter
        private final int day;
        @Getter
        private final int month;

        @Getter
        private final List<BoardWrapper> boards = new ArrayList<>();

        public Day(int day, int month) {
            this.day = day;
            this.month = month;
        }

        public void addBoard(BoardWrapper board) {
            // if (board.getBoard().getDay() != day || board.getBoard().getMonth() != month)
            //   throw new IllegalStateException("Date does not match");

            boards.add(board);
        }

    }

}
