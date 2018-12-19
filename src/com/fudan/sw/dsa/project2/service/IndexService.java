package com.fudan.sw.dsa.project2.service;

import com.fudan.sw.dsa.project2.bean.*;
import com.fudan.sw.dsa.project2.constant.FileGetter;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static com.fudan.sw.dsa.project2.utilities.TransferStationOrganizer.importBusStationPos;

/**
 * this class is what you need to complete
 *
 * @author zjiehang
 */
@Service
public class IndexService {
    //the subway subwayGraph
    private Graph subwayGraph = null;
    private Graph busGraph = null;

    /**
     * create the subwayGraph use file
     */
    public void createGraphFromFile() {
        if (subwayGraph != null && busGraph != null) return;
        FileGetter fileGetter = new FileGetter();
        File file = fileGetter.readFileFromClasspath("subway.txt");
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            //create the subwayGraph from file
            subwayGraph = new Graph();
            int totalLineNumber = 15;
            String lineName;
            String stationName;
            String time1;
            String time2;
            String[] stationInfoArray;
            for (int k = 0; k < totalLineNumber; k++) {
                lineName = bufferedReader.readLine();
                bufferedReader.readLine();
                if (k == 9 || k == 10)
                    bufferedReader.readLine();
                String stationInfo = bufferedReader.readLine();
                stationInfoArray = stationInfo.split(",");
                stationName = stationInfoArray[0];
                Vertex newStation = subwayGraph.getStation(stationName);
                if (newStation == null) {
                    newStation = new Vertex(stationName,
                            Double.parseDouble(stationInfoArray[2]),
                            Double.parseDouble(stationInfoArray[1]));
                    subwayGraph.vertices.add(newStation);
                }
                time1 = stationInfoArray[3];
                Vertex preStation;
                if (k == 9 || k == 10) {
                    Vertex binStation = null;
                    String time3 = "";
                    while (true) {
                        preStation = newStation;
                        stationInfo = bufferedReader.readLine();
                        stationInfoArray = stationInfo.split(",");
                        stationName = stationInfoArray[0];
                        time2 = stationInfoArray[3];
                        if (time2.equals("--"))
                            break;
                        if (!stationInfoArray[4].equals("--")) time3 = stationInfoArray[4];
                        if (stationInfoArray[4].equals("--") && binStation == null)
                            binStation = preStation;
                        newStation = subwayGraph.addVertex(preStation, stationName, lineName, time1, time2,
                                Double.parseDouble(stationInfoArray[2]),
                                Double.parseDouble(stationInfoArray[1]));
                        time1 = time2;
                    }

                    preStation = binStation;
                    time1 = time3;
                    while (!stationInfo.equals("Separating Line")) {
                        stationName = stationInfoArray[0];
                        time2 = stationInfoArray[4];
                        newStation = subwayGraph.addVertex(preStation, stationName, lineName, time1, time2,
                                Double.parseDouble(stationInfoArray[2]),
                                Double.parseDouble(stationInfoArray[1]));
                        time1 = time2;
                        preStation = newStation;
                        stationInfo = bufferedReader.readLine();
                        stationInfoArray = stationInfo.split(",");
                    }
                } else
                    while (true) {
                        stationInfo = bufferedReader.readLine();
                        stationInfoArray = stationInfo.split(",");
                        if (stationInfo.equals("Separating Line")) break;
                        preStation = newStation;
                        stationName = stationInfoArray[0];

                        if (lineName.equals("Line 4") && stationName.equals("浦电路"))
                            stationName = stationName + "4";
                        else if (lineName.equals("Line 6") && stationName.equals("浦电路"))
                            stationName = stationName + "6";

                        time2 = stationInfoArray[3];
                        newStation = subwayGraph.addVertex(preStation, stationName, lineName, time1, time2,
                                Double.parseDouble(stationInfoArray[2]),
                                Double.parseDouble(stationInfoArray[1]));
                        time1 = time2;
                    }
            }
            Vertex vertex1 = subwayGraph.getStation("宜山路");
            Vertex vertex2 = subwayGraph.getStation("虹桥路");
            Edge edge = new Edge(vertex1, vertex2, "Line 4", 2);
            assert vertex1 != null;
            vertex1.getEdges().add(edge);
            assert vertex2 != null;
            vertex2.getEdges().add(edge);
            vertex1.getAdjacentVertices().add(vertex2);
            vertex2.getAdjacentVertices().add(vertex1);
            System.out.println("The subwayGraph is generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }


        file = fileGetter.readFileFromClasspath("busLineValid.txt");
        File busGPSFile = fileGetter.readFileFromClasspath("busStationGPS.txt");
        try (FileReader fileReader = new FileReader(file)) {
            TreeMap<String, String> latitude = new TreeMap<>(),
                    longitude = new TreeMap<>();
            importBusStationPos(busGPSFile, latitude, longitude);
            busGraph = new Graph();
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line, stationName, preStationName;
            while ((line = bufferedReader.readLine()) != null) {
                String lineName = line.split(",")[0];
                String[] stations = line.split(",")[1].split("-");
                stationName = stations[0];
                Vertex newStation = busGraph.getStation(stationName);
                if (newStation == null) {
                    newStation = new Vertex(stationName,
                            Double.parseDouble(latitude.get(stationName)),
                            Double.parseDouble(longitude.get(stationName)));
                    busGraph.vertices.add(newStation);
                }
                Vertex preStation;
                for (int i = 1; i < stations.length; i++) {
                    preStation = newStation;
                    preStationName = stationName;
                    stationName = stations[i];
                    int time = (int) Math.abs(Graph.distance(Double.parseDouble(latitude.get(preStationName)),
                            Double.parseDouble(latitude.get(stationName)),
                            Double.parseDouble(longitude.get(preStationName)),
                            Double.parseDouble(longitude.get(stationName))) * 3);
                    newStation = busGraph.addVertex(preStation, stationName, lineName, "busGraph", Integer.toString(time),
                            Double.parseDouble(latitude.get(stationName)),
                            Double.parseDouble(longitude.get(stationName)));
                }
            }
            System.out.println("The bus lines are imported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ReturnValue travelRoute(Map<String, Object> params) {
        String startAddress = params.get("startAddress").toString();
        String startLongitude = params.get("startLongitude").toString();
        String startLatitude = params.get("startLatitude").toString();
        String endAddress = params.get("endAddress").toString();
        String endLongitude = params.get("endLongitude").toString();
        String endLatitude = params.get("endLatitude").toString();
        String choose = params.get("choose").toString();

        System.out.println(startAddress);
        System.out.println(startLongitude);
        System.out.println(startLatitude);
        System.out.println(endAddress);
        System.out.println(endLongitude);
        System.out.println(endLatitude);
        System.out.println(choose);

        Address startPoint = new Address(startAddress, startLongitude, startLatitude);
        Address endPoint = new Address(endAddress, endLongitude, endLatitude);
        ArrayList<Address> route = new ArrayList<>();
        long startTime = System.nanoTime();
        switch (choose) {
            case "1":
                //步行最少
                route = subwayGraph.shortestWalking(startPoint, endPoint);
                break;
            case "2":
                //换乘最少
                route = subwayGraph.lessTransfer(startPoint, endPoint);
                break;
            case "3":
                //时间最短
                route = subwayGraph.shortestTime(startPoint, endPoint);
                break;
            case "4":
                //换乘最少
                route = subwayGraph.leastTransfer(startPoint, endPoint);
                break;
            case "5":
                //步行最少
                route = busGraph.shortestWalking(startPoint, endPoint);
                break;
            case "6":
                //换乘最少
                route = busGraph.lessTransfer(startPoint, endPoint);
                break;
            case "7":
                //时间最短
                route = busGraph.shortestTime(startPoint, endPoint);
                break;
            default:
        }
        System.out.println("\nThe time used for the query is " +
                (System.nanoTime() - startTime) / 1000 + " microseconds");
        ReturnValue returnValue = new ReturnValue();
        returnValue.setStartPoint(startPoint);
        returnValue.setEndPoint(endPoint);
        returnValue.setSubwayList(route);
        if (choose.compareTo("3") <= 0) {
            returnValue.setMinutes(subwayGraph.totalTime);
            returnValue.setWalkingDistance(subwayGraph.walkingDistance);
        } else {
            returnValue.setMinutes(busGraph.totalTime);
            returnValue.setWalkingDistance(busGraph.walkingDistance);
        }
        return returnValue;
    }
}
