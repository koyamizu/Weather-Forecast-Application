package com.example.demo.serivce.Impl;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.demo.entity.WeatherApiClient;
import com.example.demo.entity.WeatherForecastResult;
import com.example.demo.repository.WeatherForecastSearchMapper;
import com.example.demo.serivce.WeatherForecastSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherForecastSearchServiceImp implements WeatherForecastSearchService {
	
	private final WeatherForecastSearchMapper weatherForecastSearchMapper;
	
	@Override
	public WeatherForecastResult findForecast(String city, java.time.LocalDate date) throws JsonMappingException, JsonProcessingException {
		
		WeatherForecastResult forecast=weatherForecastSearchMapper.select(city, date);
		
		if(Objects.equals(null, forecast)) {
			return forecast;
		}

		String dateStr=date.toString();
		WeatherApiClient client=new WeatherApiClient(city,dateStr);

        try {
            forecast=client.fetchWeather();    		
    		weatherForecastSearchMapper.insert(forecast);
        } catch (ApiException e) {
            System.err.println("Exception when calling ApisApi#astronomy");
            e.printStackTrace();
        }

		return null;
	}

}
