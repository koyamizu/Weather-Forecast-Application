package com.example.demo.serivce;

import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.model.ForecastForecastday;

public interface WeatherForecastSearchService {
	
	ForecastForecastday findForecast(String city,LocalDate date) throws JsonMappingException, JsonProcessingException;

}
