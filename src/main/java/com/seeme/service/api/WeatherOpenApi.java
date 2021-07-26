package com.seeme.service.api;

import com.seeme.domain.weather.*;
import com.seeme.util.WeatherUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import com.seeme.util.JSONParsingUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WeatherOpenApi {

	private final ApiConfig apiConfig;

	public WeatherMain getMainApi(String locationCode) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getWeatherMainUrl() + locationCode)
			.queryParam(WeatherUtil.API_KEY, apiConfig.getWeatherKey())
			.queryParam(WeatherUtil.LANGUAGE, "ko")
			.queryParam(WeatherUtil.DETAILS, "true");

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);
		JSONArray jsonArray = (JSONArray) JSONValue.parse(sb.toString());
		JSONObject jsonObject = (JSONObject) jsonArray.get(0);

		Integer iconNumber;
		String icon, iconText, comp;
		iconNumber = Integer.parseInt(jsonObject.get("WeatherIcon").toString());
		icon = WeatherUtil.getIcon(iconNumber);
		iconText = WeatherUtil.getIconDesc(iconNumber);

		JSONObject tempValueObject = (JSONObject) jsonObject.get("Temperature");
		JSONObject tempMetObject = (JSONObject) tempValueObject.get("Metric");

		JSONObject realValueObject = (JSONObject) jsonObject.get("RealFeelTemperature");
		JSONObject realMetObject = (JSONObject) realValueObject.get("Metric");

		JSONObject past24Object = (JSONObject) jsonObject.get("Past24HourTemperatureDeparture");
		JSONObject pastMetObject = (JSONObject) past24Object.get("Metric");
		Double compareObject = Double.parseDouble(pastMetObject.get("Value").toString());
		comp = WeatherUtil.getComp(compareObject);

		return WeatherMain.builder()
			.icon(icon)
			.iconDesc(iconText)
			.currTemp(Double.parseDouble(tempMetObject.get("Value").toString()))
			.feelTemp(Double.parseDouble(realMetObject.get("Value").toString()))
			.comp(comp)
			.build();

	}

	public String getLocationApi(Double lat, Double lon) throws IOException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getWeatherLocationUrl())
			.queryParam(WeatherUtil.API_KEY, apiConfig.getWeatherKey())
			.queryParam(WeatherUtil.Q, lat + "," + lon);

		URL url = new URL(uriComponentsBuilder.build().toUriString());
		System.out.println(url);

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);
		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());

		System.out.println("key" + jsonObject.get("Key").toString());
		return jsonObject.get("Key").toString();
	}

	public WeatherMainForecast getMainForecastApi(String locationCode) throws IOException, ParseException {

		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getWeatherForecastUrl() + locationCode)
			.queryParam(WeatherUtil.API_KEY, apiConfig.getWeatherKey())
			.queryParam(WeatherUtil.LANGUAGE, "ko")
			.queryParam(WeatherUtil.DETAILS, "true")
			.queryParam(WeatherUtil.METRIC, "true");

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);
		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
		JSONArray forecastArray = (JSONArray) jsonObject.get("DailyForecasts");
		JSONObject currentObjects = (JSONObject) forecastArray.get(0);

		JSONObject tempObjects = (JSONObject) currentObjects.get("Temperature");
		JSONObject maxObject = (JSONObject) tempObjects.get("Maximum");
		JSONObject minObject = (JSONObject) tempObjects.get("Minimum");

		String day = currentObjects.get("Date").toString();
		String time = WeatherUtil.getAMPM(day);

		JSONObject descObject = (JSONObject) currentObjects.get(time);

		return WeatherMainForecast.builder()
			.min(Double.parseDouble(minObject.get("Value").toString()))
			.max(Double.parseDouble(maxObject.get("Value").toString()))
			.desc(descObject.get("LongPhrase").toString())
			.build();
	}

	public List<WeatherTempResDto> getTimeTempApi(String locationCode) throws IOException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getWeatherTimeTempUrl())
			.path(locationCode)
			.queryParam(WeatherUtil.API_KEY, apiConfig.getWeatherKey())
			.queryParam(WeatherUtil.LANGUAGE, "ko")
			.queryParam(WeatherUtil.METRIC, true);

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);
		JSONArray jsonArray = (JSONArray) JSONValue.parse(sb.toString());

		List<WeatherTempResDto> temp = new ArrayList<>();
		int index = 0;
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			String time = (index++ == 0) ?
				("현재") : WeatherUtil.getTime(jsonObject.get("DateTime").toString());
			String temperature = ((JSONObject)
				jsonObject.get("Temperature")).get("Value").toString() + "°";
			String icon = jsonObject.get("WeatherIcon").toString();

			temp.add(
				WeatherTempResDto.builder()
					.time(time)
					.temperature(temperature)
					.icon(WeatherUtil.getWeatherIcon(icon))
					.build());
		}

		return temp;
	}


	public List<WeatherRainResDto> getTimeRainApi() {
		List<WeatherRainResDto> time = new ArrayList<>();
		time.add(
			WeatherRainResDto.builder()
				.time("현재")
				.rain(0)
				.percent(0)
				.icon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/rain/0.png")
				.build());
		time.add(
			WeatherRainResDto.builder()
				.time("18시")
				.rain(30)
				.percent(60)
				.icon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/rain/60.png")
				.build());
		time.add(
			WeatherRainResDto.builder()
				.time("19시")
				.rain(40)
				.percent(90)
				.icon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/rain/90.png")
				.build());
		time.add(
			WeatherRainResDto.builder()
				.time("20시")
				.rain(10)
				.percent(10)
				.icon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/rain/10.png")
				.build());
		return time;
	}

	public List<WeatherWeekResDto> getWeekApi(String locationCode) throws IOException, java.text.ParseException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getWeatherForecastUrl() + locationCode)
			.queryParam(WeatherUtil.API_KEY, apiConfig.getWeatherKey())
			.queryParam(WeatherUtil.LANGUAGE, "ko")
			.queryParam(WeatherUtil.DETAILS, "true")
			.queryParam(WeatherUtil.METRIC, "true");
		URL url = new URL(uriComponentsBuilder.build().toUriString());
		System.out.println(url);

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);
		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
		JSONArray forecastArray = (JSONArray) jsonObject.get("DailyForecasts");
		List<WeatherWeekResDto> week = new ArrayList<>();

		for (int idx = 0; idx < 5; idx++) {
			JSONObject dailyForecast = (JSONObject) forecastArray.get(idx);
			String day = dailyForecast.get("Date").toString();
			day = WeatherUtil.getDayOfWeek(day);

			JSONObject tempObjects = (JSONObject) dailyForecast.get("Temperature");
			JSONObject maxObjects = (JSONObject) tempObjects.get("Maximum");
			JSONObject minObjects = (JSONObject) tempObjects.get("Minimum");

			JSONObject AMObjects = (JSONObject) dailyForecast.get("Day");
			String AMIcon = WeatherUtil.getWeatherIcon(String.valueOf(AMObjects.get("Icon")));
			JSONObject AMRainObject = (JSONObject) AMObjects.get("Rain");

			JSONObject PMObjects = (JSONObject) dailyForecast.get("Day");
			String PMIcon = WeatherUtil.getWeatherIcon(String.valueOf(PMObjects.get("Icon")));
			JSONObject PMRainObject = (JSONObject) PMObjects.get("Rain");
			week.add(
				WeatherWeekResDto.builder()
					.day(day)
					.amRain(Double.parseDouble(AMRainObject.get("Value").toString()))
					.amRainPercent(Integer.parseInt(AMObjects.get("RainProbability").toString()))
					.amIcon(AMIcon)
					.pmRain(Double.parseDouble(PMRainObject.get("Value").toString()))
					.pmRainPercent(Integer.parseInt(PMObjects.get("RainProbability").toString()))
					.pmIcon(PMIcon)
					.max(Double.parseDouble(maxObjects.get("Value").toString()))
					.min(Double.parseDouble(minObjects.get("Value").toString()))
					.build()
			);
		}
		return week;
	}
}

		/*
		week.add(WeatherWeekResDto.builder()
			.day("6월 28일 (월)")
			.amRain(30).amIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Cloud.png")
			.pmRain(10).pmIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Cloud.png")
			.max(29).min(21)
			.build());
		week.add(WeatherWeekResDto.builder()
			.day("6월 29일 (화)")
			.amRain(30).amIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Rain-1.png")
			.pmRain(10).pmIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Rain-1.png")
			.max(29).min(21)
			.build());
		week.add(WeatherWeekResDto.builder()
			.day("6월 30일 (수)")
			.amRain(30).amIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Sun.png")
			.pmRain(10).pmIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Sun.png")
			.max(29).min(21)
			.build());
		week.add(WeatherWeekResDto.builder()
			.day("7월 1일 (목)")
			.amRain(30).amIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Sun.png")
			.pmRain(10).pmIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Thunder.png")
			.max(29).min(21)
			.build());
		week.add(WeatherWeekResDto.builder()
			.day("7월 2일 (금)")
			.amRain(30).amIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Cloud.png")
			.pmRain(10).pmIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Cloud.png")
			.max(29).min(21)
			.build());
		week.add(WeatherWeekResDto.builder()
			.day("7월 3일 (토)")
			.amRain(30).amIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Rain-1.png")
			.pmRain(10).pmIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Rain-2.png")
			.max(29).min(21)
			.build());
		week.add(WeatherWeekResDto.builder()
			.day("7월 4일 (일)")
			.amRain(30).amIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Rain-2.png")
			.pmRain(10).pmIcon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Cloud.png")
			.max(29).min(21)
			.build());


		return week;
	} */

