package com.example.demo.serivce.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.WeatherApiClient;
import com.example.demo.repository.WeatherForecastSearchMapper;
import com.example.demo.serivce.WeatherForecastSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;
import io.swagger.client.model.AlertsAlert;
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
			throws JsonMappingException, JsonProcessingException, ApiException {
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

		} catch (DataAccessException e) {
//			DataAccessExceptionはRuntimeExceptionのサブクラスであり、このメソッドはトランザクションを宣言しているので、
//			insertDay()とinsertHour()はロールバックされます。
			System.err.println("データアクセスにおいて例外が発生しました。");
			e.printStackTrace();
		}
		return forecastDay;
	}

	@Override
	public List<AlertsAlert> findAlerts(String city, LocalDate date) throws JsonMappingException, JsonProcessingException, ApiException {
		String dateStr = date.toString();
		WeatherApiClient client = new WeatherApiClient(city, dateStr);
		return client.fetchAlerts().getAlerts().getAlert();
		}

}
