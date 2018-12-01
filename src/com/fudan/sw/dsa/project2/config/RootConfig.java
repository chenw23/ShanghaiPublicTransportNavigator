package com.fudan.sw.dsa.project2.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.context.annotation.ComponentScan.Filter;

@Configuration
@ComponentScan(basePackages={"com.fudan.sw.dsa.project2"},excludeFilters=
				{@Filter(type=FilterType.ANNOTATION,value=EnableWebMvc.class )})
public class RootConfig {

}