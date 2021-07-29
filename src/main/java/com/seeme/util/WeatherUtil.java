package com.seeme.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class WeatherUtil {
	public static final Double DEFAULT_LAT = 37.123456;
	public static final Double DEFAULT_LON = 127.123456;
	public static final String API_KEY = "apikey";
	public static final String Q = "q";
	public static final String DETAILS = "details";
	public static final String LANGUAGE = "language";
	public static final String METRIC = "metric";
	public static final String WEATHER_ICON_PREFIX =
		"https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/";
	public static final String RAIN_ICON_PREFIX =
		"https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/rain/";

	public static String getWeatherIcon(Integer iconNumber) {
		String icon;
		switch (iconNumber) {
			case 1: case 2: case 3:
				icon = "Sun";
				break;
			case 15: case 18: case 26: case 29: case 41: case 42:
				icon = "Rain-1";
				break;
			case 12: case 39: case 40:
				icon = "Rain-2";
				break;
			case 7: case 11: case 38:
				icon = "Cloud";
				break;
			case 22: case 24: case 44:
				icon = "Snow";
				break;
			case 4: case 5: case 6:
				icon = "Cloudy-Sun";
				break;
			case 16: case 17:
				icon = "Sun-Rain-1";
				break;
			case 13: case 14:
				icon = "Sun-Rain-2";
				break;
			case 19: case 20: case 21: case 23: case 25: case 43:
				icon = "Snowy";
				break;
			case 30:
				icon = "Hot";
				break;
			case 31:
				icon = "Cold";
				break;
			case 32:
				icon = "Wind";
				break;
			case 33: case 34: case 35: case 36: case 37:
				icon = "Moon";
				break;
			default: // TODO: add default error icon
				icon = "Cloud";
		}
		return WEATHER_ICON_PREFIX + icon + ".png";
	}

	public static String getIconDesc(Integer iconNumber) {
		// TODO : 날씨 아이콘 설명
		return "흐림";
	}

	public static String getComp(Double compareVal) {
		// TODO : 날씨 변화량 보여주기
		return "오늘은 어제와 비슷한 날씨가 예상됩니다.";
	}

	public static String getRainIcon(Double rainProbability) {
		int rain = (int) (Math.round(rainProbability/10.0) * 10);
		return RAIN_ICON_PREFIX + rain + ".png";
	}

	public static String getTime(String dateTime) {
		return dateTime.substring(11, 13) + "시";
	}

	public static String getDayOfWeek(String day) throws ParseException {

		Calendar cal = Calendar.getInstance();
		String format = "yyyy-mm-dd";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = dateFormat.parse(day.substring(0, 10));
		cal.setTime(date);

		int dayNum = cal.get(Calendar.DAY_OF_WEEK);
		String dayOfWeek = "";
		switch (dayNum) {
			case 1:
				dayOfWeek = " (일)";
				break;
			case 2:
				dayOfWeek = " (월)";
				break;
			case 3:
				dayOfWeek = " (화)";
				break;
			case 4:
				dayOfWeek = " (수)";
				break;
			case 5:
				dayOfWeek = " (목)";
				break;
			case 6:
				dayOfWeek = " (금)";
				break;
			case 7:
				dayOfWeek = " (토)";
				break;
		}
		return day.substring(5, 7) + "월" + day.substring(8, 10) + "일" + dayOfWeek;

	}

	public static String getAMPM(String date) {
		String day = date.format(String.valueOf(DateTimeFormatter.ofPattern("a")));
		if (day.equals("AM"))
			return "Day";
		else
			return "Night";
	}

}
