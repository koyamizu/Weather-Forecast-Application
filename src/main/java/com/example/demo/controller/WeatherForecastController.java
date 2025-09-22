package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
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

	@PostMapping("search")
	public String search(RedirectAttributes attributes, @Validated WeatherForecastSearchForm form,
			BindingResult bindingResult, Model model, HttpSession session)
			throws JsonMappingException, JsonProcessingException, IOException {

		//		入力が空だった時
		if (bindingResult.hasErrors()) {
			attributes.addFlashAttribute("error_message", bindingResult.getAllErrors().get(0).getDefaultMessage());
			return "redirect:/";
		}

		//	LocationDataリストに、データベースから抽出したユーザー入力値、都市-行政区画名、緯度経度を格納
		List<LocationData> locations = service.findLocationData(form.getInput());
		session.setAttribute("date", form.getDate());

		if (locations.isEmpty()) {
			//データベースにデータがなかったとき
			session.setAttribute("input", form.getInput());
			//複数のクライアントから同時に検索されることを考慮して、jobIdを発行
			//jobIdにはランダム生成のコードを使用
			UUID uuid = UUID.randomUUID();
			session.setAttribute("jobId", uuid.toString());
			return "waiting";
		}

//		GPTにLocalDataのJSON配列を生成させた後との共通処理を記述。メソッドの中でRedirect先を指定している。
//		このメソッドは戻り値がStringになる。
		return checkSameLocationsAndReturnView(locations, session, model, attributes);
	}

	@GetMapping("observe")
	public SseEmitter observeResponse(HttpSession session) {
		
		SseEmitter emitter =new SseEmitter(0L);
		
	    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	    scheduler.scheduleAtFixedRate(() -> {
	        try {
	            emitter.send(SseEmitter.event().name("ping").data("keep-alive"));
	        } catch (IOException e) {
	            emitter.completeWithError(e);
	            scheduler.shutdown();
	        }
	    }, 0, 25, TimeUnit.SECONDS);
		
		String jobId=(String) session.getAttribute("jobId");
		String input=(String) session.getAttribute("input");
		//非同期処理開始→完了したら、クライアントにpush
		service.fetchLocationDataFromOpenAi(input, jobId,emitter);
		return emitter;
	}
	
	@GetMapping("processing")
	public String relayProcessing(HttpSession session,Model model,RedirectAttributes attributes) {
		String jobId=(String) session.getAttribute("jobId");
		List<LocationData> locations=service.getResult(jobId);
		return checkSameLocationsAndReturnView(locations, session, model, attributes);
	}

	private String checkSameLocationsAndReturnView(List<LocationData> locations, HttpSession session, Model model,
			RedirectAttributes attributes) {

		//		showResultView()で使用するので、セッションに保存
		session.setAttribute("locations", locations);

		LocalDate date = (LocalDate) session.getAttribute("date");

		if (locations.size() > 1) {
			//			同一地名が複数あった時
			//			[/choice]にredirect?
			//			LocationData[input,cityRegion,latlon]のうち、cityRegionのみを抽出
			List<String> choices = locations.stream().map(LocationData::getCityRegion).toList();
			model.addAttribute("confirm_message", "複数の候補が存在します。検索する地点を選択してください");
			model.addAttribute("choices", choices);
			model.addAttribute("date", date);
			return "choice";
		}
		//同一地名がなかった時
		attributes.addAttribute("cityRegion", locations.get(0).getCityRegion());
		attributes.addAttribute("date", date);

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
