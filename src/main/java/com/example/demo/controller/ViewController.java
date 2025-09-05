package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ViewController {

	private final SearchService service;

	@GetMapping("home")
	public String showHomeView() {
		return "home";
	}

	@PostMapping("result")
	public String showResultView(@Validated WeatherForecastSeachForm form,
			BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "home";
		}
		
		List<WeatherForecast> weatherForecasts = service.findForecastData(form.getCity,form.getDate);
		
//				if(weatherForecasts.isEmpty()) {
//					return "redirect:/https://api.weatherapi.com/v1/forecast.json"
//							+ "?key=92a675640403435d84a102334250509&q="+form.getCity+"&days=14&aqi=no&alerts=no";
//				}
		
		model.addAttribute(weatherForecasts);
		model.addAttribute("date",form.getDate);
		model.addAttribute("city",form.getCity);
		
		return "result";
	}

}
