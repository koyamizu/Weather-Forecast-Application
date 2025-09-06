package com.example.demo.repository;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.WeatherForecast;

@Mapper
public interface WeatherForecastSearchMapper {

	WeatherForecast select(String city, LocalDate date);
	
	void insert(WeatherForecast forecast);
}
