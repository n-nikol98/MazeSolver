package com.mazesolver.exception;

import java.awt.*;

public final class InvalidMazeCoordinatesException extends MazePositionException {

    public InvalidMazeCoordinatesException(final int[][] maze, final Point sourceCoordinates) {
        super(maze, sourceCoordinates, "The start position coordinates: " +
                getFormattedCoordinatesString(sourceCoordinates) +
                " are invalid! Please note that coordinates must be non-negative values " +
                "and within the array (0-indexed) bounds of the maze.");
    }
}
