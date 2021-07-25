package com.seeme.service.api;

import com.seeme.domain.weather.WeatherMainResDto;
import com.seeme.domain.weather.WeatherRainResDto;
import com.seeme.domain.weather.WeatherTempResDto;
import com.seeme.domain.weather.WeatherWeekResDto;
import com.seeme.util.WeatherUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WeatherOpenApi {

	private final ApiConfig apiConfig;

	public WeatherMainResDto getMainApi(String locationCode) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getWeatherMainUrl()+locationCode)
			.queryParam(WeatherUtil.API_KEY, apiConfig.getWeatherKey())
			.queryParam(WeatherUtil.DETAILS, "true")

		URL url = new URL(uriComponentsBuilder.build().toUriString());
		BufferedReader bf;
		bf = new BufferedReader(new InputStreamReader(url.openStream()));
		String result = bf.readLine();

		String icon, iconDesc, desc, comp;
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(result);

		JSONObject TextObject = (JSONObject) jsonObject.get("weatherText");
		JSONObject IconObject = (JSONObject) jsonObject.get("weatherIcon");
		desc = WeatherUtil.getDesc(TextObject.toString());
		icon = WeatherUtil.getIcon(Integer.parseInt(IconObject.toString()));
		iconDesc = WeatherUtil.getIconDesc(Integer.parseInt(IconObject.toString()));

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

		return WeatherMainResDto.builder()
			.icon(icon)
			.iconDesc(iconDesc)
			.currTemp(Integer.parseInt(tempObject.toString()))
			.feelTemp(Integer.parseInt(realObject.toString()))
			.max(29)
			.min(20)
			.desc(desc)
			.comp(comp)
			.build();
	}

	public String getLocationApi(Double lat, Double lon) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getWeatherLocationUrl())
			.queryParam(WeatherUtil.API_KEY, apiConfig.getWeatherKey())
			.queryParam(WeatherUtil.Q, lat+","+lon)
			.queryParam(WeatherUtil.DETAILS, "true");

		URL url = new URL(uriComponentsBuilder.build().toUriString());
		BufferedReader bf;
		bf = new BufferedReader(new InputStreamReader(url.openStream()));
		String result = bf.readLine();

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
		JSONObject detailObject = (JSONObject) jsonObject.get("Details");

		return detailObject.get("Key").toString();
	}

	public List<WeatherTempResDto> getTimeTempApi() {
		List<WeatherTempResDto> temp = new ArrayList<>();
		temp.add(
			WeatherTempResDto.builder()
				.time("현재")
				.temperature("25°")
				.icon("png60.png")
				.build());
		temp.add(
			WeatherTempResDto.builder()
				.time("18시")
				.temperature("24°")
				.icon("png60.png")
				.build());
		temp.add(
			WeatherTempResDto.builder()
				.time("19시")
				.temperature("23°")
				.icon("png60.png")
				.build());
		temp.add(
			WeatherTempResDto.builder()
				.time("20시")
				.temperature("22°")
				.icon("png60.png")
				.build());
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

	public List<WeatherWeekResDto> getweekApi() {
		List<WeatherWeekResDto> week = new ArrayList<>();
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
	}
}
