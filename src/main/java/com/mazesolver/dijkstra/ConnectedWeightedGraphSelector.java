package com.mazesolver.dijkstra;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * Provides a Connected Weighted Graph, which includes in itself a Vertex
 * with the original coordinates, which were passed to DijkstraMazeSolver.
 *
 * AN EXPANDED Maze, as per the DijkstraMazeSolver method must be passed to this class, else
 * it will not work.
 *
 * Used a modified version of the Flood Fill algorithm to build the Graph.
 *
 * */
@SuppressWarnings("Convert2Diamond")
final class ConnectedWeightedGraphSelector {

    private final static Logger logger = Logger.getLogger(ConnectedWeightedGraphSelector.class);

    private final int[][] maze; //Hold an expanded maze
    private final List<WeightedGraph.Vertex> selectedVertexes; //Stores Vertexes, which have already been FloodSelected
    private final List<WeightedGraph.WeightedEdge> selectedEdges; //Same as Vertexes

    private final int xAxisSize; //For convenience
    private final int yAxisSize; //For convenience

    private final WeightedGraph connectedWeightedGraph;

    /**
     *
     * @param maze Takes an EXPANDED maze. Caution is advised.
     * @param sourceCoordinates Should also take EXPANDEDsourceCoordinates.
     *
     * */
    ConnectedWeightedGraphSelector(final int[][] maze, final Point sourceCoordinates) {
        this.maze = maze;
        this.xAxisSize = maze[0].length;
        this.yAxisSize = maze.length;

        this.selectedVertexes = new ArrayList<WeightedGraph.Vertex>();
        this.selectedEdges = new ArrayList<WeightedGraph.WeightedEdge>();

        logger.info("Begin Flood selection of Vertexes and Edges from Maze with " +
                "dimensions: X(" + xAxisSize + "), Y(" +
                yAxisSize + ").");
        floodSelect(null, sourceCoordinates);

        //After the floodSelect is completed, actually construct a new Graph.
        connectedWeightedGraph = new WeightedGraph(selectedVertexes, selectedEdges);
        logger.info("Connected Weighted Graph generated from Maze with " +
                "dimensions: X(" + xAxisSize + "), Y(" +
                yAxisSize + ") via Flood Select Algorithm.");
    }

    /**
     *
     * Recursive floodSelect function.
     * Based on the Flood Fill algorithm.
     *
     * @param previousCoordinates - A point of the previous cell, which was explored.
     *                            Used to create Edges. Is NULL when we first access the func.
     * @param coordinatesToExplore - The cell, which we will attempt to FloodSelect.
     *
     * */
    @SuppressWarnings("SimplifiableConditionalExpression")
    private void floodSelect(final Point previousCoordinates, final Point coordinatesToExplore) {

        /*
        * Firstly, check if we are in the bounds of the Expanded Maze.
        * Secondly, if we are, make sure that we are not traversing the edges
        * of the Expanded Maze. The outer layer vertexes are simply helpers, so that
        * a WeightedEdge may be created between the actual outer Vertex and it.
        * THEY SHOULD NOT BE TRAVERSED, AS THIS WILL BREAK THE ALGORITHM.
        *
        * */
        if (coordinatesToExplore.getX() < 0 ||
                coordinatesToExplore.getX() >= xAxisSize ||
                coordinatesToExplore.getY() < 0 ||
                coordinatesToExplore.getY() >= yAxisSize ||
                previousCoordinates != null ?
                (previousCoordinates.getX() == 0 ||
                        previousCoordinates.getX() == xAxisSize - 1 ||
                        previousCoordinates.getY() == 0 ||
                        previousCoordinates.getY() == yAxisSize - 1) :
                false)
            return;
        //Also check that we haven't been in this cell before and that it is traversable (e.g. it has a posotive value).
        else if(selectedVertexes.stream().anyMatch(vertex ->
                vertex.getCoordinates().equals(coordinatesToExplore)) ||
                maze[(int) coordinatesToExplore.getY()]
                        [(int) coordinatesToExplore.getX()] <= 0)
            return;

        //If all is good, add a new Vertex to our Graph.
        selectedVertexes.add(new WeightedGraph.Vertex(coordinatesToExplore));
        logger.info("Selected Vertex with coordinates: (" +
                (int) coordinatesToExplore.getX() + ", " +
                (int) coordinatesToExplore.getY() + ") " +
                "from Maze.");

        /*If we were previously in another Vertex, add an edge between it and the current Vertex.
         *
         * NOTE: WeightedEdges are not used as directional in DijkstraAlgorithm.
         *
         */
        if(previousCoordinates != null) {
            selectedEdges.add(new WeightedGraph.WeightedEdge(new WeightedGraph.Vertex(previousCoordinates),
                    new WeightedGraph.Vertex(coordinatesToExplore),
                    maze[(int) previousCoordinates.getY()]
                            [(int) previousCoordinates.getX()]));

            logger.info("Selected Edge between Vertexes with coordinates: (" +
                    (int) previousCoordinates.getX() + ", " +
                    (int) previousCoordinates.getY() + ") " +
                    "and: (" + (int) coordinatesToExplore.getX() +
                    ", " + (int) coordinatesToExplore.getY() + ")" +
                    "from Maze. Edge weight: " +
                    maze[(int) previousCoordinates.getY()]
                            [(int) previousCoordinates.getX()]);
        }

        //Recurse through neighboring cells.
        floodSelect(coordinatesToExplore,
                new Point((int) coordinatesToExplore.getX() + 1,
                        (int) coordinatesToExplore.getY()));
        floodSelect(coordinatesToExplore,
                new Point((int) coordinatesToExplore.getX() - 1,
                        (int) coordinatesToExplore.getY()));
        floodSelect(coordinatesToExplore,
                new Point((int) coordinatesToExplore.getX(),
                        (int) coordinatesToExplore.getY() + 1));
        floodSelect(coordinatesToExplore,
                new Point((int) coordinatesToExplore.getX(),
                        (int) coordinatesToExplore.getY() - 1));
    }

    /**
     * @return A referance to the Connected Weighted Graph, which was created in the constructor.
     * */
    WeightedGraph getConnectedWeightedGraph() {
        return connectedWeightedGraph;
    }
}
