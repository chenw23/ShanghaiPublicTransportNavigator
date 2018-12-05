package com.fudan.sw.dsa.project2.bean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Stack;

/**
 * The implementation of the Dijkstra algorithm, the  way of implementation is
 * according to Introduction to algorithms 3e.
 *
 * @author Wang, Chen
 */
class Dijkstra {
    /**
     * The main method of the Dijkstra algorithm, which first initializes the vertices
     * and then maintains a minimum heap where the vertices are kept.
     * The loop relaxes each vertex's adjacent vertices from the source in order,
     * as a mean of search for path, and aborts when all the vertices in the minimum
     * heap have relaxed other vertices.
     *
     * @param vertices All the vertices in the graph to implement the sort on;
     * @param start    The source of the graph;
     * @param end      The sink of the graph.
     */
    private void DIJKSTRA(ArrayList<Vertex> vertices, Vertex start, Vertex end) {
        initializeSingleSource(vertices, start);
        MinHeap minHeap = new MinHeap(vertices);
        while (!minHeap.isEmpty()) {
            Vertex u = minHeap.extractMin();
            if (u == end)
                break;
            for (Vertex vertex : u.getAdjacentVertices())
                relax(u, vertex);
            minHeap.buildMinHeap();
        }
    }

    /**
     * A component of the dijkstra algorithm and it is only called at the beginning of
     * the algorithm. It sets all the vertex's distance to positive infinite with the
     * exception of the start point, set to 0.
     *
     * @param graph All the vertices to perform operation on;
     * @param start The source of the graph.
     */
    private void initializeSingleSource(@NotNull ArrayList<Vertex> graph, Vertex start) {
        for (Vertex vertex : graph)
            vertex.setDistance(Integer.MAX_VALUE);
        start.setDistance(0);
    }


    /**
     * The key component of the dijkstra algorithm, which relaxes the distance of the current
     * path. The relaxation is the process of decreasing the distance of the vertex if
     * a shorter path is found. So in this method the current searching path is firstly
     * compared to see whether the path can make getting to the {@code now} vertex shorter.
     * If this is true, the distance is relaxed to the current value and it does nothing
     * if false.
     *
     * @param preVertex the previous vertex that relaxes the current vertex,
     * @param now       the vertex that is being relaxed now.
     */
    private void relax(@NotNull Vertex preVertex, Vertex now) {
        if (preVertex.getDistance() != Integer.MAX_VALUE &&
                now.getDistance() > preVertex.getDistance() + preVertex.getEdge(now).getWeight()) {
            now.setDistance(preVertex.getDistance() + preVertex.getEdge(now).getWeight());
            now.setPreVertex(preVertex);
        }
    }

    /**
     * The auxiliary method that the work after dijkstra algorithm, it goes along the path indicated
     * by the {@code preVertex} attribute and exports the vertex as a path from the source of the graph
     * to the sink.
     *
     * @param vertices1 All the vertices of the graph,
     * @param start     The source of the graph,
     * @param end       The sink of the graph.
     */

    ArrayList<Vertex> getPath(ArrayList<Vertex> vertices1, Vertex start, Vertex end) {
        DIJKSTRA(vertices1, start, end);
        ArrayList<Vertex> path = new ArrayList<>();
        Stack<Vertex> vertices = new Stack<>();
        vertices.push(end);
        while (end.getPreVertex() != null && end.getPreVertex() != start) {
            end = end.getPreVertex();
            vertices.push(end);
        }
        if (end.getPreVertex() == null)
            return null;
        else {
            path.add(start);
            while (!vertices.empty())
                path.add(vertices.pop());
            return path;
        }
    }
}
