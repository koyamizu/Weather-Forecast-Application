package com.example.demo.utility;

import java.time.LocalDate;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.form.WeatherForecastSearchForm;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WeatherForecastSearchFormValidation implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return WeatherForecastSearchForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		WeatherForecastSearchForm form = (WeatherForecastSearchForm) target;

		LocalDate today = LocalDate.now();
		if (!today.isAfter(form.getDate())) {
			errors.rejectValue("date", "WeatherForecastSearchForm.date"
					, "今日以降の日付を指定してください");
		}

		Boolean isOver14Days = today.until(form.getDate()).getDays() > 14;
		if (isOver14Days) {
			errors.rejectValue("date", "WeatherForecastSearchForm.date"
					, "今日から14日以内までを指定してください");
		}
	}
}
