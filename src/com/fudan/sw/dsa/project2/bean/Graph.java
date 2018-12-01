package com.fudan.sw.dsa.project2.bean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Graph {
    public ArrayList<Vertex> vertices = new ArrayList<>();
    public int time;

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

    public ArrayList<Address> getPath(String start, String end) {
        ArrayList<Vertex> path = getPath1(start, end);
        ArrayList<Address> route = new ArrayList<>();
        if (path == null)
            return null;
        for (Vertex vertex : path)
            route.add(new Address(vertex.getName(),
                    Double.toString(vertex.getLatitude()),
                    Double.toString(vertex.getLongitude())));
        time = path.get(path.size() - 1).getDistance();
        return route;
    }
}
