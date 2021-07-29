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
		ResDto minmax = getMainMinMax(lat, lon);
		ResDto week = getWeek(lat, lon);

		return WeatherMainResDto.builder()
			.currentInfo(currents)
			.minmaxInfo(minmax)
			.weekInfo(week)
			.build();
	}

	private ResDto getMainMinMax(Double lat, Double lon) {
		ResDto resDto;
		try {
			resDto = ResDto.builder()
				.resultCode(200)
				.errorMessage(ErrorMessage.SUCCESS)
				.document(weatherOpenApi.getMainMinMaxApi(weatherOpenApi.getLocationApi(lat, lon)))
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
						.rain(Integer.parseInt(weatherTime.getRain()))
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
