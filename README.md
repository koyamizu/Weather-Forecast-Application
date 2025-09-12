# Weather-Forecast-Application
旅行先の天気を調べるアプリ

# 使用技術

- Java(21)
- Thymeleaf
- MyBatis
- Spring Boot
- Bootstrap 5
- WeatherAPI.com
- OpenAIAPI（使用モデル：ChatGPT5-mini）
- H2 Database
- JUnit5
- Heroku

# 概要

気象情報は[こちら](https://www.weatherapi.com/)のAPIから呼び出しております。Java用のライブラリが存在するとのことなので、GitHubからクローンして利用いたしました。ただ、Java7以前の技術で作成されたみたいで、バージョンの違いによるエラー対応に時間がかかりました。

こちらのAPIはクエリパラメータに都市名をそのまま使用できるのですが、日本語で入力すると想定外の場所の天気を調べられる場合があったので、基本的にはローマ字で入力するのが良さそうです。

ただ、**場所をいちいちローマ字で入力させる**アプリが、果たして「旅行者にとって『最適』」な天気予報検索アプリなのでしょうか。

また、私たちは必ずしも旅行先を都市名で認識しているとは限りません。例えば、ディズニーランドに行く予定のある人は、検索エンジンで「浦安市 天気」ではなく、「ディズニーランド 天気」と調べるのではないかと思われます。

幸運なことに、実はこのAPIではパラメータに緯度と経度も使用できます。つまり、緯度と経度さえわかれば具体的なランドマークの天気も調べられるわけです。

以上から、このアプリではOpenAIAPIを使用し、ユーザーの日本語入力を緯度と経度に変換して、WeatherAPIにリクエストを送ることにしました。

こうすることで、例えば「草津（群馬県草津町、滋賀県草津市）」などの同名地名や、「サンリオピューロランド」「PayPayドーム」などの具体的な建造物名でも検索が可能になりました。

ただ、やはりChatGPTによる処理が介在する分、初回の検索は少々時間がかかります。ChatGPT5の各モデルを比較したところ、miniが一番速かったのですが、それでも10秒~20秒かかる時があります。少しでも処理を早くするため、一度検索したことのある天気予報だけでなく、ユーザーの入力値とGPTが返した都市名行政区画名、そして緯度経度をデータベースに保存することにしました。例えば群馬県に行くために「草津」と検索したとき、「滋賀県草津町」の方もデータベースします。そうすることで、再度「草津」と検索した時、滋賀県の方のデータもすぐに返せるようになりました。

### トップ画面

<img width="1470" height="880" alt="トップページ" src="https://github.com/user-attachments/assets/be400942-a69d-4d99-ba96-e0287a990f82" />


### 15日先まで検索可能です
<img width="1470" height="880" alt="日付一覧" src="https://github.com/user-attachments/assets/6c1ed432-d442-41e2-a52e-db41c2dd6c35" />

### 結果画面

都道府県を入力したときは、県庁所在地のある自治体から調べるようになっております。
<img width="1470" height="880" alt="検索結果" src="https://github.com/user-attachments/assets/7d1ae08a-6f5b-4d0a-824d-ceb591672130" />

### 地名が重複しているとき
<img width="1470" height="880" alt="複数地名存在" src="https://github.com/user-attachments/assets/cb5e6e4f-a4c7-4c75-be17-3b3ab25e2930" />

滋賀県草津市
<img width="1470" height="880" alt="滋賀県草津市" src="https://github.com/user-attachments/assets/817715f6-e89e-4931-ade6-574ff3375fb8" />

群馬県草津町
<img width="1470" height="880" alt="群馬県草津町" src="https://github.com/user-attachments/assets/eb9ac9c3-2d16-4b02-87a4-57abf0bc3fbd" />

### 具体的なランドマークでも調べられます
<img width="1470" height="880" alt="サンリオピューロランド" src="https://github.com/user-attachments/assets/13868f1e-9183-4b55-b0a6-353749f6989f" />

### 気象警報がない時
<img width="1470" height="880" alt="気象警報なし" src="https://github.com/user-attachments/assets/1e005674-020d-4e05-9b8b-48fb780c57f3" />

### 気象警報がある時
<img width="1470" height="880" alt="気象警報1" src="https://github.com/user-attachments/assets/12ec24fe-2476-4ea0-b12e-5d9ed10d5132" />
<img width="1470" height="880" alt="気象警報2" src="https://github.com/user-attachments/assets/1d508959-7e82-49d1-b496-a32a4f40d817" />


### レスポンシブ対応(iPhone 12 PRO)
<img width="1470" height="880" alt="レスポンシブ対応1" src="https://github.com/user-attachments/assets/0dbdf638-164d-45a6-8422-321a40737be3" />

<img width="1470" height="880" alt="レスポンシブ対応2" src="https://github.com/user-attachments/assets/312d69d9-275e-42f4-aab0-021196cb1726" />

# テーブル構成

locationsのlatlonとは、緯度(latitude)と経度(longitude)のことです。

```sql
CREATE TABLE locations(
	id INT AUTO_INCREMENT PRIMARY KEY
	,input VARCHAR(50) NOT NULL
	,city_region VARCHAR(60) NOT NULL
	,latlon VARCHAR(30) NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE day_forecasts(
	date date NOT NULL
	,city_region VARCHAR(60) NOT NULL
	,max_temp DECIMAL(4,1) NOT NULL
	,min_temp  DECIMAL(4,1) NOT NULL
	,avg_temp DECIMAL(4,1) NOT NULL
  ,condition VARCHAR(23) NOT NULL
  ,icon VARCHAR(9) NOT NULL
	,daily_chance_of_rain DECIMAL(4,1) NOT NULL
	,avg_humidity DECIMAL(4,1) NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
	,PRIMARY KEY (date, city_region)
);

CREATE TABLE hour_forecasts(
	date date NOT NULL
	,time TIME NOT NULL
	,city_region VARCHAR(60) NOT NULL
	,temp DECIMAL(4,1) NOT NULL
  ,condition VARCHAR(23) NOT NULL
  ,icon VARCHAR(9) NOT NULL
	,chance_of_rain DECIMAL(4,1) NOT NULL
	,humidity DECIMAL(4,1) NOT NULL
	,created_at datetime DEFAULT CURRENT_TIMESTAMP
	,PRIMARY KEY (date, time, city_region)
);
```

# コード一覧

インポート編成は省略しております

### WeatherForecastController

```java

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
```

### WeatherForecastControllerAdvice

```java
@ControllerAdvice
public class WeatherForecastControllerAdvice {

//	500番台のエラーを処理 主にデータアクセスなど
	@ExceptionHandler({ DataAccessException.class, NullPointerException.class,
			JsonMappingException.class, JsonProcessingException.class, ApiException.class })
	public String showDatabaseErrorPage(Exception e) {
		e.printStackTrace();
		return "error/500";
	}
}
```

### LocationData

```java
@Data
@AllArgsConstructor
@RequiredArgsConstructor
//WeatherAPIライブラリの方に、すでにLocationというクラスが存在するので衝突防止にこの名称にしました。
public class LocationData {

//	ユーザーの入力
	@JsonProperty(required = true, value = "input")
	private String input;
	
//	「"都市名-行政区画名"」の形で格納 （例：新宿区-東京都）
	@JsonProperty(required = true, value = "city_region")
	private String cityRegion;
	
//	緯度経度 小数第四位までを「"latitude,longitude"」の形で格納
	@JsonProperty(required = true, value = "latlon")
	private String latlon;
}
```

### LocationDataWrapper

```java
//LocationData配列をフィールドとして保持
//OpenAIAPIからの戻り値を格納する際、LocationData配列をラップする必要があったのでこのクラスを用意
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDataWrapper {

	@JsonProperty(required=true,value="locations")
	private List<LocationData> locations;
	
}

```

### WeatherForecastServiceForm

```java
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
```

### WeatherForecastMapper

```java
//MyBatisのマッパーインスタンス
@Mapper
public interface WeatherForecastMapper {
	
//	1日の天気予報を抽出
	ForecastDay selectDay(String cityRegion, LocalDate date);
	
//	1時間ごとの天気予報を抽出
	List<ForecastHour> selectHour(String cityRegion, LocalDate date);
	
//	過去に検索したことのある場所（重複している場所名も含む）を抽出
	List<LocationData> selectLocations(String input);
	
//	1日の天気予報を挿入
	void insertDay(LocalDate date, String cityRegion,ForecastDay day);
	
//	1時間ごとの天気予報を挿入
	void insertHour(LocalDate date, String cityRegion,List<ForecastHour> hours);
	
//	検索結果の場所を挿入
	void insertLocations(List<LocationData> locations);
}
```

MyBatisのxmlファイル

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper
	namespace="com.example.demo.repository.WeatherForecastMapper">
		
	<!-- Condition -->
	<resultMap id="ConditionResult" type="io.swagger.client.model.ForecastCondition">
	  <id property="text" column="condition"/>
	  <result property="icon" column="icon"/>
	</resultMap>
	
	<!-- DayCondition -->
	<resultMap id="DayConditionResult" type="io.swagger.client.model.ForecastDayCondition">
	  <id property="text" column="condition"/>
	  <result property="icon" column="icon"/>
	</resultMap>
	
<!--	 Location -->
	<resultMap id="LocationDataResult" type="com.example.demo.entity.LocationData">
	  <id property="id" column="id"/>
	  <result property="input" column="input"/>
	  <result property="cityRegion" column="city_region"/>
	  <result property="latlon" column="latlon"/>
	</resultMap>
	
	<!-- ForecastDay -->
	<resultMap id="ForecastDayResult" type="io.swagger.client.model.ForecastDay">
	  <id property="dummy" column="dummy"/>
	  <result property="maxtempC" column="max_temp"/>
	  <result property="mintempC" column="min_temp"/>
	  <result property="avgtempC" column="avg_temp"/>
	  <result property="avghumidity" column="avg_humidity"/>
	  <result property="dailyChanceOfRain" column="daily_chance_of_rain"/>
	  <association property="condition" resultMap="DayConditionResult"/>
	</resultMap>
	
	<!-- ForecastHour -->
	<resultMap id="ForecastHourResult" type="io.swagger.client.model.ForecastHour">
	  <id property="time" column="time" typeHandler="com.example.demo.utility.StringTypeHandler"/>
	  <result property="tempC" column="temp"/>
	  <result property="humidity" column="humidity"/>
	  <result property="chanceOfRain" column="chance_of_rain"/>
	  <association property="condition" resultMap="ConditionResult"/>
	</resultMap>
	
	<select id="selectHour" resultMap="ForecastHourResult">
	  SELECT
	    date
	    ,time
	    ,city_region
	    ,temp
	    ,condition
	    ,icon
	    ,chance_of_rain
	    ,humidity
	  FROM
	    hour_forecasts
      WHERE
        date = #{date}
        AND city_region = #{cityRegion}
	  ;
	</select>
	
	<select id="selectDay" resultMap="ForecastDayResult">
	  SELECT
        date
        ,city_region
        ,max_temp
        ,min_temp
        ,avg_temp
        ,condition
        ,icon
        ,daily_chance_of_rain
        ,avg_humidity
      FROM
        day_forecasts
      WHERE
        date = #{date}
        AND city_region = #{cityRegion}
	  ;
	</select>
	
	<select id="selectLocations" resultMap="LocationDataResult">
	  SELECT
        input
        ,city_region
        ,latlon
      FROM
        locations
      WHERE
        input = #{input}
	  ;
	</select>
	
	<insert id="insertDay">
	  INSERT INTO
	    day_forecasts
	    (
	      date
	      ,city_region
	      ,max_temp
	      ,min_temp
	      ,avg_temp
	      ,condition
	      ,icon
	      ,daily_chance_of_rain
	      ,avg_humidity
	    )
	  VALUES
	    (
	    #{date}
	    ,#{cityRegion}
	    ,#{day.maxtempC}
	    ,#{day.mintempC}
	    ,#{day.avgtempC}
	    ,#{day.condition.text}
	    ,#{day.condition.icon}
	    ,#{day.dailyChanceOfRain}
	    ,#{day.avghumidity}
	    )
	  ;
	</insert>
	
	<insert id="insertHour">
	  INSERT INTO
	    hour_forecasts
	    (
	      date
	      ,time
	      ,city_region
	      ,temp
	      ,condition
	      ,icon
	      ,chance_of_rain
	      ,humidity
	    )
	  VALUES
	  <foreach collection="hours" item="hour" separator=",">
	    (
	    #{date}
	    ,#{hour.time}
	    ,#{cityRegion}
	    ,#{hour.tempC}
	    ,#{hour.condition.text}
	    ,#{hour.condition.icon}
	    ,#{hour.chanceOfRain}
	    ,#{hour.humidity}
	    )
	    </foreach>
	  ;
	</insert>
	
	<insert id="insertLocations">
	  INSERT INTO
	    locations
	    (
        input
        ,city_region
        ,latlon
	    )
	  VALUES
	  <foreach collection="locations" item="l" separator=",">
	    (
	    #{l.input}
	    ,#{l.cityRegion}
	    ,#{l.latlon}
	    )
	    </foreach>
	  ;
	</insert>
</mapper>
```

### WeatherForecastService

```java
public interface WeatherForecastService {
	
	ForecastForecastday findForecast(Optional<LocationData> gptResult,LocalDate date) throws JsonMappingException, JsonProcessingException, ApiException;

	String findAlerts(String city, LocalDate date) throws JsonMappingException, JsonProcessingException, ApiException;

	List<LocationData> findLocationData(String input);

}
```

### WeatherForecastServiceImpl

```java
//サービスクラス
@Service
@RequiredArgsConstructor
public class WeatherForecastServiceImpl implements WeatherForecastService {

	private final WeatherForecastMapper weatherForecastSearchMapper;
	private final ChatGPTApiClient chatGPT;

	//	ユーザーの入力した場所から、LocaitonDataオブジェクトを返却する
	@Override
	public List<LocationData> findLocationData(String input) {

		//		過去に検索した場所であれば、データベースに保存されてある
		List<LocationData> locations = weatherForecastSearchMapper.selectLocations(input);

		if (!locations.isEmpty()) {
			//ユーザーが入力した地名がデータベースに存在した場合
			return locations;
		}
		//データベースに存在しない場合は、GPTに地名、行政区画名、座標（緯度経度）を出力してもらう
		locations = chatGPT.structureLocation(input).getLocations();

		if (!locations.isEmpty()) {
			//			GPTがLocationData配列を返した時は、データベースに挿入
			weatherForecastSearchMapper.insertLocations(locations);
		}

		//存在しない地名（例：「第三東京市」）を入力された時はnullを返却
		return locations;
	}

	//	場所と日付から、ForecastForecastday(WeatherApiライブラリが用意したクラス)を返す
	@Override
	@Transactional
	public ForecastForecastday findForecast(Optional<LocationData> location, LocalDate date)
			throws JsonMappingException, JsonProcessingException, ApiException {

		ForecastForecastday forecastDay = new ForecastForecastday();

		//		locationはNull安全になっているが、普通はnullにならない（ここまで来た時点で何かしらの場所情報が入っているため）
		String cityRegion = location.orElse(null).getCityRegion();

		//		過去に検索したことがあれば、データベースから1日の天気予報が返される
		ForecastDay forecast = weatherForecastSearchMapper.selectDay(cityRegion, date);

		if (!Objects.equals(null, forecast)) {
			//			データベースに存在していれば、引き続き1時間ごとの天気情報を抽出する
			List<ForecastHour> hours = weatherForecastSearchMapper.selectHour(cityRegion, date);
			forecastDay.setDay(forecast);
			forecastDay.setHour(hours);
			return forecastDay;
		}

		//		緯度と経度
		String latlon = location.orElse(null).getLatlon();

		String dateStr = date.toString();
		WeatherApiClient client = new WeatherApiClient(latlon, dateStr);

		//			InlineResponse2002はWeatherApiライブラリが用意したクラス
		InlineResponse2002 resp = client.fetchWeather();

		forecastDay = resp.getForecast().getForecastday().get(0);
		ForecastDay day = forecastDay.getDay();
		List<ForecastHour> hours = forecastDay.getHour();

//		メソッドに宣言的トランザクションをしている(かつ、DataAccessExceptionは非検査例外である)ので、
//		データアクセスで例外が放出されたらinsertDay()とinsertHour()はロールバックされます
		weatherForecastSearchMapper.insertDay(date, cityRegion, day);
		weatherForecastSearchMapper.insertHour(date, cityRegion, hours);

		return forecastDay;
	}

	
//	緯度経度と日付から、気象警報を返す
	@Override
	public String findAlerts(String latlon, LocalDate date)
			throws JsonMappingException, JsonProcessingException, ApiException {

		String dateStr = date.toString();

		WeatherApiClient client = new WeatherApiClient(latlon, dateStr);

//		InlineResponse2001というクラスが返されるのですが、フィールドがネストされているのでゲッターが連続します
//		直でAlertオブジェクトを返すようにライブラリをいじってみたのですが、うまくいきませんでした
		List<AlertsAlert> alerts = client.fetchAlerts().getAlerts().getAlert();

		int size = alerts.size();

		if (size == 0) {
//			気象警報がなければnullを返す
			return null;
		}

		ObjectMapper mapper = new ObjectMapper();
//		size-1としているのは、配列で返される気象警報のうち、最新のものだけをビューに表示したいからです
//		GPTの処理時間を短くする狙いがあります
		String jsonStr = mapper.writeValueAsString(alerts.get(size - 1));

		return chatGPT.translateAlert(jsonStr);
	}

}
```

### WeatherApiClient

```java
//WeatherAPIを呼び出すためのクラス
public class WeatherApiClient {

	private String latlon;
//	java.time.LocalDateではない. WeatherAPIライブラリに準拠してorg.threeten.bp.LocalDateを使用
	private LocalDate date;
//	ライブラリが用意しているクラス.API呼び出しに使用.
	private ApisApi apiInstance;

	public WeatherApiClient(String latlon, String dateStr) {
		this.latlon = latlon;
		this.date = LocalDate.parse(dateStr);
	}

//	9:48 APIのキーをハードコーディングしてしまっているので、隠蔽できるように対処中です。
	static {
		ApiClient defaultClient = Configuration.getDefaultApiClient();
		ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
		ApiKeyAuth.setApiKey(APIキー);
	}

//	天気情報を取得
	public InlineResponse2002 fetchWeather() throws JsonMappingException, JsonProcessingException, ApiException {
		apiInstance = new ApisApi();
		return (InlineResponse2002) apiInstance.forecastWeather(latlon, 1, date, null, null, "ja", null, null, null);
	}
	
//	気象警報を取得
	public InlineResponse2001 fetchAlerts() throws JsonMappingException, JsonProcessingException, ApiException {
		apiInstance = new ApisApi();
		return (InlineResponse2001) apiInstance.forecastWeather(latlon, 1, date, null, null, null, "yes", null, null);
	}

}

```

### ChatGPTApiClient

```java
//OpenAIApiを呼び出すためのクラス
@Component
@RequiredArgsConstructor
public class ChatGPTApiClient{
		
	private final OpenAiChatModel chatModel;
	
//	ユーザーが入力した値から、LocationDataクラスと同じ構造をもったJSONを生成する
	public LocationDataWrapper structureLocation(String input) {
		
		var outputConverter =new BeanOutputConverter<>(LocationDataWrapper.class);
		String jsonSchema=outputConverter.getJsonSchema();
		ChatResponse response=chatModel.call(
				new Prompt("""
						「%s」という場所について、次のように変換してください。
						【変換ルール】
						1. 通常の市町村 → 「市町村名-都道府県/州」。
						2. 政令市や区を持つ都市 → 「区名-市名-都道府県/州」。
						   - 例: 清水寺 → "東山区-京都市-京都府"。
						   - 例: タイムズスクエア → "マンハッタン区-ニューヨーク市-ニューヨーク州"。
						3. 大まかな地名（都道府県や州など） → 検索に用いた座標に基づき最も近い行政区を city とする。
						   - 例: 東京 → "新宿区-東京都"。
						   - 例: カリフォルニア → "サクラメント市-カリフォルニア州"。
						4. 同名の候補が存在する場合 → 配列にすべて格納する。

						【制約】
						- 緯度経度は必ず "緯度,経度" の文字列で、小数点第4位まで。
						- 必ず JSON のみを返す。余計な文章は出力しない。
						- 架空の地名や存在しない場所については、空配列を返すこと。
						""".formatted(input),
						OpenAiChatOptions.builder()
						.responseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA,jsonSchema))
						.build()
						)
				);
		String content=response.getResult().getOutput().getText();
		return outputConverter.convert(content);
	}
	
