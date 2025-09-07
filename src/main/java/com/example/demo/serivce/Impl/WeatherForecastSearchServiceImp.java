package com.example.demo.serivce.Impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.demo.entity.WeatherApiClient;
import com.example.demo.repository.WeatherForecastSearchMapper;
import com.example.demo.serivce.WeatherForecastSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;
import io.swagger.client.model.ForecastDay;
import io.swagger.client.model.ForecastHour;
import io.swagger.client.model.InlineResponse2002;
import io.swagger.client.model.Location;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherForecastSearchServiceImp implements WeatherForecastSearchService {

	private final WeatherForecastSearchMapper weatherForecastSearchMapper;

	@Override
	public InlineResponse2002 findForecast(String city, java.time.LocalDate date)
			throws JsonMappingException, JsonProcessingException {

		InlineResponse2002 forecast = weatherForecastSearchMapper.select(city, date);

		if (!Objects.equals(null, forecast)) {
			return forecast;
		}

		String dateStr = date.toString();
		WeatherApiClient client = new WeatherApiClient(city, dateStr);

		try {
			forecast = client.fetchWeather();
			
			Location location=forecast.getLocation();
			ForecastDay day = forecast.getForecast().getForecastday().get(0).getDay();
			List<ForecastHour> hour = forecast.getForecast().getForecastday().get(0).getHour();
			
			weatherForecastSearchMapper.insertDaily(date,location, day);
			weatherForecastSearchMapper.insertHourly(location, hour);
			
		} catch (ApiException e) {
			System.err.println("Exception when calling ApisApi#astronomy");
			e.printStackTrace();
		}

		return forecast;
	}

}
