package com.mazesolver.exception;

public class MazeException extends Exception {

    private final int[][] maze;

    @SuppressWarnings("unused")
    public MazeException(final int[][] maze) {
        super("This Maze has experienced an Exception! " +
                "It may be malformed. Alternatively, an attempt to traverse it from" +
                "an invalid starting position may have been made. " +
                "In addition, please note that if the Maze is inescapable, " +
                "an Exception will also be thrown.");
        this.maze = maze;
    }

    MazeException(final int[][] maze, final String message) {
        super(message);
        this.maze = maze;
    }

    public int[][] getMaze() {
        return maze;
    }
}
