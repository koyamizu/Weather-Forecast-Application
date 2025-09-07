package com.example.demo.form;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherForecastSearchForm {
	
	@NotNull
	private LocalDate date;
	@NotNull
	private String city;
	
}
