package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.form.WeatherForecastSearchForm;
import com.example.demo.serivce.WeatherForecastSearchService;
import com.example.demo.utility.WeatherForecastSearchFormValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.model.InlineResponse2002;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ViewController {

	private final WeatherForecastSearchService service;
	private final WeatherForecastSearchFormValidator weatherForecastSearchFormValidator;

	@GetMapping("home")
	public String showHomeView() {
		return "home";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(this.weatherForecastSearchFormValidator);
	}
	
	@PostMapping("result")
	public String showResultView(@Validated WeatherForecastSearchForm form,
			BindingResult bindingResult, Model model) throws JsonMappingException, JsonProcessingException {
		
		if(bindingResult.hasErrors()) {
			return "home";
		}
		
		InlineResponse2002 weatherForecast = service.findForecast(form.getCity(),form.getDate());
		
		model.addAttribute(weatherForecast);
		model.addAttribute("date",form.getDate());
		model.addAttribute("city",form.getCity());
		
		return "result";
	}

}
