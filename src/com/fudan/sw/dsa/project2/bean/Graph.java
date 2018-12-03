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
        Vertex newStation = getStation(stationName);
        if (newStation == null) {
            newStation = new Vertex(stationName, latitude, longitude);
            vertices.add(newStation);
        }
        preStation.getAdjacentVertices().add(newStation);
        newStation.getAdjacentVertices().add(preStation);
        Edge edge = new Edge(preStation, newStation, name, getTime(time1, time2));
        preStation.getEdges().add(edge);
        newStation.getEdges().add(edge);
        return newStation;
    }

    @Nullable
    public Vertex getStation(String address) {
        for (Vertex vertex : vertices)
            if (vertex.getName().equals(address))
                return vertex;
        return null;
    }

    private Vertex searchForStation(Address address) {
        Vertex nearestStation = vertices.get(0);
        for (Vertex currentStation : vertices)
            if (calculateDistance(currentStation, address) < calculateDistance(nearestStation, address))
                nearestStation = currentStation;
        return nearestStation;
    }

    private double calculateDistance(@NotNull Vertex station, @NotNull Address address) {
        double startLatitude = station.getLatitude(),
                startLongitude = station.getLongitude(),
                endLatitude = address.getLatitude(),
                endLongitude = address.getLongitude();
        double radLat1 = Math.toRadians(startLatitude);
        double radLat2 = Math.toRadians(endLatitude);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(startLongitude) - Math.toRadians(endLongitude);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137;
        s = Math.round(s * 10000.0) / 10000.0;
        return s;
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

    public ArrayList<Address> getPath(Address start, Address end) {
        Dijkstra dijkstra = new Dijkstra();
        Vertex startV = searchForStation(start);
        Vertex endV = searchForStation(end);
        if (startV == null || endV == null)
            return null;
        ArrayList<Vertex> path = new ArrayList<>();
        if (startV == endV)
            path.add(startV);
        else
            path = dijkstra.getPath(vertices, startV, endV);
        ArrayList<Address> route = new ArrayList<>();
        if (path == null)
            return null;
        for (Vertex vertex : path)
            route.add(new Address(vertex.getName(),
                    Double.toString(vertex.getLongitude()),
                    Double.toString(vertex.getLatitude())));
        time = path.get(path.size() - 1).getDistance();
        return route;
    }
}
