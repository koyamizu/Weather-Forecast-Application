package com.example.demo.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.LocationData;
import com.example.demo.testdata.DummyData;

import io.swagger.client.model.ForecastDay;
import io.swagger.client.model.ForecastHour;

@MybatisTest
@Transactional
public class WeatherForecastSearchMapperTest {

	@Autowired
	WeatherForecastSearchMapper mapper;
	
	@Test
	void test_insertDay() {
		LocationData location=DummyData.createLocationData().get(0);
		ForecastDay dummy=DummyData.createForecastDay();
		mapper.insertDay(LocalDate.now(), location.getCityRegion(), location.getCityRegionRomaji(),dummy);
	}
	
	@Test
	void test_insertTime() {
		LocationData location=DummyData.createLocationData().get(0);
		List<ForecastHour> dummies=DummyData.createForecastHour();
		mapper.insertHour(LocalDate.now(), location.getCityRegion(), location.getCityRegionRomaji(),dummies);
	}
	
	@Test
	void test_inesrtLocations() {
		List<LocationData> locations=DummyData.createLocationData();
		mapper.insertLocations(locations);
	}
	
	@Sql("DummyDatabaseWeatherForecast.sql")
	@Test
	void test_selectDay() {
		ForecastDay expected=DummyData.createForecastDay();
		ForecastDay actual=mapper.selectDay("Shijuku-ku-Tokyo-to", LocalDate.parse("2025-09-07"));
		assertThat(actual).isEqualTo(expected);
	}
	
	@Sql("DummyDatabaseWeatherForecast.sql")
	@Test
	void test_selectHour() {
		List<ForecastHour> expected=DummyData.createForecastHour();
		List<ForecastHour> actual=mapper.selectHour("Shijuku-ku-Tokyo-to", LocalDate.parse("2025-09-07"));
		assertThat(actual).isEqualTo(expected);
	}
	
	@Sql("DummyDatabaseWeatherForecast.sql")
	@Test
	void test_selectLocations() {
		List<LocationData> locations=mapper.selectLocations("草津");
		List<LocationData> expecteds=List.of(
				new LocationData("草津","草津市-滋賀県","Kusatsu-shi-Shiga-ken","35.0094,135.9369")
				,new LocationData("草津","草津町-群馬県","Kusatsu-machi-Gunma-ken", "36.6269,138.6119")
				);
		assertThat(locations).isEqualTo(expecteds);
	}
}
