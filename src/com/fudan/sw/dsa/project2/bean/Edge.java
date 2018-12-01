package com.fudan.sw.dsa.project2.bean;

public class Edge {
    private Vertex vertex1;
    private Vertex vertex2;
    private String line;
    private int weight;

    public Edge(Vertex vertex1, Vertex vertex2, String line, int weight) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.line = line;
        this.weight = weight;
    }

    String getLine() {
        return line;
    }

    int getWeight() {
        return weight;
    }

    Vertex getOtherVertex(Vertex vertex) {
        if (vertex == vertex1)
            return vertex2;
        else if (vertex == vertex2)
            return vertex1;
        else
            return null;
    }
}
