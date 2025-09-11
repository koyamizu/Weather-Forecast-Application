package com.example.demo.serivce;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.demo.entity.LocationData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;
import io.swagger.client.model.ForecastForecastday;

public interface WeatherForecastSearchService {
	
	ForecastForecastday findForecast(Optional<LocationData> gptResult,LocalDate date) throws JsonMappingException, JsonProcessingException, ApiException;

	String findAlerts(String city, LocalDate date) throws JsonMappingException, JsonProcessingException, ApiException;

	List<LocationData> findLocationData(String input);

}
