package com.seeme.service.api;

import com.seeme.domain.weather.*;
import com.seeme.util.WeatherUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;

import com.seeme.util.JSONParsingUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.io.InputStreamReader;
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

		URL url = new URL(uriComponentsBuilder.build().toUriString());
		System.out.println(url);
		BufferedReader bf;
		bf = new BufferedReader(new InputStreamReader(url.openStream()));
		String result = bf.readLine();
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
		//StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);
		//JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());

		String icon, iconText, comp;
		JSONObject IconObject = (JSONObject) jsonObject.get("weatherIcon");
		icon = WeatherUtil.getIcon(Integer.parseInt(IconObject.toString()));
		iconText = WeatherUtil.getIconDesc(Integer.parseInt(IconObject.toString()));

		JSONObject tempValueObject = (JSONObject) jsonObject.get("Temperature");
		JSONObject tempMetObject = (JSONObject) tempValueObject.get("Metric");
		JSONObject tempObject = (JSONObject) tempMetObject.get("Value");

		JSONObject realValueObject = (JSONObject) jsonObject.get("RealFeelTemperature");
		JSONObject realMetObject = (JSONObject) realValueObject.get("Metric");
		JSONObject realObject = (JSONObject) realMetObject.get("Value");

		JSONObject past24Object = (JSONObject) jsonObject.get("Past24HourTemperatureDeparture");
		JSONObject pastMetObject = (JSONObject) past24Object.get("Metric");
		JSONObject compareObject = (JSONObject) pastMetObject.get("Value");
		comp = WeatherUtil.getComp(Double.parseDouble(compareObject.toString()));

		return WeatherMain.builder()
			.icon(icon)
			.iconDesc(iconText)
			.currTemp(Double.parseDouble(tempObject.toString()))
			.feelTemp(Double.parseDouble(realObject.toString()))
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
		System.out.println("1");
		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
		System.out.println(jsonObject);
		/*
		BufferedReader bf;
		System.out.println("1");
		bf = new BufferedReader(new InputStreamReader(url.openStream()));
		System.out.println("2");
		String result = bf.readLine();
		System.out.println("3");
		JSONObject jsonObject = (JSONObject) JSONValue.parse(result);
		System.out.println("4");
		*/
		System.out.println(jsonObject.get("Key").toString());
		return jsonObject.get("Key").toString();
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

