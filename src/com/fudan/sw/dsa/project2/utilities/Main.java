package com.fudan.sw.dsa.project2.utilities;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File file = new File("stations.txt");
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] stations = line.split("-");
                AddStation:
                for (String station : stations) {
                    for (String existingStation : arrayList)
                        if (existingStation.equals(station))
                            continue AddStation;
                    String query = "query=".concat(station);
                    query = query.concat("&tag=公交车站&region=上海&output=xml&ak=KU0KWZsvYa9wr6cCQHEGYzNj8C3v4lDT");
                    String xmlData = HttpRequest.sendGet(query);
                    StringReader sr = new StringReader(xmlData);
                    InputSource is = new InputSource(sr);
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(is);
                    String lat = "INVALID";
                    String lng = "INVALID";
                    if (document.getElementsByTagName("lat") == null &&
                            document.getElementsByTagName("lat").item(0) == null) {
                        lat = document.getElementsByTagName("lat").item(0).getFirstChild().getTextContent();
                        lng = document.getElementsByTagName("lng").item(0).getFirstChild().getTextContent();
                    }
                    System.out.println(station + " " + lat + " " + lng);
                    arrayList.add(station);
                    Thread.sleep(2000);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}