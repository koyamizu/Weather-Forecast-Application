package com.example.demo.serivce;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.entity.WeatherForecast;

public interface SearchService {
	
	List<WeatherForecast> findForecast(String city,LocalDate date);

}
