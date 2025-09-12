package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.api.ChatGPTApiClient;
import com.example.demo.entity.LocationData;
import com.example.demo.entity.LocationDataWrapper;
import com.example.demo.repository.WeatherForecastMapper;
import com.example.demo.serivce.Impl.WeatherForecastServiceImpl;
import com.example.demo.testdata.DummyData;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class WeatherForecastSearchServiceTest {

	@Mock
	WeatherForecastMapper mapper;
	
	@Mock
	ChatGPTApiClient chatGPT;
	
	@InjectMocks
	private WeatherForecastServiceImpl service;
	
	@Test
	void test_findLocationData() {
		List<LocationData> expecteds=List.of(
				new LocationData("東京", "新宿区-東京都", "35.6895, 139.6917")
				);
		doReturn(expecteds).when(mapper).selectLocations("東京");
		List<LocationData> actuals=service.findLocationData("東京");
		assertThat(actuals.size()).isEqualTo(expecteds.size());
		assertThat(actuals).containsExactlyInAnyOrderElementsOf(expecteds);
	}
	
	@Test
	void test_findLocationData_No_Data_In_Database() {
		List<LocationData> expecteds=List.of(
				new LocationData("東京", "新宿区-東京都", "35.6895, 139.6917")
				);
		var wrapper=new LocationDataWrapper();
		wrapper.setLocations(expecteds);
		doReturn(new ArrayList<LocationData>()).when(mapper).selectLocations("東京");
		doReturn(wrapper).when(chatGPT).structureLocation("東京");
		doNothing().when(mapper).insertLocations(expecteds);
		
		List<LocationData> actuals=service.findLocationData("東京");
		
		assertThat(actuals.size()).isEqualTo(expecteds.size());
		assertThat(actuals).containsExactlyInAnyOrderElementsOf(expecteds);
	}
	
	@Test
	void test_findLocationData_Imaginary_Location() {

		doReturn(new ArrayList<LocationData>()).when(mapper).selectLocations("第三東京市");
		doReturn(new LocationDataWrapper(new ArrayList<LocationData>())).when(chatGPT).structureLocation("第三東京市");
		
		List<LocationData> actuals=service.findLocationData("第三東京市");
		
		assertThat(actuals).isEmpty();
	}
	
	@Test
	void test_findLocationData_Duplicated_Location() {
		List<LocationData> expecteds=DummyData.createDuplicatedLocationData();
		var wrapper=new LocationDataWrapper();
		wrapper.setLocations(expecteds);
		doReturn(new ArrayList<LocationData>()).when(mapper).selectLocations("草津");
		doReturn(wrapper).when(chatGPT).structureLocation("草津");
		doNothing().when(mapper).insertLocations(expecteds);
		
		List<LocationData> actuals=service.findLocationData("草津");
		
		assertThat(actuals.size()).isEqualTo(expecteds.size());
		assertThat(actuals).containsExactlyInAnyOrderElementsOf(expecteds);
	}
}
