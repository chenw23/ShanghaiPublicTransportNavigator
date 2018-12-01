package com.fudan.sw.dsa.project2.service;

import com.fudan.sw.dsa.project2.bean.*;
import com.fudan.sw.dsa.project2.constant.FileGetter;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
        //如果图未初始化
        if (graph == null) {
            FileGetter fileGetter = new FileGetter();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileGetter.readFileFromClasspath()))) {
                //create the graph from file
                graph = new Graph();
                int totalLineNumber = 15;
                String name;
                String stationName;
                String time1;
                String time2;
                String[] stationInfoArray;
                for (int k = 0; k < totalLineNumber; k++) {
                    name = bufferedReader.readLine();
                    bufferedReader.readLine();
                    if (k == 9 || k == 10)
                        bufferedReader.readLine();
                    String stationInfo = bufferedReader.readLine();
                    stationInfoArray = stationInfo.split(",");
                    stationName = stationInfoArray[0];
                    Vertex newStation = graph.exist(stationName);
                    if (newStation == null) {
                        newStation = new Vertex(stationName,
                                Double.parseDouble(stationInfoArray[1]),
                                Double.parseDouble(stationInfoArray[2]));
                        graph.vertices.add(newStation);
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
                            newStation = graph.addVertex(preStation, stationName, name, time1, time2,
                                    Double.parseDouble(stationInfoArray[1]),
                                    Double.parseDouble(stationInfoArray[2]));
                            time1 = time2;
                        }

                        preStation = binStation;
                        time1 = time3;
                        while (!stationInfo.equals("Separating Line")) {
                            stationName = stationInfoArray[0];
                            time2 = stationInfoArray[4];
                            newStation = graph.addVertex(preStation, stationName, name, time1, time2,
                                    Double.parseDouble(stationInfoArray[1]),
                                    Double.parseDouble(stationInfoArray[2]));
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

                            if (name.equals("Line 4") && stationName.equals("浦电路"))
                                stationName = stationName + "4";
                            else if (name.equals("Line 6") && stationName.equals("浦电路"))
                                stationName = stationName + "6";

                            time2 = stationInfoArray[3];
                            newStation = graph.addVertex(preStation, stationName, name, time1, time2,
                                    Double.parseDouble(stationInfoArray[1]),
                                    Double.parseDouble(stationInfoArray[2]));
                            time1 = time2;
                        }
                }
                Vertex vertex1 = graph.exist("宜山路");
                Vertex vertex2 = graph.exist("虹桥路");
                Edge edge = new Edge(vertex1, vertex2, "Line 4", 2);
                assert vertex1 != null;
                vertex1.getEdges().add(edge);
                assert vertex2 != null;
                vertex2.getEdges().add(edge);
                vertex1.getVertices().add(vertex2);
                vertex2.getVertices().add(vertex1);
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
        String endAddress = URLDecoder.decode(params.get("endAddress").toString(), StandardCharsets.UTF_8);
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
        List<Address> addresses = new ArrayList<Address>();
        switch (choose) {
            case "1":
                //步行最少
                //举个例子
                addresses.add(new Address("张江高科", "121.593923", "31.207892"));
                addresses.add(new Address("龙阳路", "121.564028", "31.209714"));
                addresses.add(new Address("世纪公园", "121.557164", "31.215891"));
                addresses.add(new Address("上海科技馆", "121.550589", "31.225433"));
                addresses.add(new Address("世纪大道", "121.533449", "31.23482"));
                addresses.add(new Address("东昌路", "121.5220233", "31.23905"));
                addresses.add(new Address("陆家嘴", "121.508836", "31.243558"));
                addresses.add(new Address("南京东路", "121.490331", "31.242817"));
                addresses.add(new Address("人民广场", "121.479371", "31.238803"));
                break;
            case "2":
                //换乘最少
                break;
            case "3":
                //时间最短:
                break;
            default:
                break;
        }

        ReturnValue returnValue = new ReturnValue();
        returnValue.setStartPoint(startPoint);
        returnValue.setEndPoint(endPoint);
        returnValue.setSubwayList(addresses);

        return returnValue;
    }
}
