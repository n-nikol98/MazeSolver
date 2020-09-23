package com.mazesolver.dijkstra;

import com.mazesolver.MazeSolver;
import com.mazesolver.exception.InescapableMazeException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("Convert2Diamond")
public final class DijkstraMazeSolver extends MazeSolver {

    private static final Logger logger = Logger.getLogger(DijkstraMazeSolver.class);

    /**
     *
     * Solves a Rectangular (Square) Maze with Weights for each cell via the Dijkstra Graph Algorithm.
     * The point is to find the lowest cost path to a Maze exit (a border cell).
     *
     * @param maze - A <b>rectangular</b> 2D Maze of non-negative Integers.
     *             0 - Denotes a space, which can not be traversed.
     *             >0 - Denotes a space, which can be traversed, however a certain cost is incurred
     *
     * @param sourceCoordinates - X & Y axis coordinates for the Maze. Must be positive and within array bounds.
     *
     * @return The value of the lowest cost path to a Maze exit. In case of NoPath, an exception is thrown
     * and no result is returned.
     * */
    protected int solve(final int[][] maze, final Point sourceCoordinates) throws InescapableMazeException {

        logger.info("Initialize Dijkstra Maze Solver.");

        final Point expandedSourceCoordinates =
                new Point( ((int) (sourceCoordinates.getX() + 1)),
                        ((int) (sourceCoordinates.getY() + 1)));

        logger.info("Original Maze dimensions: X(" +
                maze[0].length + "), Y(" +
                maze.length + ").");

        final int[][] expanded_maze = expand_maze(maze);
        logger.info("Expanded Maze internally by adding an additional layer of cells, in order to " +
                "properly integrate it with Dijkstra's Algorithm. " +
                "ALL FURTHER COORDINATES IN THIS DIJKSTRA ALGORITHM (& RELATED CLASSES ) " +
                "LOG ARE FOR THE EXPANDED MAZE.");

        //Construct a Connect Weighted Graph from the Expanded Maze and the Expanded Source Coordinates
        @SuppressWarnings("SpellCheckingInspection")
        final ConnectedWeightedGraphSelector CWGS = new ConnectedWeightedGraphSelector(expanded_maze,
                expandedSourceCoordinates);

        final WeightedGraph weightedGraph = CWGS.getConnectedWeightedGraph();


        //Simply parse a list of Vertexes from the weightedGraph, which are at the border of Expanded Maze
        logger.info("Get Target Vertexes (i.e. Vertexes which are at the border of the Maze) " +
                "from the generated Connected Weighted Graph.");
        final List<WeightedGraph.Vertex> targetVertexes =
                weightedGraph.getVertexes().stream().filter(vertex ->
                        getMazeBorderCoordinates(expanded_maze)
                                .stream().anyMatch(coordinates ->
                                coordinates.equals(vertex.getCoordinates())))
                        .collect(Collectors.toList());

        //If no target(border)Vertexes exist => Our Maze does not have a solution from the current start pos
        logger.info("Check if any Target Vertexes exist (i.e. if the Maze has exits from the " +
                "given start point). " + "If this list is empty, a fatal Exception will be thrown " +
                "and the Dijkstra Maze Solver will be terminated.");
        if (targetVertexes.isEmpty()) {
            logger.fatal("The input Maze does not have any exits from the given " +
                    "start point. Unable to proceed further. Terminating Dijkstra Maze " +
                    "Solver.");
            throw new InescapableMazeException(maze, sourceCoordinates);
        }

        logger.info("Passing Connected Weighted Graph to Dijkstra Algorithm, " +
                "in order to determine the shortest paths between the source Vertex " +
                "with coordinates: (" +
                (int) expandedSourceCoordinates.getX() + ", " +
                (int) expandedSourceCoordinates.getY() + ") ~in the expanded Maze~ and all other " +
                "Vertexes.");

        //Init a new DijkstraAlgorithm. Source Vertex is parsed via our expandedSourceCoordinates.
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        final DijkstraAlgorithm algorithm = new DijkstraAlgorithm(weightedGraph,
                weightedGraph.getVertexes().stream().filter(
                        vertex ->
                                vertex.getCoordinates().
                                        equals(expandedSourceCoordinates)).
                        findFirst().get());

        //The distances of all Vertexes to our sourceVertex and vice-versa
        final Map<WeightedGraph.Vertex, Integer> distancesFromSource
                = algorithm.getDistancesFromSource();

        //Parse only those distances, which are for targetVertexes
        final Map<WeightedGraph.Vertex, Integer> shortestDistancesToTargetVertexes = new HashMap<WeightedGraph.Vertex, Integer>();

        //The actual parsing
        //noinspection OptionalGetWithoutIsPresent
        targetVertexes.forEach(targetVertex ->
            shortestDistancesToTargetVertexes.put(targetVertex, distancesFromSource.get(
                    distancesFromSource.keySet()
                            .stream().filter(vertex ->
                            vertex.equals(targetVertex)).findFirst().get())));

        logger.info("Found " + shortestDistancesToTargetVertexes.size() +
                " shortest distances to target Vertexes (border cells in the expanded Maze):");

        //Log each targetVertex and the distance from the Source to it.
        //noinspection LambdaBodyCanBeCodeBlock
        shortestDistancesToTargetVertexes.forEach((targetVertex, shortestDistanceToTargetVertex) ->
                logger.info("Vertex: (" + (int) targetVertex.getCoordinates().getX() +
                        ", " + (int) targetVertex.getCoordinates().getY() +
                        "); Distance from Source Vertex: " + shortestDistanceToTargetVertex));

        //Get the absolute shortest distances from the source to a target vertex and return.
        final int shortestDistanceToTargetVertex = Collections.min(shortestDistancesToTargetVertexes.values());

        logger.info(shortestDistanceToTargetVertex + " is the absolute shortest distance " +
                "to a target Vertex (i.e. to a border cell). " +
                "Returning as a result of the Dijkstra Maze Solver.");

        return shortestDistanceToTargetVertex;
    }


