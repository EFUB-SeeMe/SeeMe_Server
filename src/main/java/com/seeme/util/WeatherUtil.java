package com.seeme.util;

public class WeatherUtil {
	public static final Double DEFAULT_LAT = 37.123456;
	public static final Double DEFAULT_LON = 127.123456;
	public static final String API_KEY = "apikey";

	public static final String LANGUAGE = "language";
	public static final String Q = "q";
	public static final String METRIC = "metric";
	public static final String WEATHER_ICON_PREFIX =
		"https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/";

	public static String getWeatherIcon(String weatherIcon) {
		String icon = "Sun";
		return WEATHER_ICON_PREFIX + icon + ".png";
	}
}
