package com.example.demo.entity;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
//WeatherAPIライブラリの方に、すでにLocationというクラスが存在するので衝突防止にこの名称にしました。
public class LocationData {

	@SerializedName("input")
	private String input;
	@SerializedName("city_region")
	private String cityRegion;
	@SerializedName("city_region_romaji")
	private String cityRegionRomaji;
	@SerializedName("latlon")
	private String latlon;
}
