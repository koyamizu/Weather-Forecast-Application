package com.example.demo.form;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//トップページで使用するフォーム
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherForecastSearchForm {
	
	@NotNull
	private LocalDate date;
	@NotBlank(message="旅行先を入力してください")
	private String input;
	
}
