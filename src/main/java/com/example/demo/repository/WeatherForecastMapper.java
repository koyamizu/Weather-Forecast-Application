package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.LocationData;

import io.swagger.client.model.ForecastDay;
import io.swagger.client.model.ForecastHour;

@Mapper
public interface WeatherForecastMapper {
	
//	InlineResponse2002 select(String city, LocalDate date);
	
	ForecastDay selectDay(String cityRegion, LocalDate date);
	
	List<ForecastHour> selectHour(String cityRegion, LocalDate date);
	
	List<LocationData> selectLocations(String input);
	
	void insertDay(LocalDate date, String cityRegion,ForecastDay day);
	
	void insertHour(LocalDate date, String cityRegion,List<ForecastHour> hours);
	
	void insertLocations(List<LocationData> locations);
}
