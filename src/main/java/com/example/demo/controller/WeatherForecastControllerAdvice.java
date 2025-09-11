package com.example.demo.controller;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;

@ControllerAdvice
public class WeatherForecastControllerAdvice {

	@ExceptionHandler({ DataAccessResourceFailureException.class, BadSqlGrammarException.class,
			 TypeMismatchDataAccessException.class, NullPointerException.class,
			DataIntegrityViolationException.class,JsonMappingException.class
			, JsonProcessingException.class, ApiException.class })
	public String showDatabaseErrorPage(Exception e) {
		e.printStackTrace();
		return "error/500";
	}
}
