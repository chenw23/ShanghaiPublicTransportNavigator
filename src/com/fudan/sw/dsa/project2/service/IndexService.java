package com.fudan.sw.dsa.project2.service;

import com.fudan.sw.dsa.project2.bean.*;
import com.fudan.sw.dsa.project2.constant.FileGetter;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

/**
 * this class is what you need to complete
 *
 * @author zjiehang
 */
@Service
public class IndexService {
    //the subway graph
    private Graph graph = null;

    /**
     * create the graph use file
     */
    public void createGraphFromFile() {
        if (graph == null) {
            FileGetter fileGetter = new FileGetter();
            try (BufferedReader bufferedReader =
                         new BufferedReader(new FileReader(fileGetter.readFileFromClasspath()))) {
                //create the graph from file
                graph = new Graph();
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
                    Vertex newStation = graph.getStation(stationName);
                    if (newStation == null) {
                        newStation = new Vertex(stationName,
                                Double.parseDouble(stationInfoArray[2]),
                                Double.parseDouble(stationInfoArray[1]));
                        Graph.vertices.add(newStation);
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
                            newStation = graph.addVertex(preStation, stationName, lineName, time1, time2,
                                    Double.parseDouble(stationInfoArray[2]),
                                    Double.parseDouble(stationInfoArray[1]));
                            time1 = time2;
                        }

                        preStation = binStation;
                        time1 = time3;
                        while (!stationInfo.equals("Separating Line")) {
                            stationName = stationInfoArray[0];
                            time2 = stationInfoArray[4];
                            newStation = graph.addVertex(preStation, stationName, lineName, time1, time2,
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
                            newStation = graph.addVertex(preStation, stationName, lineName, time1, time2,
                                    Double.parseDouble(stationInfoArray[2]),
                                    Double.parseDouble(stationInfoArray[1]));
                            time1 = time2;
                        }
                }
                Vertex vertex1 = graph.getStation("宜山路");
                Vertex vertex2 = graph.getStation("虹桥路");
                Edge edge = new Edge(vertex1, vertex2, "Line 4", 2);
                assert vertex1 != null;
                vertex1.getEdges().add(edge);
                assert vertex2 != null;
                vertex2.getEdges().add(edge);
                vertex1.getAdjacentVertices().add(vertex2);
                vertex2.getAdjacentVertices().add(vertex1);
                System.out.println("The graph is generated successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        switch (choose) {
            case "1":
                //步行最少
                route = graph.shortestWalking(startPoint, endPoint);
                break;
            case "2":
                //换乘最少
                route = graph.lessTransfer(startPoint, endPoint);
                break;
            case "3":
                //时间最短
                route = graph.shortestTime(startPoint, endPoint);
                break;
            default:
        }
        ReturnValue returnValue = new ReturnValue();
        returnValue.setStartPoint(startPoint);
        returnValue.setEndPoint(endPoint);
        returnValue.setSubwayList(route);
        returnValue.setMinutes(graph.time);
        return returnValue;
    }
}
