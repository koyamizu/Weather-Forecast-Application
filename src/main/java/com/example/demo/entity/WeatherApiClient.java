package com.example.demo.entity;

import org.threeten.bp.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.ApisApi;
import io.swagger.client.auth.ApiKeyAuth;

public class WeatherApiClient {
	
	private String city;
	private LocalDate date;
	
	public WeatherApiClient(String city,String dateStr) {
		this.city=city;
        this.date=LocalDate.parse(dateStr);
	}

	public WeatherForecastResult fetchWeather() throws JsonMappingException, JsonProcessingException, ApiException {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("bbfd427d1cf746e79eb30549250609");

        ApisApi apiInstance = new ApisApi();

        String result = (String)apiInstance.forecastWeather(city,1,date
        		, null, null, "jap", null, null, null);
        
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(result,
				new TypeReference<WeatherForecastResult>() {
				});
	}
}
