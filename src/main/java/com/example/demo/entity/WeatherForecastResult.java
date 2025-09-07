package com.example.demo.entity;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastResult {
	
	private String name;
	private String country;
	private LocalDate date;
	private DailyWeatherForecast dailyData;
	private List<HourlyWeatherForecastData> hourlyData;
}
