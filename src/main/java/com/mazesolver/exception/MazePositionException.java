package com.mazesolver.exception;

import java.awt.*;

@SuppressWarnings("unused")
public class MazePositionException extends MazeException {

    private final Point sourceCoordinates;

    public MazePositionException(final int[][] maze, final Point sourceCoordinates) {
        super(maze, "This Exception was thrown because you cannot traverse"  +
                "this Maze from the start position: " +
                getFormattedCoordinatesString(sourceCoordinates) + "!" +
                "This may be due to an inescapable Maze, invalid coordinates amd / or an invalid" +
                "starting position/");
        this.sourceCoordinates = sourceCoordinates;
    }

    MazePositionException(final int[][] maze, final Point sourceCoordinates, final String message) {
        super(maze, message);
        this.sourceCoordinates = sourceCoordinates;
    }

    public Point getSourceCoordinates() {
        return sourceCoordinates;
    }

    static String getFormattedCoordinatesString(final Point sourceCoordinates) {
        return "(" + (int) sourceCoordinates.getX() + ", " +
                (int) sourceCoordinates.getY() + ")";
    }
}
