package com.fudan.sw.dsa.project2.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fudan.sw.dsa.project2.bean.ReturnValue;
import com.fudan.sw.dsa.project2.service.IndexService;

/**
 * For controller
 * controller is to get the http request, and give the http response
 * service is to do the business logic
 * @author zjiehang
 *
 */
@Controller
@RequestMapping("/")
public class IndexController 
{
	//use service to do all the business logic
	@Autowired
	IndexService indexService;
	
	@RequestMapping("/")
	public String index()
	{
		indexService.createGraphFromFile();
		return "index";
	}
	
	/**
	 * @params params: the attribute and its value
	 * in params map, key is below,value is by UI
	 * @param startAddress: the address of start point like 复旦大学张江校区
	 * @param startlongitude: the longitude of start point
	 * @param startLatitude: the latitude of start point
	 * @param endAddress:  the address of end point like 复旦大学张江校区
	 * @param endlongitude: the longitude of end point
	 * @param endLatitude: the latitude of end point
	 * @return
	 */
	@RequestMapping(value="/submitsearch",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ReturnValue submitSearch(@RequestBody Map<String, Object> params)
	{

		ReturnValue returnValue=indexService.travelRoute(params);

		return returnValue;
	}
}
