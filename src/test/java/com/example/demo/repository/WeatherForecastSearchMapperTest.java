package com.example.demo.repository;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import io.swagger.client.model.InlineResponse2002;

@MybatisTest
@Transactional
public class WeatherForecastSearchMapperTest {

	@Autowired
	WeatherForecastSearchMapper mapper;
	
	@Test
	void test_insert() {
		InlineResponse2002 actual= mapper.select("Tokyo",LocalDate.now());
	}
}
