package com.example.demo.api;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;
import io.swagger.client.model.InlineResponse2001;
import io.swagger.client.model.InlineResponse2002;


@SpringBootTest
public class WeatherApiClientTest {
	
	@Autowired
	private WeatherApiClientFactory clientFactory;

	@Test
	void test_fetchWeather() throws JsonMappingException, JsonProcessingException, ApiException {
		WeatherApiClient client=clientFactory.create("Tokyo","2025-09-15");
		InlineResponse2002 result=client.fetchWeather();
		assertThat(result).isNotNull();
	}
	
	@Test
	void test_fetchWeather_Alerts() throws JsonMappingException, JsonProcessingException, ApiException {
		WeatherApiClient client=clientFactory.create("23.3667,103.3981","2025-09-15");
		InlineResponse2001 result=client.fetchAlerts();
		assertThat(result).isNotNull();
	}
}
