package com.seeme.service.api;

import com.seeme.domain.weather.WeatherMainResDto;
import com.seeme.domain.weather.WeatherRainResDto;
import com.seeme.domain.weather.WeatherTempResDto;
import com.seeme.domain.weather.WeatherWeekResDto;
import com.seeme.util.JSONParsingUtil;
import com.seeme.util.WeatherUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WeatherOpenApi {

	private final ApiConfig apiConfig;

	public String getLocationApi(Double lat, Double lon) throws IOException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getWeatherLocationUrl())
			.queryParam(WeatherUtil.API_KEY, apiConfig.getWeatherKey())
			.queryParam(WeatherUtil.Q, lat + "," + lon);

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);
		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
		return jsonObject.get("Key").toString();
	}

	public WeatherMainResDto getMainApi() {
		return WeatherMainResDto.builder()
			.icon("https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Sun.png")
			.iconDesc("맑음")
			.currTemp(26)
			.feelTemp(28)
			.max(29)
			.min(20)
			.desc("오늘은 초복이에요! 기력을 보충해보는 건 어떨까요?")
			.comp("오늘은 어제와 비슷한 날씨가 예상됩니다.")
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
				("현재") : (jsonObject.get("DateTime").toString());
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
