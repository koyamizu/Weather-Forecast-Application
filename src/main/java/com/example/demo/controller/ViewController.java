package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.form.WeatherForecastSearchForm;
import com.example.demo.serivce.WeatherForecastSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;
import io.swagger.client.model.AlertsAlert;
import io.swagger.client.model.ForecastForecastday;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ViewController {

	private final WeatherForecastSearchService service;
//	private final WeatherForecastSearchFormValidator weatherForecastSearchFormValidator;

	@GetMapping
	public String showHomeView(WeatherForecastSearchForm weatherForecastSearchForm, Model model) {
		List<LocalDate> dates = IntStream.range(0, 16)
				.mapToObj(LocalDate.now()::plusDays).toList();
		model.addAttribute("dates",dates);
		return "home";
	}

//	@InitBinder
//	public void initBinder(WebDataBinder binder) {
//		binder.addValidators(this.weatherForecastSearchFormValidator);
//	}

	@GetMapping("result")
	public String showResultView(@Validated WeatherForecastSearchForm form,
			BindingResult bindingResult, Model model,RedirectAttributes attributes) throws JsonMappingException, JsonProcessingException, ApiException {

		if (bindingResult.hasErrors()) {
			attributes.addFlashAttribute(bindingResult.getAllErrors());
			return "redirect:/home";
		}

		ForecastForecastday weatherForecast = service.findForecast(form.getCity(), form.getDate());

		model.addAttribute("day",weatherForecast.getDay());
		model.addAttribute("hours",weatherForecast.getHour());
		model.addAttribute("date", form.getDate());
		model.addAttribute("city", form.getCity());

		return "result";
	}
	
	@GetMapping("alert-info/{city}/{date}")
	public String getAlerts(@PathVariable String city,@PathVariable LocalDate date
			,Model model,RedirectAttributes attributes,HttpServletRequest request) throws JsonMappingException, JsonProcessingException, ApiException {
		
		
		List<AlertsAlert> alerts=service.findAlerts(city,date);
		
		if(alerts.isEmpty()) {
			attributes.addFlashAttribute("error_message", "発表中の気象警報はありません");
		    String referer = request.getHeader("Referer");
		    return "redirect:"+ referer;
		}
		
		model.addAttribute("alerts",alerts);
		model.addAttribute("date",date);
		model.addAttribute("city",city);
		
		return "alert-info";
	}

}
