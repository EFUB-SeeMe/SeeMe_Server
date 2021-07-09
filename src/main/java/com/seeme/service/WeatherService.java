package com.seeme.service;

import com.seeme.api.WeatherOpenApi;
import com.seeme.domain.weather.TempResDto;

import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@AllArgsConstructor
public class WeatherService {
	private final WeatherOpenApi weatherOpenApi;
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH");

	public List<TempResDto> getTemperature(String nx, String ny) throws IOException, ParseException {

		Calendar cal = Calendar.getInstance();
		String time = TIME_FORMAT.format(cal.getTime());
		String date = DATE_FORMAT.format(cal.getTime());
		if (Integer.parseInt(time) <= 4) cal.add(Calendar.DATE,-1);
		List<JSONObject> temps = weatherOpenApi.getTempFromOpenApi(date, nx, ny);

		int order = 0;
		List<TempResDto> resultList = new ArrayList<>();
		for (Object temp: temps) {
			String resultTime;
			String resultIcon = "https://seeme.png";
			JSONObject jsonObject = (JSONObject) temp;
			if (jsonObject.get("fcstDate").equals(date)
				&& jsonObject.get("fcstTime").equals(time + "00")) {
				order = 1;
				resultTime = "현재";
			} else if (1 <= order && order <= 15) {
				resultTime = ((Integer.parseInt(time) + (order++)) % 24) + "시";
			} else continue;

			if (order == 8 || order == 9 || order == 11 ||
				order == 12 || order == 14 || order == 15)
				continue;
			resultList.add(TempResDto.builder()
				.time(resultTime)
				.temperature(jsonObject.get("fcstValue") + "°")
				.icon(resultIcon).build());
		}
		return resultList;
	}

}
