package com.fudan.sw.dsa.project2.bean;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * The entrance of the shortest path searching algorithm and it stores the vertices in the
 * graph. It contains the graph initialization method that creates new vertices and edges
 * storing specified data, the get path methods that correspond to the demands in the
 * project and a number of utility functions that makes code reuse more widespread.
 *
 * @author Wang, Chen
 */
public class Graph {

    /**
     * All the vertices, i.e. the subway stations are stored in this variable.
     */
    public static ArrayList<Vertex> vertices = new ArrayList<>();

    /**
     * The total consuming time of the route, including the walking time and subway running
     * time.
     *
     * @implSpec It is updated both in and outside of this class and so does query.
     */
    public int time;

    public Graph() {
    }

    /**
     * Calculates the times of transfer by comparing the name of the adjacent edges.
     * Transfer time is addes by 1 if the adjacent edges have different names.
     *
     * @param path the calculated path
     */
    private static int transferTime(@NotNull ArrayList<Vertex> path) {
        int transferTime = 0;
        for (int i = 2; i < path.size(); i++)
            if (!path.get(i).getEdge(path.get(i - 1)).getLine().equals(path.get(i - 1).getEdge(path.get(i - 2)).getLine()))
                transferTime++;
        return transferTime;
    }

    /**
     * Iterates all the vertices so as to find the vertices that is close to the requested
     * address. The straight line distance between the address and the station is calculated.
     *
     * @param address The start point or the end point
     */
    private static ArrayList<Vertex> getCandidates(Address address) {
        ArrayList<Vertex> candidate = new ArrayList<>();
        for (Vertex vertex : vertices)
            if (calculateDistance(vertex, address) < 4)
                candidate.add(vertex);
        return candidate;
    }

    /**
     * Get the distance between two points on the map.
     * Tht algorithm has taken the earth is not flat into consideration
     *
     * @param station The subway station
     * @param address The start address or the end address.
     */
    private static double calculateDistance(@NotNull Vertex station, @NotNull Address address) {
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

    @Contract("null -> null")
    private static ArrayList<Address> returnRoute(ArrayList<Vertex> path) {
        ArrayList<Address> route = new ArrayList<>();
        if (path == null)
            return null;
        for (Vertex vertex : path)
            route.add(new Address(vertex.getName(),
                    Double.toString(vertex.getLongitude()),
                    Double.toString(vertex.getLatitude())));
        return route;
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
        ArrayList<Vertex> path;
        ArrayList<Vertex> candidateStartStation = getCandidates(start);
        ArrayList<Vertex> candidateEndStation = getCandidates(end);
        ArrayList<Vertex> finalPath = null;
        if (candidateStartStation.size() == 0 || candidateEndStation.size() == 0)
            return shortestWalking(start, end);
        for (Vertex vertexS : candidateStartStation)
            for (Vertex vertexE : candidateEndStation) {
                path = getPathAndTime(vertexS, vertexE, start, end);
                if (time < shortestTime) {
                    finalPath = path;
                    shortestTime = time;
                }
            }
        time = shortestTime;
        return returnRoute(finalPath);
    }

    public ArrayList<Address> lessTransfer(Address start, Address end) {
        time = 0;
        int leastTransferNumber = Integer.MAX_VALUE;
        int shortestTime = Integer.MAX_VALUE;
        ArrayList<Vertex> finalPath = null;
        ArrayList<Vertex> candidateStartStation = getCandidates(start);
        ArrayList<Vertex> candidateEndStation = getCandidates(end);
        if (candidateStartStation.size() == 0 || candidateEndStation.size() == 0)
            return shortestWalking(start, end);
        for (Vertex vertexS : candidateStartStation)
            for (Vertex vertexE : candidateEndStation) {
                ArrayList<Vertex> path = getPathAndTime(vertexS, vertexE, start, end);
                int transferTime = transferTime(path);
                if (transferTime < leastTransferNumber ||
                        transferTime == leastTransferNumber && time < shortestTime) {
                    finalPath = path;
                    leastTransferNumber = transferTime;
                    shortestTime = time;
                }
            }
        time = shortestTime;
        return returnRoute(finalPath);
    }

    private ArrayList<Vertex> getPathAndTime(Vertex vertexS, Vertex vertexE, Address start, Address end) {
        time = 0;
        ArrayList<Vertex> path = getPath(vertexS, vertexE);
        time = path.get(path.size() - 1).getDistance() +
                (int) ((calculateDistance(vertexS, start) * 12)) +
                (int) (calculateDistance(vertexE, end) * 12);
        return path;
    }
}
