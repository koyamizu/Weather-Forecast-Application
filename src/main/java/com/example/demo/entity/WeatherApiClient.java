package com.example.demo.entity;

import org.threeten.bp.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.ApisApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.InlineResponse2001;
import io.swagger.client.model.InlineResponse2002;

public class WeatherApiClient {

	private String city;
	private LocalDate date;
	private ApisApi apiInstance;

	public WeatherApiClient(String city, String dateStr) {
		this.city = city;
		this.date = LocalDate.parse(dateStr);
	}

	static {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		ApiKeyAuth.setApiKey("bbfd427d1cf746e79eb30549250609");

	}

	public InlineResponse2002 fetchWeather() throws JsonMappingException, JsonProcessingException, ApiException {
		apiInstance = new ApisApi();
		return (InlineResponse2002) apiInstance.forecastWeather(city, 1, date, null, null, "ja", null, null, null);
	}
	
	public InlineResponse2001 fetchAlerts() throws JsonMappingException, JsonProcessingException, ApiException {
		apiInstance = new ApisApi();
		return (InlineResponse2001) apiInstance.forecastWeather(city, 1, date, null, null, null, "yes", null, null);
	}

}
