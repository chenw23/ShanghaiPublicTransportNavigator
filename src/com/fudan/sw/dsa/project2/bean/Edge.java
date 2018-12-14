package com.fudan.sw.dsa.project2.bean;

/**
 * The abstraction of the edges in the graph.
 * The weight indicates the running totalTime of the subway between two stations, the two vertices
 * variable shows which vertex it is connecting and the line String is the name of the edge.
 *
 * @author Wang, Chen
 */
public class Edge {

    /**
     * the start station of the edge
     */
    private Vertex vertex1;

    /**
     * the destination of the edge
     */
    private Vertex vertex2;

    /**
     * the name of the edge
     */
    private String line;

    /**
     * the running totalTime of the subway between two stations, i.e. vertex1
     * and vertex2
     */
    private int weight;

    /**
     * Constructor of the class, initiating all the four variables.
     *
     * @param vertex1 the start station of the edge
     * @param vertex2 the destination of the edge
     * @param line    the name of the edge
     * @param weight  the running totalTime of the subway between two stations, i.e. vertex1
     *                and vertex2
     */
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

    /**
     * Get the vertex at the other end of this edge of the requesting vertex
     *
     * @param vertex The other vertex with which the returned edge will connect with
     *               the current vertex
     * @return {@code null} if the edge does not connect the requesting vertex
     * the other vertex if the edge connects the edge and the requesting vertex
     */
    Vertex getOtherVertex(Vertex vertex) {
        if (vertex == vertex1)
            return vertex2;
        else if (vertex == vertex2)
            return vertex1;
        else
            return null;
    }
}