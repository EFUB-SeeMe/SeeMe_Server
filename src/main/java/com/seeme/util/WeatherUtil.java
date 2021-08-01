package com.seeme.util;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
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
	public static final String CLOTHES_ICON_PREFIX =
		"https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/clothes/";

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
			case 7: case 8: case 9: case 10: case 11: case 38:
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
			default:
				icon = "Cloud";
		}
		return WEATHER_ICON_PREFIX + icon + ".png";
	}

	public static String getIconDesc(Integer iconNumber) {
		String iconDesc;
		switch (iconNumber) {
			case 1: case 2: case 3: case 33: case 34: case 35: case 36: case 37:
				iconDesc = "맑음";
				break;
			case 15: case 18: case 26: case 29: case 41: case 42:
				iconDesc = "흐리고 비";
				break;
			case 12: case 39: case 40:
				iconDesc = "흐리고 한때 비";
				break;
			case 7: case 8: case 9: case 10: case 11: case 38:
				iconDesc = "흐림";
				break;
			case 22: case 24: case 44:
				iconDesc = "눈";
				break;
			case 4: case 5: case 6:
				iconDesc = "구름 조금";
				break;
			case 16: case 17:
				iconDesc = "비";
				break;
			case 13: case 14:
				iconDesc = "한때 비";
				break;
			case 19: case 20: case 21: case 23: case 25: case 43:
				iconDesc = "한때 눈";
				break;
			case 30:
				iconDesc = "더위";
				break;
			case 31:
				iconDesc = "추위";
				break;
			case 32:
				iconDesc = "바람";
				break;
			default:
				iconDesc = "흐림";
		}
		return iconDesc;
	}

	public static String getComp(Integer compareVal) {
		if (compareVal <= -1 )
			return "오늘은 어제보다 "+Math.abs(compareVal)+"도 낮습니다.";
		else if (compareVal >= 1)
			return "오늘은 어제보다 "+compareVal+"도 높습니다.";
		else
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

		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = transFormat.parse(day);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;
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

	public static String getClothesIcon(Integer iconNum4) {
		return CLOTHES_ICON_PREFIX + iconNum4 + ".png";
	}

	public static String getObjectValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
		Field field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(obj).toString();
	}

	public static String getTime() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour >= 5 && hour <= 12)
			return "minmax";
		else
			return "curr";
	}

	public static int getTemp(String temperature){
		int temp = Integer.parseInt(temperature);
		if (temp <= 4)
			temp = 4;
		else if (temp <=8)
			temp = 5;
		else if (temp <= 11)
			temp = 9;
		else if (temp <= 16)
			temp = 12;
		else if (temp <= 19)
			temp = 17;
		else if (temp <= 22)
			temp = 20;
		else if (temp <= 27)
			temp = 23;
		else if (temp <= 32)
			temp = 28;
		else
			temp = 33;
		return temp;
	}
}
