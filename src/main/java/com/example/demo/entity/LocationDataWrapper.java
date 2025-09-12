package com.example.demo.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//LocationData配列をフィールドとして保持
//OpenAIAPIからの戻り値を格納する際、LocationData配列をラップする必要があったのでこのクラスを用意
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDataWrapper {

	@JsonProperty(required=true,value="locations")
	private List<LocationData> locations;
	
}
