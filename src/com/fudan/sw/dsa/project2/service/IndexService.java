package com.fudan.sw.dsa.project2.service;

import com.fudan.sw.dsa.project2.bean.Address;
import com.fudan.sw.dsa.project2.bean.Graph;
import com.fudan.sw.dsa.project2.bean.ReturnValue;
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
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
                //create the graph from file
                graph = new Graph();

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
