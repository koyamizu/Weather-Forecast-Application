package com.example.demo.serivce;

import java.time.LocalDate;

import com.example.demo.entity.WeatherForecastResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface WeatherForecastSearchService {
	
	WeatherForecastResult findForecast(String city,LocalDate date) throws JsonMappingException, JsonProcessingException;

}
