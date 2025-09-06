package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.WeatherForecast;
import com.example.demo.form.WeatherForecastSearchForm;
import com.example.demo.serivce.SearchService;

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
	public String showResultView(@Validated WeatherForecastSearchForm form,
			BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "home";
		}
		
		List<WeatherForecast> weatherForecasts = service.findForecast(form.getCity(),form.getDate());
		
		
		model.addAttribute(weatherForecasts);
		model.addAttribute("date",form.getDate());
		model.addAttribute("city",form.getCity());
		
		return "result";
	}

}
