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
                .name("東京都")
                .country("日本");

        ForecastDay day = new ForecastDay()
                .maxtempC(BigDecimal.valueOf(31.3))
                .mintempC(BigDecimal.valueOf(25.9))
                .avgtempC(BigDecimal.valueOf(28.2))
                .avghumidity(BigDecimal.valueOf(67))
                .dailyChanceOfRain(BigDecimal.ZERO)
                .condition(new ForecastDayCondition().text("晴れ"));

        List<ForecastHour> hours = new ArrayList<>();

        hours.add(new ForecastHour().time("2025-09-07 00:00").tempC(BigDecimal.valueOf(26.0)).condition(new ForecastCondition().text("快晴")).humidity(BigDecimal.valueOf(73)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 01:00").tempC(BigDecimal.valueOf(25.9)).condition(new ForecastCondition().text("所により曇り")).humidity(BigDecimal.valueOf(74)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 02:00").tempC(BigDecimal.valueOf(25.9)).condition(new ForecastCondition().text("所により曇り")).humidity(BigDecimal.valueOf(75)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 03:00").tempC(BigDecimal.valueOf(25.9)).condition(new ForecastCondition().text("所により曇り")).humidity(BigDecimal.valueOf(76)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 04:00").tempC(BigDecimal.valueOf(25.9)).condition(new ForecastCondition().text("曇り")).humidity(BigDecimal.valueOf(77)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 05:00").tempC(BigDecimal.valueOf(25.9)).condition(new ForecastCondition().text("所により曇り")).humidity(BigDecimal.valueOf(77)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 06:00").tempC(BigDecimal.valueOf(25.9)).condition(new ForecastCondition().text("所により曇り")).humidity(BigDecimal.valueOf(76)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 07:00").tempC(BigDecimal.valueOf(26.5)).condition(new ForecastCondition().text("所により曇り")).humidity(BigDecimal.valueOf(73)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 08:00").tempC(BigDecimal.valueOf(27.4)).condition(new ForecastCondition().text("所により曇り")).humidity(BigDecimal.valueOf(68)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 09:00").tempC(BigDecimal.valueOf(28.4)).condition(new ForecastCondition().text("所により曇り")).humidity(BigDecimal.valueOf(63)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 10:00").tempC(BigDecimal.valueOf(29.4)).condition(new ForecastCondition().text("晴れ")).humidity(BigDecimal.valueOf(59)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 11:00").tempC(BigDecimal.valueOf(30.2)).condition(new ForecastCondition().text("晴れ")).humidity(BigDecimal.valueOf(56)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 12:00").tempC(BigDecimal.valueOf(30.8)).condition(new ForecastCondition().text("晴れ")).humidity(BigDecimal.valueOf(55)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 13:00").tempC(BigDecimal.valueOf(31.2)).condition(new ForecastCondition().text("晴れ")).humidity(BigDecimal.valueOf(54)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 14:00").tempC(BigDecimal.valueOf(31.3)).condition(new ForecastCondition().text("晴れ")).humidity(BigDecimal.valueOf(54)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 15:00").tempC(BigDecimal.valueOf(31.1)).condition(new ForecastCondition().text("晴れ")).humidity(BigDecimal.valueOf(55)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 16:00").tempC(BigDecimal.valueOf(30.5)).condition(new ForecastCondition().text("晴れ")).humidity(BigDecimal.valueOf(58)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 17:00").tempC(BigDecimal.valueOf(31.0)).condition(new ForecastCondition().text("晴れ")).humidity(BigDecimal.valueOf(63)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 18:00").tempC(BigDecimal.valueOf(28.8)).condition(new ForecastCondition().text("快晴")).humidity(BigDecimal.valueOf(67)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 19:00").tempC(BigDecimal.valueOf(28.3)).condition(new ForecastCondition().text("快晴")).humidity(BigDecimal.valueOf(71)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 20:00").tempC(BigDecimal.valueOf(28.2)).condition(new ForecastCondition().text("快晴")).humidity(BigDecimal.valueOf(72)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 21:00").tempC(BigDecimal.valueOf(28.3)).condition(new ForecastCondition().text("快晴")).humidity(BigDecimal.valueOf(72)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 22:00").tempC(BigDecimal.valueOf(28.2)).condition(new ForecastCondition().text("快晴")).humidity(BigDecimal.valueOf(71)).chanceOfRain(BigDecimal.ZERO));
        hours.add(new ForecastHour().time("2025-09-07 23:00").tempC(BigDecimal.valueOf(28.0)).condition(new ForecastCondition().text("快晴")).humidity(BigDecimal.valueOf(72)).chanceOfRain(BigDecimal.ZERO));

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
