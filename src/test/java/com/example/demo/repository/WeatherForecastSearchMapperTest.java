package com.example.demo.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.testdata.DummyData;

import io.swagger.client.model.ForecastForecastday;
import io.swagger.client.model.ForecastHour;
import io.swagger.client.model.InlineResponse2002;

@MybatisTest
@Transactional
public class WeatherForecastSearchMapperTest {

	@Autowired
	WeatherForecastSearchMapper mapper;
	
	@Test
	void test_insertDay() {
		InlineResponse2002 resp=DummyData.createTokyoForecast();
		ForecastForecastday ffd= resp.getForecast().getForecastday().get(0);
		mapper.insertDay(LocalDate.parse(ffd.getDate().toString()), resp.getLocation(), ffd.getDay());
	}
	
	@Test
	void test_insertTime() {
		InlineResponse2002 resp=DummyData.createTokyoForecast();
		ForecastForecastday ffd= resp.getForecast().getForecastday().get(0);
		mapper.insertHour(LocalDate.parse(ffd.getDate().toString()),resp.getLocation(), ffd.getHour());
	}
	
	@Sql("DummyDatabaseWeatherForecast.sql")
	@Test
	void test_selectDay() {
		InlineResponse2002 actual=mapper.selectDay("Tokyo", LocalDate.parse("2025-09-07"));
		assertThat(actual).isNotNull();
	}
	
	@Sql("DummyDatabaseWeatherForecast.sql")
	@Test
	void test_selectHour() {
		List<ForecastHour> actual=mapper.selectHour("Tokyo", LocalDate.parse("2025-09-07"));
		assertThat(actual).isNotEmpty();
	}
}
