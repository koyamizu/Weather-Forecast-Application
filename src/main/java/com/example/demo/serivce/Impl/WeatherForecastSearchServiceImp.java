package com.example.demo.serivce.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.LocationData;
import com.example.demo.entity.WeatherApiClient;
import com.example.demo.open_ai_api.ChatGPT;
import com.example.demo.repository.WeatherForecastSearchMapper;
import com.example.demo.serivce.WeatherForecastSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	@Autowired
	private ChatGPT chatGPT;

	@Override
	public List<LocationData> findLocationData(String input){
		
		List<LocationData> locations=weatherForecastSearchMapper.selectLocations(input);
		
		//ユーザーが入力した地名がデータベースに存在した場合
		if(!locations.isEmpty()) {
			return locations;
		}
		//存在しない場合は、GPTに地名、行政区画名、座標（緯度経度）を出力してもらう
		locations= chatGPT.structureLocation(input).getLocations();
		//存在しない地名（例：「第三東京市」）を入力された時はnullを返却
		if(locations.isEmpty()) {
			return null;
		}
		
		weatherForecastSearchMapper.insertLocations(locations);
		return locations;
	}
	
	
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
			weatherForecastSearchMapper.insertDay(date, cityRegion, cityRegionRomaji, day);
			weatherForecastSearchMapper.insertHour(date, cityRegion, cityRegionRomaji, hours);

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
		
		List<AlertsAlert> alerts = client.fetchAlerts().getAlerts().getAlert();
		
		int size = alerts.size();
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(alerts.get(size - 1));
		
		return size > 0 ? chatGPT.translateAlert(jsonStr) : null;
	}

}
