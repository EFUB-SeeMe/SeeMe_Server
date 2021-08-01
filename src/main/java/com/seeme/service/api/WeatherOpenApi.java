package com.seeme.service.api;

import com.seeme.config.ApiConfig;
import com.seeme.domain.weather.*;
import com.seeme.util.WeatherUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import com.seeme.util.JSONParsingUtil;
import org.json.simple.JSONArray;

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

	public WeatherMain getMainApi(String locationCode) throws IOException {
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
		icon = WeatherUtil.getWeatherIcon(iconNumber);
		iconText = WeatherUtil.getIconDesc(iconNumber);

		JSONObject tempValueObject = (JSONObject) jsonObject.get("Temperature");
		JSONObject tempMetObject = (JSONObject) tempValueObject.get("Metric");

		JSONObject realValueObject = (JSONObject) jsonObject.get("RealFeelTemperature");
		JSONObject realMetObject = (JSONObject) realValueObject.get("Metric");

		JSONObject past24Object = (JSONObject) jsonObject.get("Past24HourTemperatureDeparture");
		JSONObject pastMetObject = (JSONObject) past24Object.get("Metric");
		Integer compareValue = (int) Double.parseDouble(pastMetObject.get("Value").toString());
		comp = WeatherUtil.getComp(compareValue);

		return WeatherMain.builder()
			.icon(icon)
			.iconDesc(iconText)
			.currTemp((int) Double.parseDouble(tempMetObject.get("Value").toString()))
			.feelTemp((int) Double.parseDouble(realMetObject.get("Value").toString()))
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

	public List<Weather> getForecastApi(String locationCode) throws IOException, java.text.ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getWeatherForecastUrl() + locationCode)
			.queryParam(WeatherUtil.API_KEY, apiConfig.getWeatherKey())
			.queryParam(WeatherUtil.LANGUAGE, "ko")
			.queryParam(WeatherUtil.DETAILS, "true")
			.queryParam(WeatherUtil.METRIC, "true");

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);
		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
		JSONArray forecastArray = (JSONArray) jsonObject.get("DailyForecasts");

		List<Weather> weather = new ArrayList<>();
		for (Object object : forecastArray) {
			JSONObject dailyForecast = (JSONObject) object;

			String day = dailyForecast.get("Date").toString();
			String date = WeatherUtil.getDayOfWeek(day);

			JSONObject tempObjects = (JSONObject) dailyForecast.get("Temperature");
			JSONObject maxObjects = (JSONObject) tempObjects.get("Maximum");
			JSONObject minObjects = (JSONObject) tempObjects.get("Minimum");

			JSONObject AMObjects = (JSONObject) dailyForecast.get("Day");
			String AMIcon = WeatherUtil.getWeatherIcon(Integer.parseInt(AMObjects.get("Icon").toString()));
			JSONObject AMRainObject = (JSONObject) AMObjects.get("Rain");

			JSONObject PMObjects = (JSONObject) dailyForecast.get("Night");
			String PMIcon = WeatherUtil.getWeatherIcon(Integer.parseInt(PMObjects.get("Icon").toString()));
			JSONObject PMRainObject = (JSONObject) PMObjects.get("Rain");

			String time = WeatherUtil.getAMPM(day);
			JSONObject descObject = (JSONObject) dailyForecast.get(time);

			weather.add(
				Weather.builder()
					.date(date)
					.amRain((int) Double.parseDouble(AMRainObject.get("Value").toString()))
					.amRainPercent(Integer.parseInt(AMObjects.get("RainProbability").toString()))
					.amIcon(AMIcon)
					.pmRain((int) Double.parseDouble(PMRainObject.get("Value").toString()))
					.pmRainPercent(Integer.parseInt(PMObjects.get("RainProbability").toString()))
					.pmIcon(PMIcon)
					.max((int) Double.parseDouble(maxObjects.get("Value").toString()))
					.min((int) Double.parseDouble(minObjects.get("Value").toString()))
					.desc(descObject.get("LongPhrase").toString())
					.build()
			);
		}
		return weather;
	}

	public List<WeatherTime> getTimeApi(String locationCode) throws IOException, NullPointerException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getWeatherTimeTempUrl())
			.path(locationCode)
			.queryParam(WeatherUtil.API_KEY, apiConfig.getWeatherKey())
			.queryParam(WeatherUtil.LANGUAGE, "ko")
			.queryParam(WeatherUtil.DETAILS, true)
			.queryParam(WeatherUtil.METRIC, true);

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);
		JSONArray jsonArray = (JSONArray) JSONValue.parse(sb.toString());

		List<WeatherTime> times = new ArrayList<>();
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			String time = WeatherUtil.getTime(jsonObject.get("DateTime").toString());
			String rainAmount = ((JSONObject) jsonObject.get("Rain")).get("Value").toString();
			String percent = jsonObject.get("RainProbability").toString();
			String temperature = ((JSONObject)
				jsonObject.get("Temperature")).get("Value").toString() + "°";
			String tempIcon = jsonObject.get("WeatherIcon").toString();

			times.add(
				WeatherTime.builder()
					.time(time)
					.rain(rainAmount)
					.percent(percent)
					.temperature(temperature)
					.rainIcon(WeatherUtil.getRainIcon(Double.parseDouble(percent)))
					.tempIcon(WeatherUtil.getWeatherIcon(Integer.parseInt(tempIcon)))
					.build());
		}

		return times;
	}

}