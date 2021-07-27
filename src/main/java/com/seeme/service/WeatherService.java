package com.seeme.service;

import com.seeme.domain.ResDto;
import com.seeme.domain.weather.*;
import com.seeme.service.api.WeatherOpenApi;
import com.seeme.util.ErrorMessage;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class WeatherService {

	private final WeatherOpenApi weatherOpenApi;

	public WeatherMainResDto getMain(Double lat, Double lon) {

		ResDto currents = getMainCurrent(lat, lon);
		ResDto forecast = getMainForecast(lat, lon);

		return WeatherMainResDto.builder()
			.currentInfo(currents)
			.forecastInfo(forecast)
			.build();
	}

	private ResDto getMainForecast(Double lat, Double lon) {
		ResDto resDto;
		try {
			resDto = ResDto.builder()
				.resultCode(200)
				.errorMessage(ErrorMessage.SUCCESS)
				.document(weatherOpenApi.getMainForecastApi(weatherOpenApi.getLocationApi(lat, lon)))
				.build();
		} catch (IOException e) {
			resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.JSON_PARSING_ERROR)
				.document(null)
				.build();
		} catch (Exception e) {
			resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.UNKNOWN_ERROR)
				.document(null)
				.build();
		}

		return resDto;
	}

	private ResDto getMainCurrent(Double lat, Double lon) {
		ResDto resDto;
		try {
			resDto = ResDto.builder()
				.resultCode(200)
				.errorMessage(ErrorMessage.SUCCESS)
				.document(weatherOpenApi.getMainApi(weatherOpenApi.getLocationApi(lat, lon)))
				.build();
		} catch (IOException | ParseException e) {
			resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.JSON_PARSING_ERROR)
				.document(null)
				.build();
		} catch (Exception e) {
			resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.UNKNOWN_ERROR)
				.document(null)
				.build();
		}

		return resDto;
	}

	public ResDto getWeek(Double lat, Double lon) {
		try {
			List<WeatherWeekResDto> week = weatherOpenApi.getWeekApi(
				weatherOpenApi.getLocationApi(lat, lon));
			return ResDto.builder()
				.resultCode(200)
				.errorMessage(ErrorMessage.SUCCESS)
				.document(week)
				.build();
		} catch (IOException | java.text.ParseException e) {
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

	public WeatherTimeResDto getTime(Double lat, Double lon) {
		ResDto temp = getTempResDto(lat, lon);
		ResDto rain = getRainResDto();
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

	private ResDto getRainResDto() {
		List<WeatherRainResDto> rain = weatherOpenApi.getTimeRainApi(); // add exception;
		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(rain)
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
