package com.fudan.sw.dsa.project2.bean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * The abstraction of the stations in the map, here as a vertex in the graph.
 *
 * @author Wang, Chen
 * @see Graph
 */
public class Vertex implements Comparable<Vertex> {

    /**
     * The Chinese name of the station
     */
    private String name;

    /**
     * All the edges connecting the vertex
     * In the map, it is all the lines that connects the station
     * Note that An edge connects only two vertices and does not span longer
     * The edges in the different direction are different edges
     * The same line that goes two directions at one station are denoted by two
     * different edges
     */
    private ArrayList<Edge> edges = new ArrayList<>();

    /**
     * All the edges that can be reached from the current station within one edge
     */
    private ArrayList<Vertex> adjacentVertices = new ArrayList<>();

    /**
     * This field is used in the dijkstra algorithm to denote the distance from the
     * source to this vertex.
     * Note that the distance of the source is 0
     */
    private int distance;

    /**
     * This field is used in the dijkstra algorithm to trace the path
     */
    private Vertex preVertex;

    /**
     * The location info of the station
     */
    private double longitude, latitude;

    public Vertex(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>This implementation ensures
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} throws an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementation also ensures that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementation ensures that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>Note: this class has a natural ordering that is inconsistent with equals.
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(@NotNull Vertex o) {
        return this.distance - o.distance;
    }

    double getLongitude() {
        return longitude;
    }

    double getLatitude() {
        return latitude;
    }

    String getName() {
        return name;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    int getDistance() {
        return distance;
    }

    void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * This method tests whether two vertices are adjacent. If not,
     * the method returns null, otherwise it will return the edge that
     * connects the two vertices.
     *
     * @param vertex1 The other vertex queried
     * @return The edge that connects the two vertices
     */
    Edge getEdge(Vertex vertex1) {
        for (Edge edge : edges)
            if (edge.getOtherVertex(this) == vertex1)
                return edge;
        return null;
    }

    Vertex getPreVertex() {
        return preVertex;
    }

    void setPreVertex(Vertex vertex) {
        preVertex = vertex;
    }

    public ArrayList<Vertex> getAdjacentVertices() {
        return adjacentVertices;
    }
}
