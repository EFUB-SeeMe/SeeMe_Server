package com.seeme.service.api;

import com.seeme.domain.weather.*;
import com.seeme.util.WeatherUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import com.seeme.domain.weather.WeatherRainResDto;
import com.seeme.domain.weather.WeatherTempResDto;
import com.seeme.domain.weather.WeatherWeekResDto;
import com.seeme.util.JSONParsingUtil;
import org.json.simple.JSONArray;

import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

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

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);
		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
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
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			String time = WeatherUtil.getTime(jsonObject.get("DateTime").toString());
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


	public List<WeatherRainResDto> getTimeRainApi(String locationCode) throws IOException, NullPointerException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getWeatherTimeTempUrl())
			.path(locationCode)
			.queryParam(WeatherUtil.API_KEY, apiConfig.getWeatherKey())
			.queryParam(WeatherUtil.LANGUAGE, "ko")
			.queryParam(WeatherUtil.DETAILS, true)
			.queryParam(WeatherUtil.METRIC, true);

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);
		JSONArray jsonArray = (JSONArray) JSONValue.parse(sb.toString());

		List<WeatherRainResDto> rain = new ArrayList<>();
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			String time = WeatherUtil.getTime(jsonObject.get("DateTime").toString());
			String rainAmount = ((JSONObject) jsonObject.get("Rain")).get("Value").toString();
			String percent = jsonObject.get("RainProbability").toString();

			rain.add(
				WeatherRainResDto.builder()
					.time(time)
					.rain((int)Double.parseDouble(rainAmount))
					.percent(Integer.parseInt(percent))
					.icon(WeatherUtil.getRainIcon(Integer.parseInt(percent)))
					.build());
		}

		return rain;
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