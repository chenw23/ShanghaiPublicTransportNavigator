package com.fudan.sw.dsa.project2.bean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The abstraction of the stations in the map, here as a vertex in the graph.
 *
 * @author Wang, Chen
 * @see Graph
 */
public class Vertex implements Comparable<Vertex> {

    Color color;
    int finishingTime;
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
    private double addressDistance;

    public Vertex(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    void setAddressDistance(double addressDistance) {
        this.addressDistance = addressDistance;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>Note: this class has a natural ordering that is inconsistent with equals.
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

    public static class distanceComparator implements Comparator<Vertex> {
        @Override
        public int compare(Vertex o1, Vertex o2) {
            return (int) ((o1.addressDistance - o2.addressDistance) * 1000);
        }
    }
}