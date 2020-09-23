package com.mazesolver.exception;

public final class MalformedMazeException extends MazeException {

    public MalformedMazeException(final int[][] maze) {
        super(maze,
                "This Maze has an invalid shape!" +
                        "Please note that it must be a Rectangle or a Square.");
    }
}
