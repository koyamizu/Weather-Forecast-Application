package com.example.demo.serivce.Impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.WeatherApiClient;
import com.example.demo.repository.WeatherForecastSearchMapper;
import com.example.demo.serivce.WeatherForecastSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;
import io.swagger.client.model.ForecastDay;
import io.swagger.client.model.ForecastForecastday;
import io.swagger.client.model.ForecastHour;
import io.swagger.client.model.InlineResponse2002;
import io.swagger.client.model.Location;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherForecastSearchServiceImp implements WeatherForecastSearchService {

	private final WeatherForecastSearchMapper weatherForecastSearchMapper;

	@Override
	@Transactional
	public ForecastForecastday findForecast(String city, java.time.LocalDate date)
			throws JsonMappingException, JsonProcessingException {
		ForecastForecastday forecastDay = new ForecastForecastday();

		ForecastDay forecast = weatherForecastSearchMapper.selectDay(city, date);

		if (!Objects.equals(null, forecast)) {
			List<ForecastHour> hours = weatherForecastSearchMapper.selectHour(city, date);
			forecastDay.setDay(forecast);
			forecastDay.setHour(hours);
			return forecastDay;
		}

		String dateStr = date.toString();
		WeatherApiClient client = new WeatherApiClient(city, dateStr);

		try {
			InlineResponse2002 resp = client.fetchWeather();

			Location location = resp.getLocation();
			forecastDay = resp.getForecast().getForecastday().get(0);
			ForecastDay day = forecastDay.getDay();
			List<ForecastHour> hours = forecastDay.getHour();

			weatherForecastSearchMapper.insertDay(date, location, day);
			weatherForecastSearchMapper.insertHour(date, location, hours);

		} catch (ApiException e) {
			System.err.println("Exception when calling ApisApi#astronomy");
			e.printStackTrace();
			//TODO RuntimeExceiptionを投げる
		}
		//DBアクセスに失敗しても、とりあえず結果だけはクライアントに返して、ログ表示を行う
		return forecastDay;
	}

}
