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
     * Note that it is updated both in and outside of this class and so does query.
     */
    public int time;

    public Graph() {
    }

    /**
     * Calculates the times of transfer by comparing the name of the adjacent edges.
     * Transfer time is added by 1 if the adjacent edges have different names.
     *
     * @param path the calculated path
     * @return The times of the transfer
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
     * @return The candidate vertices
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
     * @return The distance of the station and the address, in kilometers
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


    /**
     * Returns the route that is accepted by the front end. It encapsulates each
     * station vertex into an Address object and returns the arrayList.
     *
     * @param path The calculated path that will be used to do the output the route
     * @return the generated route with address object accepted by the front end
     */
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

    /**
     * It parses the input string format time into the readable time and then
     * calculates the difference of the two times.
     * Note that the start time should be earlier than the end time
     * and the formats are according to the xls file the TA distributes
     *
     * @param start The time of the subway arriving at the previous station
     * @param end   The time of the subway arriving at the current station
     * @return The difference of the two times, in minute unit
     */
    private static int getTime(@NotNull String start, @NotNull String end) {
        int length1 = start.length();
        int length2 = end.length();
        int minute = (int) end.charAt(length2 - 1) - (int) start.charAt(length1 - 1);
        int ten = (int) end.charAt(length2 - 2) - (int) start.charAt(length1 - 2);
        int hour = (int) end.charAt(length2 - 4) - (int) start.charAt(length1 - 4);
        hour = hour >= 0 ? hour : hour + 4;
        return hour * 60 + ten * 10 + minute;
    }

    /**
     * This method is called when initializing the graph, it adds the station as
     * a vertex into the graph. It modifies the existing vertex if the station already
     * exists or new a vertex if it is the first time that such a vertex is read.
     * While newing the vertex objects, this method also maintains the previous station
     * of the new station, the edge that connects the two edges, and the GPS info
     * of the new address.
     * Note that the station name of the transfer stations on different lines is the
     * same.
     *
     * @param preStation  The previous station of the current station along the edge
     * @param stationName The name of the station
     * @param lineName    The name of the line connecting the station that is being added
     * @param time1       The arriving time of the subway at the previous station
     * @param time2       The arriving time of the subway at the current station
     * @param latitude    Latitude of the current subway station
     * @param longitude   Longitude of the current subway station
     * @return The just newed vertex
     */
    public Vertex addVertex(Vertex preStation, String stationName, String lineName,
                            String time1, String time2, double latitude, double longitude) {
        Vertex newStation = getStation(stationName);
        if (newStation == null) {
            newStation = new Vertex(stationName, latitude, longitude);
            vertices.add(newStation);
        }
        preStation.getAdjacentVertices().add(newStation);
        newStation.getAdjacentVertices().add(preStation);
        Edge edge = new Edge(preStation, newStation, lineName, getTime(time1, time2));
        preStation.getEdges().add(edge);
        newStation.getEdges().add(edge);
        return newStation;
    }

    /**
     * Returns a station whose name matches the requesting address
     * Note that the request address must match the name of the station exactly,
     * otherwise it will return null
     *
     * @param address The address in the String format
     * @return A Vertex object that is a subway station
     */
    @Nullable
    public Vertex getStation(String address) {
        for (Vertex vertex : vertices)
            if (vertex.getName().equals(address))
                return vertex;
        return null;
    }

    /**
     * Search for the nearest station with the requesting address
     * It calculates the straight line distance of the address and the
     * subway station and iterates through the entire station list.
     * Note that the address has no restriction
     *
     * @param address The address requested
     * @return The nearest station from the address
     */
    private Vertex nearestStation(Address address) {
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

    /**
     * The core entrance of the algorithm, it calls the dijkstra algorithm and
     * gets a shortest weight path from the two subway stations.
     * Note that the two vertices must be the stations that have already been in the
     * station array list and This method does not call the dijkstra algorithm if the
     * two requested vertices are the same.
     *
     * @param startV The source of the graph
     * @param endV   The sink of the graph
     * @return The path returned from the dijkstra algorithm
     */
    private ArrayList<Vertex> getPath(Vertex startV, Vertex endV) {
        ArrayList<Vertex> path = new ArrayList<>();
        Dijkstra dijkstra = new Dijkstra();
        if (startV == endV)
            path.add(startV);
        else
            path = dijkstra.getPath(vertices, startV, endV);
        return path;
    }

    /**
     * The auxiliary method for the last two method for conveniently get the path and total
     * consuming time for any requested station and the specified start and end address.
     * Note that the global variable time is refreshed and updated in this method
     *
     * @param vertexS Start subway station
     * @param vertexE End subway station
     * @param start   The start address
     * @param end     The end address
     * @return The same as get path method
     */
    private ArrayList<Vertex> getPathAndTime(Vertex vertexS, Vertex vertexE, Address start, Address end) {
        time = 0;
        ArrayList<Vertex> path = getPath(vertexS, vertexE);
        time = path.get(path.size() - 1).getDistance() +
                (int) ((calculateDistance(vertexS, start) * 12)) +
                (int) (calculateDistance(vertexE, end) * 12);
        return path;
    }

    /**
     * The starting entrance from the front end, the shortest walking option
     * will call this method directly and this method will select the station
     * based on the shortest distance and call the dijkstra algorithm
     *
     * @param start The start point got from the front end
     * @param end   The end point got from the front end
     * @return The Address array list prepared for the frontend-backend interacting
     * methods.
     */
    public ArrayList<Address> shortestWalking(Address start, Address end) {
        time = 0;
        Vertex startV = nearestStation(start);
        Vertex endV = nearestStation(end);
        if (startV == null || endV == null)
            return null;
        ArrayList<Vertex> path = getPath(startV, endV);
        time += path.get(path.size() - 1).getDistance();
        return returnRoute(path);
    }

    /**
     * The starting entrance from the front end, the shortest time option
     * will call this method directly and this method will call the dijkstra
     * algorithm to calculate the path and time for a list of stations
     * around the start and end point and then select the station
     * based on the shortest total time.
     *
     * @param start The start point got from the front end
     * @param end   The end point got from the front end
     * @return The Address array list prepared for the frontend-backend interacting
     * methods.
     */
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

    /**
     * The starting entrance from the front end, the less transfer option
     * will call this method directly and this method will call the dijkstra
     * algorithm to calculate the path and transfer time for a list of stations
     * around the start and end point and then select the station
     * based on the least transfer time.
     *
     * @param start The start point got from the front end
     * @param end   The end point got from the front end
     * @return The Address array list prepared for the frontend-backend interacting
     * methods.
     */
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
}
