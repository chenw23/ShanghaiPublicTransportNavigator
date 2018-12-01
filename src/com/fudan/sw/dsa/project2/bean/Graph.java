package com.fudan.sw.dsa.project2.bean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Vertex> vertices = new ArrayList<>();

    public Graph() {
    }

    public Vertex addVertex(Vertex preStation, String stationName, String name,
                            String time1, String time2, double latitude, double longitude) {
        Vertex newStation = exist(stationName);
        if (newStation == null) {
            newStation = new Vertex(stationName, latitude, longitude);
            vertices.add(newStation);
        }
        preStation.getVertices().add(newStation);
        newStation.getVertices().add(preStation);
        Edge edge = new Edge(preStation, newStation, name, getTime(time1, time2));
        preStation.getEdges().add(edge);
        newStation.getEdges().add(edge);
        return newStation;
    }

    @Nullable
    public Vertex exist(String station) {
        for (Vertex vertex : vertices)
            if (vertex.getName().equals(station))
                return vertex;
        return null;
    }

    private int getTime(@NotNull String start, @NotNull String end) {
        int length1 = start.length();
        int length2 = end.length();
        int minute = (int) end.charAt(length2 - 1) - (int) start.charAt(length1 - 1);
        int ten = (int) end.charAt(length2 - 2) - (int) start.charAt(length1 - 2);
        int hour = (int) end.charAt(length2 - 4) - (int) start.charAt(length1 - 4);
        hour = hour >= 0 ? hour : hour + 4;
        return hour * 60 + ten * 10 + minute;
    }

    @Nullable
    private ArrayList<Vertex> getPath1(String start, String end) {
        Dijkstra dijkstra = new Dijkstra();
        Vertex startV = exist(start);
        Vertex endV = exist(end);
        if (startV == null || endV == null)
            return null;
        ArrayList<Vertex> path = new ArrayList<>();
        if (startV == endV) {
            path.add(startV);
            return path;
        }
        return dijkstra.getPath(vertices, startV, endV);
    }

    private ArrayList<String> getPath(String start, String end) {
        ArrayList<Vertex> path = getPath1(start, end);
        ArrayList<String> path1 = new ArrayList<>();
        if (path == null) {
            path1.add("The station you have requested does not exist");
            return path1;
        }
        Vertex preVertex = path.get(0);
        Vertex vertex;
        String line = "";
        for (int i = 1; i < path.size(); i++) {
            vertex = path.get(i);
            if (!vertex.getEdge(preVertex).getLine().equals(line)) {
                line = vertex.getEdge(preVertex).getLine();
                path1.add(preVertex.getName());
                path1.add("-");
                path1.add(line);
                path1.add("-");
            }
            preVertex = vertex;
        }
        path1.add(end);
        return path1;
    }

    ArrayList<String> getPath(String[] station) {
        ArrayList<String> path = new ArrayList<>();
        for (String value : station)
            if (exist(value) == null) {
                path.add("The station you have requested does not exist");
                return path;
            }

        String pre = station[0];
        ArrayList<String> path1;
        int i;
        for (i = 1; i < station.length; i++) {
            path1 = getPath(pre, station[i]);
            path1.remove(path1.size() - 1);
            path.addAll(path1);
            pre = station[i];
        }
        path.add(station[i - 1]);
        return path;
    }
}
