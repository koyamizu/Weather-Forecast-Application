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

	@JsonProperty(required = true, value = "input")
	private String input;
	@JsonProperty(required = true, value = "city_region")
	private String cityRegion;
	@JsonProperty(required = true, value = "city_region_romaji")
	private String cityRegionRomaji;
	@JsonProperty(required = true, value = "latlon")
	private String latlon;
}
