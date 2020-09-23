package com.mazesolver.exception;

import java.awt.*;

public final class InescapableMazeException extends MazePositionException {

    public InescapableMazeException(final int[][] maze, final Point sourceCoordinates) {
        super(maze, sourceCoordinates,
                "This Maze is inescapable from the start position: " +
                        getFormattedCoordinatesString(sourceCoordinates) + "!");
    }
}
