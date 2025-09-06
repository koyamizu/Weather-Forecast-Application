package com.example.demo.serivce.Impl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;

import com.example.demo.entity.WeatherForecast;
import com.example.demo.repository.WeatherForecastSearchMapper;
import com.example.demo.serivce.WeatherForecastSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.ApisApi;
import io.swagger.client.auth.ApiKeyAuth;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherForecastSearchServiceImp implements WeatherForecastSearchService {
	
	private final WeatherForecastSearchMapper weatherForecastSearchMapper;
	
	@Override
	public WeatherForecast findForecast(String city, java.time.LocalDate date) throws JsonMappingException, JsonProcessingException {
		
		WeatherForecast forecast=weatherForecastSearchMapper.select(city, date);
		
		if(Objects.equals(null, forecast)) {
			return forecast;
		}

		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
        ApiKeyAuth.setApiKey("bbfd427d1cf746e79eb30549250609");

        ApisApi apiInstance = new ApisApi();
        
		String dateStr=date.toString();

        LocalDate threetenbpFormat=LocalDate.parse(dateStr);

        try {
            String result = (String)apiInstance.forecastWeather(city,1,threetenbpFormat
            		, null, null, "jap", null, null, null);
            
    		ObjectMapper objectMapper = new ObjectMapper();
    		forecast = objectMapper.readValue(result,
    				new TypeReference<WeatherForecast>() {
    				});
    		
    		weatherForecastSearchMapper.insert(forecast);
        } catch (ApiException e) {
            System.err.println("Exception when calling ApisApi#astronomy");
            e.printStackTrace();
        }

		return null;
	}

}
