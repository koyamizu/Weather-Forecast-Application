package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastDailyData {
	
	private Integer maxtemp_c;
	private Integer mintemp_c;
	private Integer avgtemp_c;
	private Integer code;
	private String text;
	private Integer dayilyChanceOfRain;
	private Integer avghumidity;

}
