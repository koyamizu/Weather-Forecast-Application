package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.LocationData;
import com.example.demo.form.WeatherForecastSearchForm;
import com.example.demo.serivce.WeatherForecastSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.client.ApiException;
import io.swagger.client.model.ForecastForecastday;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WeatherForecastController {

	private final WeatherForecastSearchService service;

	@GetMapping
	public String showHomeView(WeatherForecastSearchForm weatherForecastSearchForm, Model model) {
		List<LocalDate> dates = IntStream.range(0, 16)
				.mapToObj(LocalDate.now()::plusDays).toList();
		model.addAttribute("dates", dates);
		return "home";
	}

	@GetMapping("search")
	public String search(RedirectAttributes attributes, @Validated WeatherForecastSearchForm form,
			BindingResult bindingResult, Model model, HttpSession session)
			throws JsonMappingException, JsonProcessingException {

		if (bindingResult.hasErrors()) {
			attributes.addFlashAttribute(bindingResult.getAllErrors());
			return "redirect:/";
		}
		//ここから下はドメインオブジェクトとして独立させる
		String gptResponse/* =入力値からJSONを返すメソッドを呼び出す。*/="Resp";

		ObjectMapper mapper = new ObjectMapper();
		List<LocationData> locations = mapper.readValue(gptResponse,
				new TypeReference<List<LocationData>>() {
				});
		//↑ここまで

		if (locations.isEmpty()) {
			attributes.addAttribute("error_message", "入力された地点は検索ができませんでした。");
			return "redirect:/";
		}

		session.setAttribute("locations", locations);

		if (locations.size() > 1) {
			List<String> choices = locations.stream().map(LocationData::getCityRegion).toList();
			model.addAttribute("error_message", "複数の候補が存在します。検索する地点を選択してください。");
			model.addAttribute("choices", choices);
			return "making-choices";
		}
		attributes.addAttribute("cityRegion", locations.get(0).getCityRegion());
		attributes.addAttribute("date", form.getDate());

		return "redirect:/result";
	}

	@GetMapping("result")
	public String showResultView(@RequestParam String cityRegion, @RequestParam LocalDate date, Model model,
			RedirectAttributes attributes, HttpSession session)
			throws JsonMappingException, JsonProcessingException, ApiException {

		@SuppressWarnings("unchecked")
		List<LocationData> locations = (List<LocationData>) session.getAttribute("locations");

		Optional<LocationData> location = locations.stream().filter(gr -> gr.getCityRegion()
				.equals(cityRegion)).findFirst();

		ForecastForecastday weatherForecast = service.findForecast(location, date);

		model.addAttribute("day", weatherForecast.getDay());
		model.addAttribute("hours", weatherForecast.getHour());
		model.addAttribute("date", date);
		model.addAttribute("location", location.orElse(null));

		return "result";
	}

	@GetMapping("alert-info/{latlon}/{date}")
	public String getAlerts(@PathVariable String latlon, @PathVariable LocalDate date, Model model,
			RedirectAttributes attributes, HttpServletRequest request)
			throws JsonMappingException, JsonProcessingException, ApiException {

		String alert = service.findAlerts(latlon, date);

		if (Objects.equals(alert, null)) {
			attributes.addFlashAttribute("error_message", "発表中の気象警報はありません");
			String referer = request.getHeader("Referer");
			return "redirect:" + referer;
		}

		model.addAttribute("alerts", alert);

		return "alert-info";
	}

}
