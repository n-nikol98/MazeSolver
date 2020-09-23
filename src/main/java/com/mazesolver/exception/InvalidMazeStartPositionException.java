package com.mazesolver.exception;

import java.awt.*;

public final class InvalidMazeStartPositionException extends MazePositionException {

    private final int mazeCellValue;

    public InvalidMazeStartPositionException(final int[][] maze, final Point sourceCoordinates) {
        super(maze, sourceCoordinates,
                "You cannot traverse this Maze from the start position: " +
                getFormattedCoordinatesString(sourceCoordinates) + " as that cell " +
                "has a value of \"" + maze[(int) sourceCoordinates.getY()]
                        [(int) sourceCoordinates.getX()] + "\"! " +
                        "Take note that you may only start on cells which have a positive " +
                        "associated value.");

        this.mazeCellValue = maze[(int) sourceCoordinates.getY()]
                [(int) sourceCoordinates.getX()];
    }

    @SuppressWarnings("unused")
    public int getMazeCellValue() {
        return mazeCellValue;
    }
}
