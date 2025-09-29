package com.example.demo.service.Impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.api.OpenAiApiClient;
import com.example.demo.api.WeatherApiClient;
import com.example.demo.api.WeatherApiClientFactory;
import com.example.demo.entity.LocationData;
import com.example.demo.repository.WeatherForecastMapper;
import com.example.demo.service.WeatherForecastService;
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
	private final OpenAiApiClient openAiApi;
	private final WeatherApiClientFactory clientFactory;
	private final Map<String, List<LocationData>> results = new ConcurrentHashMap<>();

	//	ユーザーの入力した場所から、データベースにアクセスしてLocaitonDataオブジェクトを返却する
	@Override
	public List<LocationData> findLocationData(String input) {
		List<LocationData> locationData = weatherForecastSearchMapper.selectLocations(input);
		return locationData;
	}

	//	OpenAiApiを呼び出し、ユーザーの入力値からLocationDataオブジェクト（地名、都市名-行政区画名、緯度経度）を生成する
	@Override
	@Async("Thread")
	public void fetchLocationDataFromOpenAi(String input, String jobId, SseEmitter emitter) throws IOException {

		emitter.send(SseEmitter.event().name("init").data("connected"));

		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

		// SSEが閉じられたらスケジューラも終了
		emitter.onCompletion(scheduler::shutdown);
		emitter.onTimeout(scheduler::shutdown);

		//		25秒ごとにダミーイベントを送る。push送信に30秒以上かかると、Herokuではタイムアウトになるため。
		scheduler.scheduleAtFixedRate(() -> {
			try {
				emitter.send(SseEmitter.event().name("ping").data("keep-alive"));
			} catch (IOException e) {
				emitter.completeWithError(e);
				scheduler.shutdown();
			}
		}, 0, 25, TimeUnit.SECONDS);

		try {
			//LocationDataオブジェクト（地名、都市名-行政区画名、緯度経度）を生成する
			List<LocationData> locations = openAiApi.generateLocationData(input).getLocations();

			if (locations.isEmpty()) {
				emitter.send(SseEmitter.event().name("not_found").data("error"));
			} else {
				weatherForecastSearchMapper.insertLocations(locations);
				//jobId、locationsのセットでデータを保持
				results.put(jobId, locations);
				emitter.send(SseEmitter.event().name("done").data("ok"));
			}
		} catch (IOException e) {
			emitter.completeWithError(e);
		}
	}

	//	jobIdに対応したLocationDataリストを返す
	@Override
	public List<LocationData> getResult(String jobId) {
		return results.remove(jobId);
	}

	//	場所と日付から、ForecastForecastday(WeatherApiライブラリが用意したクラス)を返す
	@Override
	@Transactional
	public ForecastForecastday findForecast(String cityRegion, LocalDate date)
			throws JsonMappingException, JsonProcessingException, ApiException {

		ForecastForecastday forecastDay = new ForecastForecastday();

		//		過去に検索したことがあれば、データベースから1日の天気予報が返される
		ForecastDay forecast = weatherForecastSearchMapper.selectDay(cityRegion, date);

		if (!Objects.equals(null, forecast)) {
			//			データベースに存在していれば、引き続き1時間ごとの天気情報を抽出する
			List<ForecastHour> hours = weatherForecastSearchMapper.selectHour(cityRegion, date);
			forecastDay.setDay(forecast);
			forecastDay.setHour(hours);
			return forecastDay;
		}

		//		データベースにデータがなければ、緯度と経度を用いてWeatherAPIにリクエストを送信

		//		緯度と経度を抽出
		String latlon = weatherForecastSearchMapper.selectLatLon(cityRegion);

		String dateStr = date.toString();

		WeatherApiClient client = clientFactory.create(latlon, dateStr);
		//			InlineResponse2002はWeatherApiライブラリが用意したクラス
		InlineResponse2002 resp = client.fetchWeather();

		forecastDay = resp.getForecast().getForecastday().get(0);
		ForecastDay day = forecastDay.getDay();
		List<ForecastHour> hours = forecastDay.getHour();

		//		メソッドに宣言的トランザクションを付与している(かつ、MyBatisのメソッドから放出されるDataAccessExceptionは非検査例外である)ので、
		//		データアクセスで例外が放出されたらinsertDay()とinsertHour()はロールバックされます
		weatherForecastSearchMapper.insertDay(date, cityRegion, day);
		weatherForecastSearchMapper.insertHour(date, cityRegion, hours);

		return forecastDay;
	}

	//	緯度経度と日付から、気象警報を返す
	@Override
	public String findAlerts(String cityRegion, LocalDate date)
			throws JsonMappingException, JsonProcessingException, ApiException {

		String dateStr = date.toString();
		String latlon = weatherForecastSearchMapper.selectLatLon(cityRegion);

		WeatherApiClient client = clientFactory.create(latlon, dateStr);

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
		//		OpenAiApiの処理時間を短くする狙いがあります
		String jsonStr = mapper.writeValueAsString(alerts.get(size - 1));

		return openAiApi.translateAlert(jsonStr);
	}

}