//	引数で渡された気象警報を日本語に翻訳する
	public String translateAlert(String alertJson) {
		
		ChatResponse response=chatModel.call(
				new Prompt("以下に示す気象警報のJSONデータを解析し、日本語に翻訳してください。翻訳結果のみ出力すること「%s」"
						.formatted(alertJson),
						OpenAiChatOptions.builder().build()
						)
				);
		return response.getResult().getOutput().getText();
	}
}
```

### home.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title th:text="天気予報検索"></title>
		<link rel="stylesheet" th:href="@{/css/style.css}">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"
		integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
</head>

<body class="bg-light">
	<div class="container">
		<header>
		</header>
		<main>
			<h1 class="text-center mt-3">天気予報検索</h1>
			<div class="card mt-3">
				<div class="card-body">
				<form th:action="@{/search}" th:object="${weatherForecastSearchForm}" method="get"
					class="form-horizontal forecast-form">
					<div class="row mb-3">
						<label class="col-form-label col-md-2" for="date">日付</label>
						<div class="col-md-9">
							<select id="date" th:field="*{date}" class="form-select">
								<option th:each="date:${dates}" th:value="${date}" th:text="${date}"></option>
							</select>
						</div>
					</div>
					<div class="row mb-3">
						<label class="col-form-label col-md-2" for="input">旅行先</label>
						<div class="col-md-10">
							<input type="text" id="input" th:field="*{input}" class="form-control">
							<div th:if="error_message" class="form-text text-danger" th:text="${error_message}">エラーメッセージ</div>
						</div>
					</div>
					<div class="row">
						<div class="offset-md-2 col-md-10">
							<input type="submit" th:value="天気予報を表示" class="btn btn-primary">
						</div>
					</div>
				</form>
				</div>
			</div>
		</main>
	</div>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
		crossorigin="anonymous"></script>
</body>

</html>
```

