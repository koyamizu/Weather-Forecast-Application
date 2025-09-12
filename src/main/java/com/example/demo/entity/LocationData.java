package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
//WeatherAPIライブラリの方に、すでにLocationというクラスが存在するので衝突防止にこの名称にしました。
public class LocationData {

//	ユーザーの入力
	@JsonProperty(required = true, value = "input")
	private String input;
	
//	「"都市名-行政区画名"」の形で格納 （例：新宿区-東京都）
	@JsonProperty(required = true, value = "city_region")
	private String cityRegion;
	
//	緯度経度 小数第四位までを「"latitude,longitude"」の形で格納
	@JsonProperty(required = true, value = "latlon")
	private String latlon;
}
