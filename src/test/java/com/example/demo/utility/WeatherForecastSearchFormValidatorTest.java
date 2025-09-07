package com.example.demo.utility;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import com.example.demo.form.WeatherForecastSearchForm;


public class WeatherForecastSearchFormValidatorTest {
	
	@InjectMocks
	WeatherForecastSearchFormValidator validator;
	
	WeatherForecastSearchForm form;
	BindingResult result;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		form=new WeatherForecastSearchForm();
		result=new BindException(form,"WeatherForecastSearchForm");
		form.setCity("東京");
	}
	
	@Test
	void test_Enter_A_Date_Earlier_Than_Today() {
		form.setDate(LocalDate.now().minusDays(1));
		validator.validate(form, result);
		assertEquals(1,result.getFieldErrorCount());
		assertTrue(result.getFieldError("date").toString().contains("今日以降の日付を指定してください"));
	}
	
	@Test
	void test_Enter_A_Date_Same_Today() {
		form.setDate(LocalDate.now());
		validator.validate(form, result);
		assertEquals(0,result.getFieldErrorCount());
	}
	
	@Test
	void test_Enter_A_Date_More_Than_15_Days_Later() {
		form.setDate(LocalDate.now().plusDays(16));
		validator.validate(form, result);
		assertEquals(1,result.getFieldErrorCount());
		assertTrue(result.getFieldError("date").toString().contains("今日から15日以内までを指定してください"));
	}
	
	@Test
	void test_Enter_A_Date_More_Just_15_Days_Later() {
		form.setDate(LocalDate.now().plusDays(15));
		validator.validate(form, result);
		assertEquals(0,result.getFieldErrorCount());
	}
}
