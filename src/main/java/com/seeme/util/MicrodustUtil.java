package com.seeme.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

	public static int getGrade(String pm10Grade, String pm25Grade) {
		return Math.max(Integer.parseInt(pm10Grade), Integer.parseInt(pm25Grade));
	}

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

	public static String getDataTime() {
		SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh");
		Calendar cal = Calendar.getInstance();
		return TIME_FORMAT.format(cal.getTime());
	}
}
