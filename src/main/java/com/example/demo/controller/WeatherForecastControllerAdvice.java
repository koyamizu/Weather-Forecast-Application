package com.example.demo.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;

@ControllerAdvice
public class WeatherForecastControllerAdvice {

//	500番台のエラーを処理 主にデータアクセスなど
	@ExceptionHandler({ DataAccessException.class, NullPointerException.class,
			JsonMappingException.class, JsonProcessingException.class, ApiException.class })
	public String showDatabaseErrorPage(Exception e) {
		e.printStackTrace();
		return "error/500";
	}
}
