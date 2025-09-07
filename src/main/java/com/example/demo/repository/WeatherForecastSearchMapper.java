package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.swagger.client.model.ForecastDay;
import io.swagger.client.model.ForecastHour;
import io.swagger.client.model.InlineResponse2002;
import io.swagger.client.model.Location;

@Mapper
public interface WeatherForecastSearchMapper {
	
	InlineResponse2002 select(String city, LocalDate date);
		
	void insertDay(LocalDate date, Location location,ForecastDay day);
	
	void insertHour(Location location,List<ForecastHour> hours);
}
