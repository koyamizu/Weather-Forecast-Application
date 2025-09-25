package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.LocationData;

import io.swagger.client.model.ForecastDay;
import io.swagger.client.model.ForecastHour;

//MyBatisのマッパーインスタンス
@Mapper
public interface WeatherForecastMapper {
	
//	1日の天気予報を抽出
	ForecastDay selectDay(String cityRegion, LocalDate date);
	
//	1時間ごとの天気予報を抽出
	List<ForecastHour> selectHour(String cityRegion, LocalDate date);
	
//	過去に検索したことのある場所（重複している場所名も含む）を抽出
	List<LocationData> selectLocations(String input);
	
//	緯度と経度を抽出
	String selectLatLon(String cityRegion);
	
//	1日の天気予報を挿入
	void insertDay(LocalDate date, String cityRegion,ForecastDay day);
	
//	1時間ごとの天気予報を挿入
	void insertHour(LocalDate date, String cityRegion,List<ForecastHour> hours);
	
//	検索結果の場所を挿入
	void insertLocations(List<LocationData> locations);
}
