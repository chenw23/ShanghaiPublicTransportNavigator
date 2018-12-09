package com.fudan.sw.dsa.project2.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * By now, the busLine.csv and busStationGPS.txt are already generated. But for importing the data
 * into the graph, the current info still make the process of importing rather difficult.
 * Therefore, some pre-importing processes are to be made for the convenience of importing data.
 * Specifically, three steps are needed to do this work.
 * 1) Some stations didn't get valid GPS info from the Baidu map POI API, therefore, such stations
 * should be removed from the station list of the bus line route so as to avoid crash.
 * 2)
 */
public class TransferStationOrganizer {
    public static void main(String[] args) {
        removeInvalidData();
    }

    private static void removeInvalidData() {
        File busLineFile = new File("busLine.csv");
        System.out.println(busLineFile.getAbsolutePath());
        File busGPSFile = new File("busStationGPS.txt");
        try (Scanner busLine = new Scanner(busLineFile);
             Scanner scanner = new Scanner(busGPSFile)) {
            ArrayList<String> busLineString = new ArrayList<>();
            while (busLine.hasNextLine())
                busLineString.add(busLine.nextLine());
            TreeMap<String, String> latitude = new TreeMap<>();
            TreeMap<String, String> longitude = new TreeMap<>();
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" ");
                latitude.put(line[0], line[1]);
                longitude.put(line[0], line[2]);
            }
            ArrayList<String> stations = new ArrayList<>();
            for (String station : busLineString)
                stations.addAll(Arrays.asList(station.split(",")[1].split("-")));
            for (String station : stations) {
                String latitudeResult = latitude.get(station);
                String longitudeResult = longitude.get(station);
                if (latitudeResult == null || latitudeResult.equals("INVALID") ||
                        longitudeResult == null || longitudeResult.equals("INVALID"))
                    for (int i = 0; i < busLineString.size(); i++) {
                        String line = busLineString.remove(i);
                        busLineString.add(i, line.replaceAll(station + "-", ""));
                        line = busLineString.remove(i);
                        busLineString.add(i, line.replaceAll("-" + station, ""));
                    }
            }
            System.out.println(busLineString.size());
            for (String line : busLineString)
                System.out.println(line);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
