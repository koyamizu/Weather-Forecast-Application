package com.example.demo.api;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;
import io.swagger.client.model.InlineResponse2001;
import io.swagger.client.model.InlineResponse2002;


public class WeatherApiClientTest {
	

	@Test
	void test_fetchWeather() throws JsonMappingException, JsonProcessingException, ApiException {
		WeatherApiClient client=new WeatherApiClient("Tokyo","2025-09-15");
		InlineResponse2002 result=client.fetchWeather();
		assertThat(result).isNotNull();
	}
	
	@Test
	void test_fetchWeather_Alerts() throws JsonMappingException, JsonProcessingException, ApiException {
		WeatherApiClient client=new WeatherApiClient("23.3667,103.3981","2025-09-15");
		InlineResponse2001 result=client.fetchAlerts();
		assertThat(result).isNotNull();
	}
}
