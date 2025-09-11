package com.example.demo.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LocationDataWrapper {

	@JsonProperty(required=true,value="locations")
	private List<LocationData> locations;
	
//	@JsonProperty(required=true,value="note")
//	private String note;
//	
}
