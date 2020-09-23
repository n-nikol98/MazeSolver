package com.mazesolver;

import com.mazesolver.exception.*;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.Arrays;

/**
 * Helper class to valid user Maze related input and throw Exceptions, when necessary.
 * Takes the original Maze and not the Expanded one.
 * Also validates sourceCoordinates.
 * */
final class InputValidator {

    private final static Logger logger = Logger.getLogger(InputValidator.class);

    private final int[][] maze;
    private final Point sourceCoordinates;

    //Init
    InputValidator(final int[][] maze, final Point sourceCoordinates) {
        this.maze = maze;
        this.sourceCoordinates = sourceCoordinates;
    }

    /**
     * Quick validate function that just throws a lot of exceptions if something is wrong.
     * Uses helper boolean functions, which are quite simple and will not be explained below.
     *
     * */
    void validate() throws EmptyMazeException, MalformedMazeException,
            InvalidMazeCoordinatesException, InvalidMazeStartPositionException,
            InvalidMazeValuesException {
        logger.info("Initialize input validation of a Maze and the source coordinates at which to " +
                "begin traversal.");

        logger.info("Check if the Maze is empty (i.e. if it has any cells).");
        if (mazeIsEmpty()) {
            logger.error("Input Maze is empty.");
            throw new EmptyMazeException(maze);
        }

        logger.info("NOT Empty Maze Check - OK");

        logger.info("Check if the Maze is rectangular.");
        if (!mazeIsRectangle()) {
            logger.error("Input Maze is not rectangular.");
            throw new MalformedMazeException(maze);
        }

        logger.info("Maze Rectangular Check - OK");

        logger.info("Confirm that the Maze does not contain cells with negative values.");
        if (!mazeContainsValidValues()) {
            logger.error("Input Maze contains a cell / cells with negative values.");
            throw new InvalidMazeValuesException(maze);
        }

        logger.info("Maze Valid Values Check - OK");

        logger.info("Check if the traversal start coordinates are valid (i.e. are they in the maze?).");
        if (!coordinatesAreValid()) {
            logger.error("Input traversal start coordinates are invalid.");
            throw new InvalidMazeCoordinatesException(maze, sourceCoordinates);
        }

        logger.info("Maze Coordinates Validity Check - OK");

        logger.info("Check if the traversal start coordinates point to a cell with a positive value.");
        if (!coordinatesValueIsValid()) {
            logger.error("Input traversal start coordinates point to a cell with a non-positive value.");
            throw new InvalidMazeStartPositionException(maze, sourceCoordinates);
        }

        logger.info("Maze Start Position Value Check - OK");
    }

    private boolean mazeIsEmpty() {
        if (maze.length == 0)
            return true;

        return Arrays.stream(maze).allMatch(row -> row.length == 0);
    }

    private boolean mazeIsRectangle() {
        return Arrays.stream(maze).allMatch(row ->
                row.length == maze[0].length);
    }

    private boolean mazeContainsValidValues() {
        return Arrays.stream(maze).allMatch(row ->
                Arrays.stream(row).allMatch(cell ->
                        cell >= 0));
    }

    private boolean coordinatesAreValid() {
        return !(sourceCoordinates.getY() < 0) &&
                !(sourceCoordinates.getY() >= maze.length) &&
                !(sourceCoordinates.getX() < 0) &&
                !(sourceCoordinates.getX() >= maze[0].length);
    }

    private boolean coordinatesValueIsValid() {
        return maze[(int) sourceCoordinates.getY()]
                [(int) sourceCoordinates.getX()] > 0;
    }
}
