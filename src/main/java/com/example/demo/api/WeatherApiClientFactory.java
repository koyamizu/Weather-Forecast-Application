package com.example.demo.api;

import org.springframework.stereotype.Component;

import com.example.demo.configuration.ApiKeyConfig;

@Component
public class WeatherApiClientFactory {

	private final ApiKeyConfig apiKeyConfig;

	public WeatherApiClientFactory(ApiKeyConfig apiKeyConfig) {
		this.apiKeyConfig = apiKeyConfig;
	}

	public WeatherApiClient create(String latlon, String dateStr) {
		return new WeatherApiClient(latlon, dateStr, apiKeyConfig.getApiKey());
	}

}
