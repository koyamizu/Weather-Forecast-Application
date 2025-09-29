package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import jakarta.servlet.http.HttpServletRequest;

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
	private final Map<String, WeatherForecastSearchForm> forms = new ConcurrentHashMap<>();
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	//	ホーム画面を表示
	@GetMapping
	public String showHomeView(WeatherForecastSearchForm weatherForecastSearchForm, Model model) {
		//		今日含めて16日間の日付リストを作成
		List<LocalDate> dates = IntStream.range(0, 16)
				.mapToObj(LocalDate.now()::plusDays).toList();
		model.addAttribute("dates", dates);
		return "home";
	}

	//	土地データ（ユーザー入力値、都市-行政区画名、緯度経度）を検索
	@PostMapping("search")
	public String search(RedirectAttributes attributes, @Validated WeatherForecastSearchForm form,
			BindingResult bindingResult, Model model)
			throws JsonMappingException, JsonProcessingException, IOException {

		//		入力が空だった時
		if (bindingResult.hasErrors()) {
			attributes.addFlashAttribute("error_message", bindingResult.getAllErrors().get(0).getDefaultMessage());
			return "redirect:/";
		}

		//	LocationDataリストに、データベースから抽出したユーザー入力値、都市-行政区画名、緯度経度を格納
		List<LocationData> locations = service.findLocationData(form.getInput());

		if (locations.isEmpty()) {
			//データベースにデータがなかったとき

			//1ユーザーが複数のタブから同時に検索した時用に、ジョブごとにIDを発行。
			String jobId = UUID.randomUUID().toString();

			//jobIdとフォーム入力情報を結びつける
			forms.put(jobId, form);
			model.addAttribute("jobId", jobId);
			return "waiting";
		}

		//	共通処理　戻り値はString（リダイレクト先、またはview）
		return checkSameLocationsAndReturnView(model, attributes, form.getDate(), locations);
	}

	//	SSEからpushを受け取るために、リクエストを送る
	@GetMapping("observe")
	public SseEmitter observeResponse(@RequestParam String jobId) throws IOException {

		//0L = タイムアウト値無制限
		SseEmitter emitter = new SseEmitter(0L);
		emitter.send(SseEmitter.event().name("init").data("connected"));

		//		25秒ごとにダミーイベントを送る。push送信に30秒以上かかると、Herokuではタイムアウトになるため。
		scheduler.scheduleAtFixedRate(() -> {
		    try {
		        emitter.send(SseEmitter.event().name("ping").data("keep-alive"));
		    } catch (IOException e) {
		        emitter.completeWithError(e);
		        scheduler.shutdown();
		    }
		}, 0, 25, TimeUnit.SECONDS);

		var form = forms.get(jobId);
		// 非同期処理開始→完了したら、クライアントにpush送信
		// OpenAiApiに入力値を渡して、ユーザー入力値、都市-行政区画名、緯度経度のJSON配列に変換し、その結果をリストLocaitonDataに格納
		service.fetchLocationDataFromOpenAi(form.getInput(), jobId, emitter);
		return emitter;
	}

	//	クライアントにpush送信した後の処理
	@GetMapping("processing")
	public String relayProcessing(@RequestParam String jobId,
			Model model, RedirectAttributes attributes) {
        scheduler.shutdown();
		List<LocationData> locations = service.getResult(jobId);
		LocalDate date = forms.remove(jobId).getDate();
		//	共通処理　戻り値はString（リダイレクト先、またはview）
		return checkSameLocationsAndReturnView(model, attributes, date, locations);
	}

//	共通処理　同一名称の地名が複数あった時、どの場所の天気を調べるのかクライアントに返す。なければ/resultにリダイレクト。
	private String checkSameLocationsAndReturnView(Model model,
			RedirectAttributes attributes, LocalDate date, List<LocationData> locations) {

		String input = locations.get(0).getInput();

		if (locations.size() > 1) {
			//	同一地名が複数あった時
			//	LocationData[input,cityRegion,latlon]のうち、cityRegionのみを抽出
			List<String> choices = locations.stream().map(LocationData::getCityRegion).toList();
			model.addAttribute("confirm_message", "複数の候補が存在します。検索する地点を選択してください");
			model.addAttribute("choices", choices);
			model.addAttribute("date", date);
			model.addAttribute("input", input);
			return "choice";
		}
		//同一地名がなかった時
		attributes.addAttribute("date", date.toString());
		attributes.addAttribute("input", input);
		attributes.addAttribute("cityRegion", locations.get(0).getCityRegion());

		return "redirect:/result";
	}

	//	検索結果画面を表示
	@GetMapping("result")
	public String showResultView(@RequestParam LocalDate date, @RequestParam String input,
			@RequestParam String cityRegion, Model model,
			RedirectAttributes attributes)
			throws JsonMappingException, JsonProcessingException, ApiException {

		//		WeatherAPIにリクエストを送る or 過去の検索結果を返す
		ForecastForecastday weatherForecast = service.findForecast(cityRegion, date);

		model.addAttribute("day", weatherForecast.getDay());
		model.addAttribute("hours", weatherForecast.getHour());
		model.addAttribute("date", date);
		model.addAttribute("input", input);
		model.addAttribute("cityRegion", cityRegion);

		return "result";
	}

	//	気象警報を表示
	@GetMapping("alert-info/{cityRegion}/{date}")
	public String getAlerts(@PathVariable String cityRegion, @PathVariable LocalDate date, Model model,
			RedirectAttributes attributes, HttpServletRequest request)
			throws JsonMappingException, JsonProcessingException, ApiException {

		//		気象警報を格納
		String alert = service.findAlerts(cityRegion, date);

		//		前のページ（/result?=xx&?=xx&?=xx）を取得
		String referer = request.getHeader("Referer");

		if (Objects.equals(alert, null)) {
			attributes.addFlashAttribute("alert_message", "発表中の気象警報はありません");
			return "redirect:" + referer;
		}

		attributes.addFlashAttribute("alert_message", alert);
		return "redirect:" + referer;
	}

}
