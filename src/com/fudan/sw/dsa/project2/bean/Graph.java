package com.fudan.sw.dsa.project2.bean;

import org.jetbrains.annotations.Contract;
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
        double distance = Double.MAX_VALUE;
        double currentDistance;
        Vertex nearestStation = vertices.get(0);
        for (Vertex currentStation : vertices) {
            currentDistance = calculateDistance(currentStation, address);
            if (currentDistance < distance) {
                distance = currentDistance;
                nearestStation = currentStation;
            }
        }
        time += distance * 12;
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

    private ArrayList<Vertex> getPath(Vertex startV, Vertex endV) {
        ArrayList<Vertex> path = new ArrayList<>();
        Dijkstra dijkstra = new Dijkstra();
        if (startV == endV)
            path.add(startV);
        else
            path = dijkstra.getPath(vertices, startV, endV);
        return path;
    }

    @Contract("null -> null")
    private ArrayList<Address> returnRoute(ArrayList<Vertex> path) {
        ArrayList<Address> route = new ArrayList<>();
        if (path == null)
            return null;
        for (Vertex vertex : path)
            route.add(new Address(vertex.getName(),
                    Double.toString(vertex.getLongitude()),
                    Double.toString(vertex.getLatitude())));
        return route;
    }

    public ArrayList<Address> shortestWalking(Address start, Address end) {
        time = 0;
        Vertex startV = searchForStation(start);
        Vertex endV = searchForStation(end);
        if (startV == null || endV == null)
            return null;
        ArrayList<Vertex> path = getPath(startV, endV);
        time += path.get(path.size() - 1).getDistance();
        return returnRoute(path);
    }

    public ArrayList<Address> shortestTime(Address start, Address end) {
        time = 0;
        int shortestTime = Integer.MAX_VALUE;
        ArrayList<Vertex> candidateStartStation = new ArrayList<>();
        ArrayList<Vertex> candidateEndStation = new ArrayList<>();
        ArrayList<Vertex> path;
        ArrayList<Vertex> finalPath = null;

        for (Vertex vertex : vertices) {
            if (calculateDistance(vertex, start) < 5)
                candidateStartStation.add(vertex);
            if (calculateDistance(vertex, end) < 5)
                candidateEndStation.add(vertex);
        }
        if (candidateStartStation.size() == 0 || candidateEndStation.size() == 0)
            return shortestWalking(start, end);
        for (Vertex vertexS : candidateStartStation)
            for (Vertex vertexE : candidateEndStation) {
                time = 0;
                path = getPath(vertexS, vertexE);
                time = path.get(path.size() - 1).getDistance() +
                        (int) ((calculateDistance(vertexS, start) * 12)) +
                        (int) (calculateDistance(vertexE, end) * 12);
                if (time < shortestTime) {
                    finalPath = path;
                    shortestTime = time;
                }
            }
        time = shortestTime;
        return returnRoute(finalPath);
    }
}
