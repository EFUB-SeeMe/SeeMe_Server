package com.seeme.service;

import com.seeme.domain.ResDto;
import com.seeme.domain.weather.*;
import com.seeme.service.api.WeatherOpenApi;
import com.seeme.util.ErrorMessage;
import lombok.AllArgsConstructor;
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

	public WeatherTimeResDto getTime(Double lat, Double lon) {
		ResDto temp = getTempResDto(lat, lon);
		ResDto rain = getRainResDto(lat, lon);
		ResDto ootd = getOotdResDto(temp, rain);
		return WeatherTimeResDto.builder()
			.tempInfo(temp)
			.rainInfo(rain)
			.ootdInfo(ootd)
			.build();
	}

	private ResDto getTempResDto(Double lat, Double lon) {
		try {
			List<WeatherTempResDto> temp = weatherOpenApi.getTimeTempApi(
				weatherOpenApi.getLocationApi(lat, lon)); // add exception;
			return ResDto.builder()
				.resultCode(200)
				.errorMessage(ErrorMessage.SUCCESS)
				.document(temp)
				.build();
		} catch (IOException e) {
			return ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.JSON_PARSING_ERROR)
				.document(null)
				.build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.UNKNOWN_ERROR)
				.document(null)
				.build();
		}
	}

	private ResDto getRainResDto(Double lat, Double lon) {
		try {
			List<WeatherRainResDto> rain = weatherOpenApi.getTimeRainApi(
				weatherOpenApi.getLocationApi(lat, lon)); // add exception;
			return ResDto.builder()
				.resultCode(200)
				.errorMessage(ErrorMessage.SUCCESS)
				.document(rain)
				.build();
		} catch (IOException e) {
			return ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.JSON_PARSING_ERROR)
				.document(null)
				.build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.UNKNOWN_ERROR)
				.document(null)
				.build();
		}
	}

	private ResDto getOotdResDto(ResDto temp, ResDto main) {
		// TODO: add clothes recommendation logic to here page
		Clothes clothes = Clothes.builder()
			.item1("https://~.png")
			.item1Desc("하와이안 셔츠")
			.item2("https://~.png")
			.item2Desc("반바지")
			.item3("https://~.png")
			.item3Desc("선크림")
			.item4("https://~.png")
			.item4Desc("샌들")
			.build();

		WeatherOotdResDto ootd = WeatherOotdResDto.builder()
			.umbrellaIcon("https://~umbrella.png")
			.umbrellaFlag(true)
			.age10(clothes)
			.age20(clothes)
			.age30(clothes)
			.age40(clothes)
			.age50(clothes)
			.build();

		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(ootd)
			.build();
	}
}
