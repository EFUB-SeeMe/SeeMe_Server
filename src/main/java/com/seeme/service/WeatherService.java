package com.seeme.service;

import com.seeme.domain.ResDto;
import com.seeme.domain.weather.*;
import com.seeme.service.api.WeatherOpenApi;
import com.seeme.util.ErrorMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WeatherService {

	private final WeatherOpenApi weatherOpenApi;

	public ResDto getMain(Double lat, Double lon) {
		WeatherMainResDto main = weatherOpenApi.getMainApi(); // add exception;
		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(main)
			.build();
	}

	public WeatherTimeResDto getTime(Double lat, Double lon) {
		ResDto temp = getTempResDto();
		ResDto rain = getRainResDto();
		ResDto ootd = getOotdResDto(temp, rain);
		return WeatherTimeResDto.builder()
			.tempInfo(temp)
			.rainInfo(rain)
			.ootdInfo(ootd)
			.build();
	}

	private ResDto getTempResDto() {
		List<WeatherTempResDto> temp = weatherOpenApi.getTimeTempApi(); // add exception;
		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(temp)
			.build();
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

	public ResDto getWeek(Double lat, Double lon) {
		List<WeatherWeekResDto> weekList = weatherOpenApi.getweekApi(); // add exception;
		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(weekList)
			.build();
	}
}