### result.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>検索結果</title>
	<link rel="stylesheet" th:href="@{/css/style.css}">
	<link rel="preconnect" href="https://cdn.weatherapi.com">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"
		integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
</head>

<body>
	<header>
		<nav class="navbar bg-body-tertiary">
			<div class="container justify-content-center">
				<ul class="navbar-nav flex-row gap-3">
					<li class="nav-item">
						<a class="nav-link active px-3" aria-current="page" th:href="@{/}">戻る</a>
					</li>
					<li class="nav-item">
						<a class="nav-link px-3"
							th:href="@{/alert-info/{latlon}/{date}(latlon=${location.latlon},date=${date})}">
							気象警報
						</a>
					</li>
				</ul>
			</div>
		</nav>
	</header>
	<main>
		<script th:if="${alert_message} != null" th:inline="javascript">
			window.confirm(/*[[${alert_message}]]*/);
		</script>
		<div class="container-sm-fluid">
			<p th:if="${error_message}" th:text="${error_message}" class="text-danger text-center"></p>
			<h1 class="text-center mx-5 my-3">
				<span th:text="|${#temporals.format(date,'M月d日')}の天気|"></span>
			</h1>
			<h3 class="text-center mx-5">
				<span th:text="|@${location.input}" |></span>
				<span class="fs-5" th:text="|(${location.cityRegion})|"></span>
			</h3>
			<div th:object="${day}" class="day-weather mt-3">
				<img th:src="|https://cdn.weatherapi.com/weather/64x64/*{condition.icon}.png|" alt="1日の天気の画像"
					width="144" height="144" class="float-start" />
				<div class="float-none text-center">
					<h3 th:text="*{condition.text}">天気</h3>
					<div class="text-danger">最高&nbsp;&nbsp;<span th:text="*{maxtempC}"></span>°C</div>
					<div class="text-primary">最低&nbsp;&nbsp;<span th:text="*{mintempC}"></span>°C</div>
					<div>平均気温&nbsp;&nbsp;<span th:text="*{avgtempC}"></span>°C</div>
					<div>降水確率&nbsp;&nbsp;<span th:text="*{dailyChanceOfRain}"></span>%</div>
					<div>平均湿度&nbsp;&nbsp;<span th:text="*{avghumidity}"></span>%</div>
				</div>
			</div>
			<table border="1" class="table-bordered my-4" style="margin:0 auto">
				<tr class="bg-info">
					<th>時刻</th>
					<th>天気</th>
					<th>気温</th>
					<th>湿度</th>
					<th class="fs-md">降水確率</th>
				</tr>
				<tr th:each="hour : ${hours}" th:object="${hour}">
					<td th:text="*{time}"></td>
					<td>
						<div class="d-flex flex-column flex-md-row align-items-center justify-content-center">
							<img th:src="|https://cdn.weatherapi.com/weather/64x64/*{condition.icon}.png|"
								alt="1時間ごとの天気の画像" class="img-fluid" />
							<span th:text="*{condition.text}" class="ms-md-2 mt-2 mt-md-0"></span>
						</div>
					</td>
					<td th:text="*{tempC}"></td>
					<td th:text="*{#numbers.formatDecimal(humidity,2,0)}"></td>
					<td th:text="*{chanceOfRain}" th:classappend="*{chanceOfRain>0}? 'text-primary'"></td>
				</tr>
			</table>
		</div>
	</main>
	Powered by <a href="https://www.weatherapi.com/" title="Free Weather API">WeatherAPI.com</a>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
		crossorigin="anonymous"></script>
