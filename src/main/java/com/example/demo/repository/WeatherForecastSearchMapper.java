package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.swagger.client.model.ForecastDay;
import io.swagger.client.model.ForecastHour;

@Mapper
public interface WeatherForecastSearchMapper {
	
//	InlineResponse2002 select(String city, LocalDate date);
	
	ForecastDay selectDay(String city, LocalDate date);
	
	List<ForecastHour> selectHour(String city, LocalDate date);
	
	//TODO location→cityRegionRomajiに書き換える。また、cityRegionも引数として追加。
	void insertDay(LocalDate date, String location,String cityRegion, ForecastDay day);
	
	void insertHour(LocalDate date, String location,String cityRegion, List<ForecastHour> hours);
}
