package com.seeme.service;

import com.seeme.domain.ResDto;
import com.seeme.domain.microdust.MicrodustResDto;
import com.seeme.domain.weather.*;
import com.seeme.service.api.WeatherOpenApi;
import com.seeme.util.ErrorMessage;
import com.seeme.util.MicrodustUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class WeatherService {

	private final WeatherOpenApi weatherOpenApi;

	public WeatherMainResDto getMain(Double lat, Double lon) throws IOException, ParseException {

		String locationCode =  weatherOpenApi.getLocationApi(lat, lon);
		WeatherMain main = weatherOpenApi.getMainApi(locationCode);
		// MIN MAX 구하기 ResDto forecast = getTemp

		return WeatherMainResDto.builder()
			.icon("")
			.errorMessage(ErrorMessage.SUCCESS)
			.document(main)
			.build();
	}

	private ResDto getMainResDto(String locationCode) {
		ResDto resDto;
		try {
			resDto = ResDto.builder()
				.resultCode(200)
				.errorMessage(null)
				.document(weatherOpenApi.getMainApi(locationCode))
				.build();
		} catch (ParseException | IOException e) {
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