</body>

</html>
```

### choice.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title th:text="複数候補確認"></title>
	<link rel="stylesheet" th:href="@{/css/style.css}">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"
		integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
</head>

<body class="bg-light">
	<div class="container">
		<header>
		</header>
		<main>
			<p th:if="${confirm_message}" th:text="${confirm_message}" class="text-danger text-center mt-5"></p>
			<form th:action="@{/result}" method="get" class="forecast-form">
				<input type="hidden" name="date" th:value="${date}" />
				<div class="text-center mb-3">
					<div th:each="cityRegion:${choices}" class="form-check form-check-inline">
						<input type="radio" th:value="${cityRegion}" th:text="${cityRegion}" name="cityRegion">
					</div>
				</div>
				<div class="d-flex justify-content-center gap-3">
						<input type="submit" value="確定" class="btn btn-primary">
						<a th:href="@{/}" class="btn btn-outline-secondary">戻る</a>
				</div>
			</form>
		</main>
	</div>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
		crossorigin="anonymous"></script>
</body>

</html>
```

### 500.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head>
	<meta charset="UTF-8">
	<title>500</title>
</head>

<body>
	<h2>500-Internet Server Error</h2>
	<p>お手数ですが、しばらく時間をおいて再度お試しください。</p>
	<p>問題が解決しない場合は、管理者までお問い合わせください。</p>
	<a th:href="@{/}">ホーム画面へ</a>
</body>

</html>
```
