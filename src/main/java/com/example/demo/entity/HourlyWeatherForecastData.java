package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyWeatherForecastData {

	private LocalDateTime time;
	private String temp_c;
	private Integer code;
	private String text;
	private Integer humidity;
	private Integer chanceOfRain;

}
