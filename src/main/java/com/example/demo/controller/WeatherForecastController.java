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
import com.example.demo.serivce.WeatherForecastService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.client.ApiException;
import io.swagger.client.model.ForecastForecastday;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WeatherForecastController {

	private final WeatherForecastService service;

//	ホーム画面を表示
	@GetMapping
	public String showHomeView(WeatherForecastSearchForm weatherForecastSearchForm, Model model) {
//		今日含めて16日間の日付リストを作成
		List<LocalDate> dates = IntStream.range(0, 16)
				.mapToObj(LocalDate.now()::plusDays).toList();
		model.addAttribute("dates", dates);
		return "home";
	}

	@GetMapping("search")
	public String search(RedirectAttributes attributes, @Validated WeatherForecastSearchForm form,
			BindingResult bindingResult, Model model, HttpSession session)
			throws JsonMappingException, JsonProcessingException {

//		入力が空だった時
		if (bindingResult.hasErrors()) {
			attributes.addFlashAttribute("error_message",bindingResult.getAllErrors().get(0).getDefaultMessage());
			return "redirect:/";
		}

//		LocationDataに、ユーザー入力値、都市-行政区画名、緯度経度を格納
		List<LocationData> locations = service.findLocationData(form.getInput());

		if (locations.isEmpty()) {
//			存在しない場所を入力した時や、ChatGPTが場所を検索（認識）できなかった時
			attributes.addFlashAttribute("error_message", "入力された地点は検索ができませんでした。");
			return "redirect:/";
		}

//		showResultView()で使用するので、セッションに保存
		session.setAttribute("locations", locations);

		if (locations.size() > 1) {
//			複数の候補が見つかった時
			
//			LocationData[input,cityRegion,latlon]のうち、cityRegionのみを抽出
			List<String> choices = locations.stream().map(LocationData::getCityRegion).toList();
			model.addAttribute("confirm_message", "複数の候補が存在します。検索する地点を選択してください");
			model.addAttribute("choices", choices);
			model.addAttribute("date", form.getDate());
			return "choice";
		}
		attributes.addAttribute("cityRegion", locations.get(0).getCityRegion());
		attributes.addAttribute("date", form.getDate());

		return "redirect:/result";
	}

//	検索結果画面を表示
	@GetMapping("result")
	public String showResultView(@RequestParam String cityRegion, @RequestParam LocalDate date, Model model,
			RedirectAttributes attributes, HttpSession session)
			throws JsonMappingException, JsonProcessingException, ApiException {

//		セッションに保存していたlocationsオブジェクトを取得
		@SuppressWarnings("unchecked")
		List<LocationData> locations = (List<LocationData>) session.getAttribute("locations");

//		LocationData配列locationsのうち、リクエストパラメータで渡されたcityRegionと一致するものを抽出
//		複数候補があった際に、配列の中から検索したい地点を抽出している
		Optional<LocationData> location = locations.stream().filter(gr -> gr.getCityRegion()
				.equals(cityRegion)).findFirst();

//		天気を検索し、結果を格納
		ForecastForecastday weatherForecast = service.findForecast(location, date);

		model.addAttribute("day", weatherForecast.getDay());
		model.addAttribute("hours", weatherForecast.getHour());
		model.addAttribute("date", date);
		model.addAttribute("location", location.orElse(null));

		return "result";
	}

//	気象警報を表示 latlonは緯度(latitude)と経度(longitude)
	@GetMapping("alert-info/{latlon}/{date}")
	public String getAlerts(@PathVariable String latlon, @PathVariable LocalDate date, Model model,
			RedirectAttributes attributes, HttpServletRequest request)
			throws JsonMappingException, JsonProcessingException, ApiException {

//		気象警報を格納
		String alert = service.findAlerts(latlon, date);

//		前のページ（/result?=xx&?=xx）を取得
		String referer = request.getHeader("Referer");

		if (Objects.equals(alert, null)) {
			attributes.addFlashAttribute("alert_message", "発表中の気象警報はありません");
			return "redirect:" + referer;
		}

		attributes.addFlashAttribute("alert_message", alert);
		return "redirect:" + referer;
	}

}
