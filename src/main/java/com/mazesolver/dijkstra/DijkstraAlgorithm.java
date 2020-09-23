package com.mazesolver.dijkstra;

import org.apache.log4j.Logger;

import java.util.*;
import java.util.List;

/**
 * Several Important notes about this class:
 *
 * - It's purpose is to only run the DijkstraAlgorithm on a Weighted NON-DIRECTIONAL Graph, using
 * a given sourceVertex.
 *
 *
 * - Afterwards a map of distances to Vertexes from a a given sourceVertex is available to the user.
 *
 * No further comments about how the actual algorithm works will be made, as it is publicly available info.
 *
 * */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "Convert2Diamond"})
final class DijkstraAlgorithm {

    private static final Logger logger = Logger.getLogger(DijkstraAlgorithm.class);

    private final List<WeightedGraph.WeightedEdge> edges;
    private final Set<WeightedGraph.Vertex> settledVertexes;
    private final Set<WeightedGraph.Vertex> unSettledVertexes;
    private final Map<WeightedGraph.Vertex, WeightedGraph.Vertex> predecessors;
    private final Map<WeightedGraph.Vertex, Integer> distances;

    DijkstraAlgorithm(final WeightedGraph graph, final WeightedGraph.Vertex sourceVertex) {

        this.edges = graph.getEdges();
        settledVertexes = new HashSet<WeightedGraph.Vertex>();
        unSettledVertexes = new HashSet<WeightedGraph.Vertex>();
        distances = new HashMap<WeightedGraph.Vertex, Integer>();
        predecessors = new HashMap<WeightedGraph.Vertex, WeightedGraph.Vertex>();

        logger.info("Execute Dijkstra Algorithm for a connected weighted graph with: " +
                graph.getVertexes().size() + " Vertexes & " + edges.size() + " Edges.");
        execute(sourceVertex);
    }

    private void execute(final WeightedGraph.Vertex source) {
        distances.put(source, 0);
        unSettledVertexes.add(source);
        while (unSettledVertexes.size() > 0) {
            final WeightedGraph.Vertex vertex = getMinimum(unSettledVertexes);
            settledVertexes.add(vertex);
            unSettledVertexes.remove(vertex);
            findMinimalDistances(vertex);

            logger.info("Settled Vertex with coordinates: (" +
                    (int) vertex.getCoordinates().getX() + ", " + (int) vertex.getCoordinates().getY() +
                    "). Distance from Source Vertex: " + getShortestDistance(vertex) + ".");
        }
    }

    private void findMinimalDistances(final WeightedGraph.Vertex vertex) {
        final List<WeightedGraph.Vertex> adjacentVertexes = getNeighbors(vertex);

        for (WeightedGraph.Vertex target : adjacentVertexes) {
            if (getShortestDistance(target) > getShortestDistance(vertex)
                    + getDistance(vertex, target)) {
                distances.put(target, getShortestDistance(vertex)
                        + getDistance(vertex, target));
                predecessors.put(target, vertex);
                unSettledVertexes.add(target);
            }
        }

    }

    private int getDistance(final WeightedGraph.Vertex vertex, final WeightedGraph.Vertex target) {
        for (WeightedGraph.WeightedEdge edge : edges) {
            if ((edge.getFirstVertex().equals(vertex)
                    && edge.getSecondVertex().equals(target)) ||
                    (edge.getSecondVertex().equals(vertex)
                        && edge.getFirstVertex().equals(target)))
                return edge.getWeight();
        }

        return -1;
    }

    private List<WeightedGraph.Vertex> getNeighbors(final WeightedGraph.Vertex vertex) {
        final List<WeightedGraph.Vertex> neighbors = new ArrayList<WeightedGraph.Vertex>();

        for (WeightedGraph.WeightedEdge edge : edges) {
            if (edge.getFirstVertex().equals(vertex)
                    && !isSettled(edge.getSecondVertex()))
                neighbors.add(edge.getSecondVertex());
            else if (edge.getSecondVertex().equals(vertex)
                    && !isSettled(edge.getFirstVertex()))
                neighbors.add(edge.getFirstVertex());
        }

        return neighbors;
    }

    private WeightedGraph.Vertex getMinimum(final Set<WeightedGraph.Vertex> vertexes) {
        WeightedGraph.Vertex minimum = null;

        for (WeightedGraph.Vertex vertex : vertexes) {
            if (minimum == null)
                minimum = vertex;
            else if (getShortestDistance(vertex) < getShortestDistance(minimum))
                    minimum = vertex;
        }

        return minimum;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isSettled(final WeightedGraph.Vertex vertex) {
        return settledVertexes.contains(vertex);
    }

    private int getShortestDistance(WeightedGraph.Vertex destination) {
        final Integer d = distances.get(destination);

        return Objects.requireNonNullElse(d, Integer.MAX_VALUE);
    }

    /**
     * @return A list of distances to Vertexes from a given sourceVertex in a connected
     * Weighted NON-DIRECTIONAL Graph.
     * */
    Map<WeightedGraph.Vertex, Integer> getDistancesFromSource() {
        return new HashMap<WeightedGraph.Vertex, Integer>(distances);
    }
}
