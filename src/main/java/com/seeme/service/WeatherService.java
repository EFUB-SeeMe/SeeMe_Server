package com.seeme.service;

import com.seeme.domain.ResDto;
import com.seeme.domain.weather.*;
import com.seeme.service.api.WeatherOpenApi;
import com.seeme.util.ErrorMessage;
import com.seeme.util.WeatherUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WeatherService {

	private final WeatherOpenApi weatherOpenApi;
	private final ClothesRepository clothesRepository;

	public WeatherMainResDto getMain(Double lat, Double lon) {

		try {
			String location = weatherOpenApi.getLocationApi(lat, lon);
			List<Weather> weather = weatherOpenApi.getForecastApi(location);
			WeatherMain current = weatherOpenApi.getMainApi(location);

			ResDto currentResDto = getCurrentResDto(current);
			ResDto minmaxResDto = getMainMinMaxResDto(weather);
			ResDto ootdResDto = getOotdResDto(currentResDto, minmaxResDto);
			ResDto weekResDto = getWeekResDto(weather);

			return WeatherMainResDto.builder()
				.currentInfo(currentResDto)
				.minmaxInfo(minmaxResDto)
				.weekInfo(weekResDto)
				.ootdInfo(ootdResDto)
				.build();
		} catch (IOException e) {
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

	private ResDto getMainMinMaxResDto(List<Weather> weatherList) {
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

	private ResDto getCurrentResDto(WeatherMain current) {
		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(current)
			.build();
	}

	public ResDto getWeekResDto(List<Weather> weatherList) {

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
		try {
			List<WeatherTime> weatherTimeList = weatherOpenApi.getTimeApi(
				weatherOpenApi.getLocationApi(lat, lon));
			return WeatherTimeResDto.builder()
				.tempInfo(getTempResDto(weatherTimeList))
				.rainInfo(getRainResDto(weatherTimeList))
				.build();
		} catch (IOException e) {
			e.printStackTrace();
			ResDto resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.JSON_PARSING_ERROR)
				.document(null)
				.build();

			return WeatherTimeResDto.builder()
				.tempInfo(resDto).rainInfo(resDto).build();
		} catch (Exception e) {
			e.printStackTrace();
			ResDto resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.UNKNOWN_ERROR)
				.document(null)
				.build();

			return WeatherTimeResDto.builder()
				.tempInfo(resDto).rainInfo(resDto).build();
		}
	}

	private ResDto getTempResDto(List<WeatherTime> weatherTimeList) {

		List<WeatherTempResDto> weatherTempResDtoList = new ArrayList<>();

		for (WeatherTime weatherTime : weatherTimeList) {

			weatherTempResDtoList.add(
				WeatherTempResDto.builder()
					.time(weatherTime.getTime())
					.temperature(weatherTime.getTemperature())
					.icon(weatherTime.getTempIcon())
					.build());
		}

		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(weatherTempResDtoList)
			.build();
	}

	private ResDto getRainResDto(List<WeatherTime> weatherTimeList) {

		List<WeatherRainResDto> weatherRainResDtoList = new ArrayList<>();

		for (WeatherTime weatherTime : weatherTimeList) {

			weatherRainResDtoList.add(
				WeatherRainResDto.builder()
					.time(weatherTime.getTime())
					.rain((int) Double.parseDouble(weatherTime.getRain()))
					.percent(Integer.parseInt(weatherTime.getPercent()))
					.icon(weatherTime.getRainIcon())
					.build());
		}

		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(weatherRainResDtoList)
			.build();
	}

	private ResDto getOotdResDto(ResDto currentResDto, ResDto mixmaxResDto) {
		// TODO: link data to temp and rainFlag variable;
		int temp = 33;
		Boolean rainFlag = false;

		WeatherOotdResDto ootd = WeatherOotdResDto.builder()
			.age10(getClothesResDto(temp, 10, false)) // update 3rd parameter to "rainFlag";
			.age20(getClothesResDto(temp, 20, false))
			.age30(getClothesResDto(temp, 30, false))
			.age40(getClothesResDto(temp, 40, true))
			.age50(getClothesResDto(temp, 50, true))
			.build();

		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(ootd)
			.build();
	}

	@Transactional
	public ClothesResDto getClothesResDto(int temp, int age, boolean rainFlag) {
		int iconNum1, iconNum2, iconNum3, iconNum4, index;
		String item1, item2, item3, item4;

		List<Clothes> topList = clothesRepository.findAllByTempAndAgeAndCategory(temp, age, "top");
		index = (int) (Math.random() * topList.size());
		iconNum1 = topList.get(index).getIconNum();
		item1 = topList.get(index).getDescription();

		List<Clothes> bottomList = clothesRepository.findAllByTempAndAgeAndCategory(temp, age, "bottom");
		index = (int) (Math.random() * bottomList.size());
		iconNum2 = bottomList.get(index).getIconNum();
		item2 = bottomList.get(index).getDescription();

		List<Clothes> shoesList = clothesRepository.findAllByTempAndAgeAndCategory(temp, age, "shoes");
		index = (int) (Math.random() * shoesList.size());
		iconNum3 = shoesList.get(index).getIconNum();
		item3 = shoesList.get(index).getDescription();

		if (rainFlag) {
			iconNum4 = 28;
			item4 = "우산 챙기세요!";
		} else {
			List<Clothes> itemList = clothesRepository.findAllByTempAndAgeAndCategory(temp, age, "item");
			index = (int) (Math.random() * itemList.size());
			iconNum4 = itemList.get(index).getIconNum();
			item4 = itemList.get(index).getDescription();
		}

		return ClothesResDto.builder()
			.item1(WeatherUtil.getClothesIcon(iconNum1)).item1Desc(item1)
			.item2(WeatherUtil.getClothesIcon(iconNum2)).item2Desc(item2)
			.item3(WeatherUtil.getClothesIcon(iconNum3)).item3Desc(item3)
			.item4(WeatherUtil.getClothesIcon(iconNum4)).item4Desc(item4)
			.build();
	}
}
