package com.example.demo.api;

import org.threeten.bp.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.ApisApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.InlineResponse2001;
import io.swagger.client.model.InlineResponse2002;

//WeatherAPIを呼び出すためのクラス
public class WeatherApiClient {

	private String latlon;
	//	java.time.LocalDateではない。WeatherAPIライブラリに準拠してorg.threeten.bp.LocalDateを使用
	private LocalDate date;
	//	ライブラリが用意しているクラス。API呼び出しに使用。
	private ApisApi apiInstance;

	public WeatherApiClient(String latlon, String dateStr, String apiKey) {
		this.latlon = latlon;
		this.date = LocalDate.parse(dateStr);
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		ApiKeyAuth.setApiKey(apiKey);
	}

	//	天気情報を取得
	public InlineResponse2002 fetchWeather() throws JsonMappingException, JsonProcessingException, ApiException {
		apiInstance = new ApisApi();
		return (InlineResponse2002) apiInstance.forecastWeather(latlon, 1, date, null, null, "ja", null, null, null);
	}

	//	気象警報を取得
	public InlineResponse2001 fetchAlerts() throws JsonMappingException, JsonProcessingException, ApiException {
		apiInstance = new ApisApi();
		return (InlineResponse2001) apiInstance.forecastWeather(latlon, 1, date, null, null, null, "yes", null, null);
	}

}
