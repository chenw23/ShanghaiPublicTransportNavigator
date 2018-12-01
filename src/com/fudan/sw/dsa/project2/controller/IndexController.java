package com.fudan.sw.dsa.project2.controller;

import com.fudan.sw.dsa.project2.bean.ReturnValue;
import com.fudan.sw.dsa.project2.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * For controller
 * controller is to get the http request, and give the http response
 * service is to do the business logic
 *
 * @author zjiehang
 */
@Controller
@RequestMapping("/")
public class IndexController {
    //use service to do all the business logic
    @Autowired
    IndexService indexService;

    @RequestMapping("/")
    public String index() {
        indexService.createGraphFromFile();
        return "index";
    }

    /**
     * @param params startAddress:   the address of start point like 复旦大学张江校区
     *               startlongitude: the longitude of start point
     *               startLatitude:  the latitude of start point
     *               endAddress:     the address of end point like 复旦大学张江校区
     *               endlongitude:   the longitude of end point
     *               endLatitude:    the latitude of end point
     *               in params map, key is below,value is by UI
     * @return a ReturnValue object containing start point, end point and the subway lists.
     */
    @RequestMapping(value = "/submitsearch", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ReturnValue submitSearch(@RequestBody Map<String, Object> params) {
        return indexService.travelRoute(params);
    }
}
