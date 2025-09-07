package com.example.demo.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;


public class WeatherApiClientTest {

	@Test
	void test_fetchWeather() throws JsonMappingException, JsonProcessingException, ApiException {
		WeatherApiClient client=new WeatherApiClient("東京","2025-09-10");
		WeatherForecastResult result=client.fetchWeather();
		assertThat(result).isNotNull();
	}
}