    /**
     * Add a new outer layer of cells to the original Maze.
     * Used so that the Dijkstra Algorithm and ConnectedWeightedGraphSelector work
     * properly.
     *
     * @return A new Expanded Maze with an additional outer layer.
     *
     * The old Maze is not affected in any way.
     *
     * */
    private int[][] expand_maze(final int[][] maze) {

        int[][] maze_copy = Arrays.stream(maze).map(int[]::clone).toArray(int[][]::new);

        final int xAxisSize = maze[0].length;
        final int yAxisSize = maze.length;

        for (int i = 0; i < yAxisSize; i++)
            maze_copy[i] = ArrayUtils.add(ArrayUtils.insert(0, maze[i],
                    Integer.MAX_VALUE), Integer.MAX_VALUE);

        maze_copy = ArrayUtils.add(ArrayUtils.insert(0, maze_copy,
                Arrays.stream(Collections.nCopies(xAxisSize + 2, Integer.MAX_VALUE)
                        .toArray(new Integer[xAxisSize + 2])).mapToInt(Integer::intValue).toArray() ),
                Arrays.stream(Collections.nCopies(xAxisSize + 2, Integer.MAX_VALUE)
                        .toArray(new Integer[xAxisSize + 2])).mapToInt(Integer::intValue).toArray() );

        maze_copy[0][0] = -1;
        maze_copy[0][xAxisSize + 1] = -1;
        maze_copy[yAxisSize + 1][0] = -1;
        maze_copy[yAxisSize + 1][xAxisSize + 1] = -1;

        return maze_copy;
    }

    /**
     *
     * Utility function.
     *
     * @return A list of points, which are coordinates to
     * all of the outer layer cells of the expanded Maze
     * **/
    private List<Point> getMazeBorderCoordinates(final int[][] maze) {
        final List<Point> borderCoordinates = new ArrayList<Point>();
        final int xAxisSize = maze[0].length;
        final int yAxisSize = maze.length;

        IntStream.range(0, yAxisSize).forEach(yIndex -> {
            if (yIndex == 0 || yIndex == yAxisSize - 1)
                IntStream.range(0, xAxisSize).forEach(xIndex ->
                    borderCoordinates.add(new Point(xIndex, yIndex))
                );
            else {
                borderCoordinates.add(new Point(0, yIndex));
                borderCoordinates.add(new Point(xAxisSize - 1, yIndex));
            }
        });

        return borderCoordinates;
    }
}
