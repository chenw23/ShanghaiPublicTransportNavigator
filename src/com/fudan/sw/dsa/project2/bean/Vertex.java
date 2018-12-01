package com.fudan.sw.dsa.project2.bean;

import java.util.ArrayList;

public class Vertex {
    private String name;
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private int distance;
    private Vertex preVertex;
    private double longitude, latitude;

    double getLongitude() {
        return longitude;
    }

    double getLatitude() {
        return latitude;
    }

    public Vertex(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }
}
