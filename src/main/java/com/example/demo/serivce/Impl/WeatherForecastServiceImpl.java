package com.example.demo.serivce.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.api.ChatGPTApiClient;
import com.example.demo.api.WeatherApiClient;
import com.example.demo.entity.LocationData;
import com.example.demo.repository.WeatherForecastMapper;
import com.example.demo.serivce.WeatherForecastService;
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

//サービスクラス
@Service
@RequiredArgsConstructor
public class WeatherForecastServiceImpl implements WeatherForecastService {

	private final WeatherForecastMapper weatherForecastSearchMapper;
	private final ChatGPTApiClient chatGPT;

	//	ユーザーの入力した場所から、LocaitonDataオブジェクトを返却する
	@Override
	public List<LocationData> findLocationData(String input) {

		//		過去に検索した場所であれば、データベースに保存されてある
		List<LocationData> locations = weatherForecastSearchMapper.selectLocations(input);

		if (!locations.isEmpty()) {
			//ユーザーが入力した地名がデータベースに存在した場合
			return locations;
		}
		//データベースに存在しない場合は、GPTに地名、行政区画名、座標（緯度経度）を出力してもらう
		locations = chatGPT.structureLocation(input).getLocations();

		if (!locations.isEmpty()) {
			//			GPTがLocationData配列を返した時は、データベースに挿入
			weatherForecastSearchMapper.insertLocations(locations);
		}

		//存在しない地名（例：「第三東京市」）を入力された時はnullを返却
		return locations;
	}

	//	場所と日付から、ForecastForecastday(WeatherApiライブラリが用意したクラス)を返す
	@Override
	@Transactional
	public ForecastForecastday findForecast(Optional<LocationData> location, LocalDate date)
			throws JsonMappingException, JsonProcessingException, ApiException {

		ForecastForecastday forecastDay = new ForecastForecastday();

		//		locationはNull安全になっているが、普通はnullにならない（ここまで来た時点で何かしらの場所情報が入っているため）
		String cityRegion = location.orElse(null).getCityRegion();

		//		過去に検索したことがあれば、データベースから1日の天気予報が返される
		ForecastDay forecast = weatherForecastSearchMapper.selectDay(cityRegion, date);

		if (!Objects.equals(null, forecast)) {
			//			データベースに存在していれば、引き続き1時間ごとの天気情報を抽出する
			List<ForecastHour> hours = weatherForecastSearchMapper.selectHour(cityRegion, date);
			forecastDay.setDay(forecast);
			forecastDay.setHour(hours);
			return forecastDay;
		}

		//		緯度と経度
		String latlon = location.orElse(null).getLatlon();

		String dateStr = date.toString();
		WeatherApiClient client = new WeatherApiClient(latlon, dateStr);

		//			InlineResponse2002はWeatherApiライブラリが用意したクラス
		InlineResponse2002 resp = client.fetchWeather();

		forecastDay = resp.getForecast().getForecastday().get(0);
		ForecastDay day = forecastDay.getDay();
		List<ForecastHour> hours = forecastDay.getHour();

//		メソッドに宣言的トランザクションをしている(かつ、DataAccessExceptionは非検査例外である)ので、
//		データアクセスで例外が放出されたらinsertDay()とinsertHour()はロールバックされます
		weatherForecastSearchMapper.insertDay(date, cityRegion, day);
		weatherForecastSearchMapper.insertHour(date, cityRegion, hours);

		return forecastDay;
	}

	
//	緯度経度と日付から、気象警報を返す
	@Override
	public String findAlerts(String latlon, LocalDate date)
			throws JsonMappingException, JsonProcessingException, ApiException {

		String dateStr = date.toString();

		WeatherApiClient client = new WeatherApiClient(latlon, dateStr);

//		InlineResponse2001というクラスが返されるのですが、フィールドがネストされているのでゲッターが連続します
//		直でAlertオブジェクトを返すようにライブラリをいじってみたのですが、うまくいきませんでした
		List<AlertsAlert> alerts = client.fetchAlerts().getAlerts().getAlert();

		int size = alerts.size();

		if (size == 0) {
//			気象警報がなければnullを返す
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();
//		size-1としているのは、配列で返される気象警報のうち、最新のものだけをビューに表示したいからです
//		GPTの処理時間を短くする狙いがあります
		String jsonStr = mapper.writeValueAsString(alerts.get(size - 1));

		return chatGPT.translateAlert(jsonStr);
	}

}
