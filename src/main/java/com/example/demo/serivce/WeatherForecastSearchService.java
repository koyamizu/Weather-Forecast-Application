package com.example.demo.serivce;

import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.model.InlineResponse2002;

public interface WeatherForecastSearchService {
	
	InlineResponse2002 findForecast(String city,LocalDate date) throws JsonMappingException, JsonProcessingException;

}
