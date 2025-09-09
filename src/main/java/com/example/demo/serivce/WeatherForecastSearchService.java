package com.example.demo.serivce;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;
import io.swagger.client.model.AlertsAlert;
import io.swagger.client.model.ForecastForecastday;

public interface WeatherForecastSearchService {
	
	ForecastForecastday findForecast(String city,LocalDate date) throws JsonMappingException, JsonProcessingException, ApiException;

	List<AlertsAlert> findAlerts(String city, LocalDate date) throws JsonMappingException, JsonProcessingException, ApiException;

}
