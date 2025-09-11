package com.example.demo.serivce.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.LocationData;
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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherForecastSearchServiceImp implements WeatherForecastSearchService {

	private final WeatherForecastSearchMapper weatherForecastSearchMapper;

	@Override
	@Transactional
	public ForecastForecastday findForecast(Optional<LocationData> location, LocalDate date)
			throws JsonMappingException, JsonProcessingException, ApiException {
		ForecastForecastday forecastDay = new ForecastForecastday();

		String cityRegionRomaji = location.orElse(null).getCityRegionRomaji();

		ForecastDay forecast = weatherForecastSearchMapper.selectDay(cityRegionRomaji, date);

		if (!Objects.equals(null, forecast)) {
			List<ForecastHour> hours = weatherForecastSearchMapper.selectHour(cityRegionRomaji, date);
			forecastDay.setDay(forecast);
			forecastDay.setHour(hours);
			return forecastDay;
		}

		String latlon = location.orElse(null).getLatlon();

		String dateStr = date.toString();
		WeatherApiClient client = new WeatherApiClient(latlon, dateStr);

		try {
			InlineResponse2002 resp = client.fetchWeather();

			forecastDay = resp.getForecast().getForecastday().get(0);
			ForecastDay day = forecastDay.getDay();
			List<ForecastHour> hours = forecastDay.getHour();

			String cityRegion = location.orElse(null).getCityRegionRomaji();
			weatherForecastSearchMapper.insertDay(date, cityRegionRomaji, cityRegion, day);
			weatherForecastSearchMapper.insertHour(date, cityRegionRomaji, cityRegion, hours);

		} catch (DataAccessException e) {
			//			DataAccessExceptionはRuntimeExceptionのサブクラスであり、このメソッドはトランザクションを宣言しているので、
			//			DataAccessExceptionが放出されるとinsertDay()とinsertHour()はロールバックされます。
			System.err.println("データアクセスにおいて例外が発生しました。");
			e.printStackTrace();
		}
		return forecastDay;
	}

	@Override
	public String findAlerts(String city, LocalDate date)
			throws JsonMappingException, JsonProcessingException, ApiException {
		String dateStr = date.toString();
		WeatherApiClient client = new WeatherApiClient(city, dateStr);
		List<AlertsAlert> alerts=client.fetchAlerts().getAlerts().getAlert();
//		String alert=GPTにAlertsを翻訳、要約してもらうメソッド(alerts);
		return null;
	}

}
