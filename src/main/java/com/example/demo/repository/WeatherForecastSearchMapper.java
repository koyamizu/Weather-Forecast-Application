package com.example.demo.repository;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.WeatherForecastResult;

@Mapper
public interface WeatherForecastSearchMapper {

	WeatherForecastResult select(String city, LocalDate date);
	
	void insert(WeatherForecastResult forecast);
}
