package com.example.demo.serivce;

import java.time.LocalDate;

import com.example.demo.entity.WeatherForecast;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface WeatherForecastSearchService {
	
	WeatherForecast findForecast(String city,LocalDate date) throws JsonMappingException, JsonProcessingException;

}
