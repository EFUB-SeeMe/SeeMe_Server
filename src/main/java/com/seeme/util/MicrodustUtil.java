package com.seeme.util;

public class MicrodustUtil {
	public static final String SERVICE_KEY = "serviceKey";
	public static final String NUM_OF_ROWS = "numOfRows";
	public static final String STATION_NAME = "stationName";
	public static final String PAGE_NO = "pageNo";
	public static final String DATA_TERM = "dataTerm";
	public static final String VERSION = "ver";
	public static final String RETURN_TYPE = "returnType";
	public static final String GRADE_ICON =
			"https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/microdust/microdust.png";
	public static final String TM_X = "tmX";
	public static final String TM_Y = "tmY";
	public static final String APP_ID = "appid";
	public static final String LAT = "lat";
	public static final String LON = "lon";
	public static final String FIELDS = "fields";
	public static final String LOCATION = "location";
	public static final String TIME_STEPS = "timesteps";
	public static final String UNITS = "units";
	public static final String API_KEY = "apikey";


	public static String getGrade(int pm10Grade1h) {
		switch (pm10Grade1h) {
			case 1:
				return "좋음";
			case 2:
				return "보통";
			case 3:
				return "나쁨";
			case 4:
				return "매우나쁨";
			default:
				return "error";
		}
	}

	public static String getDesc(int pm10Grade1h) {
		switch (pm10Grade1h) {
			case 1:
				return "야외 활동을 즐겨보세요 !";
			case 2:
				return "적당한 야외 활동은 괜찮아요 ~";
			case 3:
				return "야외 활동을 자제하세요 !";
			case 4:
				return "절대 밖에 나가지 마세요 !!!";
			default:
				return "error";
		}
	}

	public static String getTime(String utc) {
		int kst = Integer.parseInt(utc) + 9;
		if (kst < 24)
			return Integer.toString(kst);
		else if (kst == 24)
			return "0";
		else
			return Integer.toString(kst % 24);
	}
}
