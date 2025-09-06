package com.example.demo.serivce.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.WeatherForecast;
import com.example.demo.serivce.SearchService;

import io.swagger.client.ApiClient;
import io.swagger.client.Configuration;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchServiceImp implements SearchService {
	
	 ApiClient defaultClient = Configuration.getDefaultApiClient();

	@Override
	public List<WeatherForecast> findForecast(String city, LocalDate date) {

		return null;
	}

}
