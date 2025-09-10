package com.example.demo.testdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.threeten.bp.LocalDate;

import io.swagger.client.model.Forecast;
import io.swagger.client.model.ForecastCondition;
import io.swagger.client.model.ForecastDay;
import io.swagger.client.model.ForecastDayCondition;
import io.swagger.client.model.ForecastForecastday;
import io.swagger.client.model.ForecastHour;
import io.swagger.client.model.InlineResponse2002;
import io.swagger.client.model.Location;

public class DummyData extends InlineResponse2002{

	public static InlineResponse2002 createTokyoForecast() {
        Location location = new Location()
                .name("Tokyo")
//                .country("japan")
                ;

        ForecastDay day = new ForecastDay()
                .maxtempC(BigDecimal.valueOf(31.3))
                .mintempC(BigDecimal.valueOf(25.9))
                .avgtempC(BigDecimal.valueOf(28.2))
                .avghumidity(BigDecimal.valueOf(67.0))
                .dailyChanceOfRain(BigDecimal.valueOf(0.0))
                .condition(new ForecastDayCondition().text("晴れ").icon("night/113"));

        List<ForecastHour> hours = new ArrayList<>();

        hours.add(new ForecastHour().time("00:00:00").tempC(BigDecimal.valueOf(26.0)).condition(new ForecastCondition().text("快晴").icon("night/113")).humidity(BigDecimal.valueOf(73.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("01:00:00").tempC(BigDecimal.valueOf(25.9)).condition(new ForecastCondition().text("所により曇り").icon("night/113")).humidity(BigDecimal.valueOf(74.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("02:00:00").tempC(BigDecimal.valueOf(25.9)).condition(new ForecastCondition().text("所により曇り").icon("night/113")).humidity(BigDecimal.valueOf(75.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("03:00:00").tempC(BigDecimal.valueOf(25.9)).condition(new ForecastCondition().text("所により曇り").icon("night/113")).humidity(BigDecimal.valueOf(76.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("04:00:00").tempC(BigDecimal.valueOf(25.9)).condition(new ForecastCondition().text("曇り").icon("night/113")).humidity(BigDecimal.valueOf(77.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("05:00:00").tempC(BigDecimal.valueOf(25.9)).condition(new ForecastCondition().text("所により曇り").icon("night/113")).humidity(BigDecimal.valueOf(77.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("06:00:00").tempC(BigDecimal.valueOf(25.9)).condition(new ForecastCondition().text("所により曇り").icon("night/113")).humidity(BigDecimal.valueOf(76.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("07:00:00").tempC(BigDecimal.valueOf(26.5)).condition(new ForecastCondition().text("所により曇り").icon("night/113")).humidity(BigDecimal.valueOf(73.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("08:00:00").tempC(BigDecimal.valueOf(27.4)).condition(new ForecastCondition().text("所により曇り").icon("night/113")).humidity(BigDecimal.valueOf(68.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("09:00:00").tempC(BigDecimal.valueOf(28.4)).condition(new ForecastCondition().text("所により曇り").icon("night/113")).humidity(BigDecimal.valueOf(63.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("10:00:00").tempC(BigDecimal.valueOf(29.4)).condition(new ForecastCondition().text("晴れ").icon("night/113")).humidity(BigDecimal.valueOf(59.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("11:00:00").tempC(BigDecimal.valueOf(30.2)).condition(new ForecastCondition().text("晴れ").icon("night/113")).humidity(BigDecimal.valueOf(56.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("12:00:00").tempC(BigDecimal.valueOf(30.8)).condition(new ForecastCondition().text("晴れ").icon("night/113")).humidity(BigDecimal.valueOf(55.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("13:00:00").tempC(BigDecimal.valueOf(31.2)).condition(new ForecastCondition().text("晴れ").icon("night/113")).humidity(BigDecimal.valueOf(54.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("14:00:00").tempC(BigDecimal.valueOf(31.3)).condition(new ForecastCondition().text("晴れ").icon("night/113")).humidity(BigDecimal.valueOf(54.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("15:00:00").tempC(BigDecimal.valueOf(31.1)).condition(new ForecastCondition().text("晴れ").icon("night/113")).humidity(BigDecimal.valueOf(55.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("16:00:00").tempC(BigDecimal.valueOf(30.5)).condition(new ForecastCondition().text("晴れ").icon("night/113")).humidity(BigDecimal.valueOf(58.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("17:00:00").tempC(BigDecimal.valueOf(31.0)).condition(new ForecastCondition().text("晴れ").icon("night/113")).humidity(BigDecimal.valueOf(63.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("18:00:00").tempC(BigDecimal.valueOf(28.8)).condition(new ForecastCondition().text("快晴").icon("night/113")).humidity(BigDecimal.valueOf(67.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("19:00:00").tempC(BigDecimal.valueOf(28.3)).condition(new ForecastCondition().text("快晴").icon("night/113")).humidity(BigDecimal.valueOf(71.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("20:00:00").tempC(BigDecimal.valueOf(28.2)).condition(new ForecastCondition().text("快晴").icon("night/113")).humidity(BigDecimal.valueOf(72.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("21:00:00").tempC(BigDecimal.valueOf(28.3)).condition(new ForecastCondition().text("快晴").icon("night/113")).humidity(BigDecimal.valueOf(72.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("22:00:00").tempC(BigDecimal.valueOf(28.2)).condition(new ForecastCondition().text("快晴").icon("night/113")).humidity(BigDecimal.valueOf(71.0)).chanceOfRain(BigDecimal.valueOf(0.0)));
        hours.add(new ForecastHour().time("23:00:00").tempC(BigDecimal.valueOf(28.0)).condition(new ForecastCondition().text("快晴").icon("night/113")).humidity(BigDecimal.valueOf(72.0)).chanceOfRain(BigDecimal.valueOf(0.0)));

        ForecastForecastday forecastDay = new ForecastForecastday()
                .date(LocalDate.of(2025, 9, 7))
                .day(day)
                .hour(hours);

        Forecast forecast = new Forecast().forecastday(List.of(forecastDay));

        return new InlineResponse2002()
                .location(location)
                .forecast(forecast);
    }
}
