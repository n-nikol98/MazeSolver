package com.mazesolver.exception;

public  final class InvalidMazeValuesException extends MazeException {

    public InvalidMazeValuesException(final int[][] maze) {
        super(maze,
                "This Maze contains invalid values! " +
                        "Please note that all of the cells in your maze " +
                        "must have non-negative values!");
    }
}
