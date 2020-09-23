package com.mazesolver;

import com.mazesolver.exception.*;
import org.apache.log4j.Logger;

import java.awt.*;

public abstract class MazeSolver {

    private static final Logger logger = Logger.getLogger(MazeSolver.class);

    /**
     *
     * Solves a Rectangular (Square) Maze with Weights for each cell via an implementable algorithm.
     * The point is to find the lowest cost path to a Maze exit (a border cell).
     *
     * NOTE: Input validation is performed and different Exceptions may be thrown.
     *
     * @param maze - A <b>rectangular</b> 2D Maze of non-negative Integers.
     *             0 - Denotes a space, which can not be traversed.
     *             >0 - Denotes a space, which can be traversed, however a certain cost is incurred
     *
     * @param x - X axis coordinate for the Maze. Must be positive and within array bounds.
     * @param y - Y axis coordinate for the Maze. Must be positive and within array bounds.
     *
     * @return The value of the lowest cost path to a Maze exit.
     *
     * @throws InescapableMazeException If the implementing algorithm finds
     * no path from the start X & Y coordinates:
     * */
    public int solve(final int[][] maze, final int x, final int y) throws MalformedMazeException,
            InvalidMazeCoordinatesException, InvalidMazeStartPositionException,
            EmptyMazeException, InescapableMazeException, InvalidMazeValuesException {

        logger.info("Initialize Abstract Maze Solver.");

        final Point sourceCoordinates = new Point(x, y);

        logger.info("Passing input Maze and source coordinates to Validator.");

        final InputValidator validator = new InputValidator(maze, sourceCoordinates);

        try {
            validator.validate();
        } catch (MalformedMazeException | InvalidMazeCoordinatesException |
                InvalidMazeStartPositionException | EmptyMazeException |
                InvalidMazeValuesException ex) {
            logger.fatal("Input validation of Dijkstra Maze Solver has failed! Terminating.");

            throw ex;
        }

        return solve(maze, sourceCoordinates);
    }

    /**
     * Abstract solve function, which can be implemented by any algorithm.
     *
     * @throws InescapableMazeException By default, all implementing algorithms should
     * throw in certain cases.
     * */
    protected abstract int solve(final int[][] maze, final Point sourceCoordinates) throws InescapableMazeException;
}
