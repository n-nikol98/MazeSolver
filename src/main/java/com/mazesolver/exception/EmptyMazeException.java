package com.mazesolver.exception;

public final class EmptyMazeException extends MazeException {

    public EmptyMazeException(final int[][] maze) {
        super(maze,
                "This Maze is empty and cannot be traversed!");
    }
}
