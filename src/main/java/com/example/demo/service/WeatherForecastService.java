package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.entity.LocationData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;
import io.swagger.client.model.ForecastForecastday;

public interface WeatherForecastService {
	
	ForecastForecastday findForecast(String cityRegion,LocalDate date) throws JsonMappingException, JsonProcessingException, ApiException;

	String findAlerts(String city, LocalDate date) throws JsonMappingException, JsonProcessingException, ApiException;

	List<LocationData> findLocationData(String input);

	void fetchLocationDataFromOpenAi(String input, String jobId, SseEmitter emitter) throws IOException;

	List<LocationData> getResult(String jobId);
}
