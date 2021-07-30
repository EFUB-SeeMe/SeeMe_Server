package com.seeme.service;

import com.google.gson.Gson;
import com.seeme.domain.ResDto;
import com.seeme.domain.weather.*;
import com.seeme.service.api.WeatherOpenApi;
import com.seeme.util.ErrorMessage;
import com.seeme.util.WeatherUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WeatherService {

	private final WeatherOpenApi weatherOpenApi;

	public WeatherMainResDto getMain(Double lat, Double lon) {

		try {
			String location = weatherOpenApi.getLocationApi(lat, lon);
			List<Weather> weather = weatherOpenApi.getForecastApi(location);
			ResDto currents = getMainCurrent(location);
			ResDto minmax = getMainMinMax(weather);
			ResDto week = getWeek(weather);
			return WeatherMainResDto.builder()
				.currentInfo(currents)
				.minmaxInfo(minmax)
				.weekInfo(week)
				.build();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			ResDto resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.JSON_PARSING_ERROR)
				.document(null)
				.build();
			return WeatherMainResDto.builder()
				.currentInfo(resDto).minmaxInfo(resDto).weekInfo(resDto).build();
		} catch (Exception e) {
			e.printStackTrace();
			ResDto resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.UNKNOWN_ERROR)
				.document(null)
				.build();
			return WeatherMainResDto.builder()
				.currentInfo(resDto).minmaxInfo(resDto).weekInfo(resDto).build();
		}
	}

	private ResDto getMainMinMax(List<Weather> weatherList) {
		Weather weather = weatherList.get(0);

		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(WeatherMainMinMax.builder()
				.min(weather.getMin())
				.max(weather.getMax())
				.desc(weather.getDesc())
				.build())
			.build();
	}

	private ResDto getMainCurrent(String location) throws IOException, ParseException {
		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(weatherOpenApi.getMainApi(location))
			.build();
	}

	public ResDto getWeek(List<Weather> weatherList) {

		List<WeatherWeekResDto> week = new ArrayList<>();
		for (Weather weather : weatherList) {
			week.add(
				WeatherWeekResDto.builder()
					.day(weather.getDate())
					.amRain(weather.getAmRain())
					.amRainPercent(weather.getAmRainPercent())
					.amIcon(weather.getAmIcon())
					.pmRain(weather.getPmRain())
					.pmRainPercent(weather.getPmRainPercent())
					.pmIcon(weather.getPmIcon())
					.max(weather.getMax())
					.min(weather.getMin())
					.build()
			);
		}
		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(week)
			.build();
	}

	public WeatherTimeResDto getTime(Double lat, Double lon) throws IOException {
		try {
			List<WeatherTime> weatherTimeList = weatherOpenApi.getTimeApi(
				weatherOpenApi.getLocationApi(lat, lon));
			return WeatherTimeResDto.builder()
				.tempInfo(getTempResDto(weatherTimeList))
				.rainInfo(getRainResDto(weatherTimeList))
				.ootdInfo(getOotdResDto(
					getTempResDto(weatherTimeList), getRainResDto(weatherTimeList)))
				.build();
		} catch (IOException e) {
			e.printStackTrace();
			ResDto resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.JSON_PARSING_ERROR)
				.document(null)
				.build();

			return WeatherTimeResDto.builder()
				.tempInfo(resDto).rainInfo(resDto).ootdInfo(resDto).build();
		} catch (Exception e) {
			e.printStackTrace();
			ResDto resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.UNKNOWN_ERROR)
				.document(null)
				.build();

			return WeatherTimeResDto.builder()
				.tempInfo(resDto).rainInfo(resDto).ootdInfo(resDto).build();
		}
	}

	private ResDto getTempResDto(List<WeatherTime> weatherTimeList) {
		for (WeatherTime weatherTime : weatherTimeList) {

			if (!weatherTime.getTime().equals("-") && !weatherTime.getTempIcon().equals("-") &&
				!weatherTime.getTemperature().equals("-"))

			return ResDto.builder()
				.resultCode(200)
				.errorMessage(ErrorMessage.SUCCESS)
				.document(WeatherTempResDto.builder()
					.time(weatherTime.getTime())
					.temperature(weatherTime.getTemperature())
					.icon(weatherTime.getTempIcon())
					.build())
				.build();
		}

		WeatherTime weatherTime = weatherTimeList.get(0);

		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(WeatherTempResDto.builder()
				.time(weatherTime.getTime())
				.temperature(weatherTime.getTemperature())
				.icon(weatherTime.getTempIcon())
				.build())
			.build();

	}

	private ResDto getRainResDto(List<WeatherTime> weatherTimeList) {
		for (WeatherTime weatherTime : weatherTimeList) {

			if (!weatherTime.getTime().equals("-") && !weatherTime.getRain().equals("-") &&
				!weatherTime.getRainIcon().equals("-") && !weatherTime.getPercent().equals("-"))

				return ResDto.builder()
					.resultCode(200)
					.errorMessage(ErrorMessage.SUCCESS)
					.document(WeatherRainResDto.builder()
						.time(weatherTime.getTime())
						.rain((int)Double.parseDouble(weatherTime.getRain()))
						.percent(Integer.parseInt(weatherTime.getPercent()))
						.icon(weatherTime.getRainIcon())
						.build())
					.build();
		}

		WeatherTime weatherTime = weatherTimeList.get(0);

		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(WeatherRainResDto.builder()
				.time(weatherTime.getTime())
				.rain(Integer.parseInt(weatherTime.getRain()))
				.percent(Integer.parseInt(weatherTime.getPercent()))
				.icon(weatherTime.getRainIcon())
				.build())
			.build();

	}

	private ResDto getOotdResDto(ResDto temp, ResDto main) {
		// TODO: add clothes recommendation logic to here page
		Clothes clothes = Clothes.builder()
			.age(20)
			.top("top.png")
			.bottom("bottom.png")
			.shoes("shoes.png")
			.desc("아이템 설명")
			.reason("추천 이유")
			.build();
		WeatherOotdResDto ootd = WeatherOotdResDto.builder()
			.umbrellaIcon("umbrella.png")
			.umbrellaFlag(true)
			.clothes(clothes)
			.build();

		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(ootd)
			.build();
	}
}
