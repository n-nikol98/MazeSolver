package com.mazesolver.dijkstra;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Convert2Diamond")
final class WeightedGraph {

    /**
     * A model for a Vertex.
     * It is defined only by the coordinates of a cell in the Expanded Maze.
     * E
     *
     * public boolean equals(Object obj) is Overridden for convenience, in order to assure that
     * everything is checked properly and not based on Object references, which caused some issues
     * in DijkstraAlgorithm.
     * */
    static final class Vertex {

        private final Point coordinates;

        Vertex(final Point coordinates) {
            this.coordinates = coordinates;
        }

        Point getCoordinates() {
            return new Point(coordinates);
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(Object obj) {
            return coordinates.equals(((Vertex) obj).getCoordinates());
        }
    }

    /**
     * A model for a WeightedEdge.
     *
     * DISCLAIMER: These are NON-DIRECTION Edges.
     *
     * Simply holds a source & destination (both interchangeable) Vertex, as well as a weight,
     * as defined in the Maze & the Expanded Maze.
     *
     * */
    static final class WeightedEdge {

        private final Vertex firstVertex;
        private final Vertex secondVertex;
        private final int weight;

        WeightedEdge(final Vertex firstVertex, final Vertex secondVertex, final int weight) {
            this.firstVertex = firstVertex;
            this.secondVertex = secondVertex;
            this.weight = weight;
        }

        Vertex getFirstVertex() {
            return firstVertex;
        }

        Vertex getSecondVertex() {
            return secondVertex;
        }

        int getWeight() {
            return weight;
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(Object obj) {
            final WeightedEdge edge = (WeightedEdge) obj;

            return firstVertex.equals(edge.firstVertex) &&
                    secondVertex.equals(edge.secondVertex) &&
                    weight == edge.weight;
        }
    }

    /**
     * Our WeightedGraph is very simple too.
     * All it does is encapsulate two lists of the above-described Vertex & Edge models.
     *
     * */

    private final List<Vertex> vertexes;
    private final List<WeightedEdge> edges;

    WeightedGraph(final List<Vertex> vertexes, final List<WeightedEdge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    List<Vertex> getVertexes() {
        return new ArrayList<Vertex>(vertexes);
    }

    List<WeightedEdge> getEdges() {
        return new ArrayList<WeightedEdge>(edges);
    }

}
