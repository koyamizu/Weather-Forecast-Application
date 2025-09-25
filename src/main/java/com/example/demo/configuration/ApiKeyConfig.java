package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiKeyConfig {

	@Value("${weather.api.key}")
	private String apiKey;

    public String getApiKey() {
		return apiKey;
	}
}
