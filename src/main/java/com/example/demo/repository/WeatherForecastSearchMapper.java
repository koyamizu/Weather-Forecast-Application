package com.example.demo.repository;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;

import io.swagger.client.model.InlineResponse2002;

@Mapper
public interface WeatherForecastSearchMapper {

	InlineResponse2002 select(String city, LocalDate date);
	
	void insert(InlineResponse2002 forecast);
}
