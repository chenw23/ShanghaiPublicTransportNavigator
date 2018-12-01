package com.fudan.sw.dsa.project2.bean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Stack;

class Dijkstra {
    private void DIJKSTRA(ArrayList<Vertex> vertices, Vertex start, Vertex end) {
        initializeSingleSource(vertices, start);
        MinHeap minHeap = new MinHeap(vertices);
        while (!minHeap.isEmpty()) {
            Vertex u = minHeap.extractMin();
            if (u == end)
                break;
            for (Vertex vertex : u.getVertices())
                relax(u, vertex);
            minHeap.buildMinHeap();
        }
    }

    private void initializeSingleSource(@NotNull ArrayList<Vertex> graph, Vertex start) {
        for (Vertex vertex : graph)
            vertex.setDistance(Integer.MAX_VALUE);
        start.setDistance(0);
    }

    private void relax(@NotNull Vertex preVertex, Vertex now) {
        if (preVertex.getDistance() != Integer.MAX_VALUE &&
                now.getDistance() > preVertex.getDistance() + preVertex.getEdge(now).getWeight()) {
            now.setDistance(preVertex.getDistance() + preVertex.getEdge(now).getWeight());
            now.setPreVertex(preVertex);
        }
    }

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
