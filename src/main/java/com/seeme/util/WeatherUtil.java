package com.seeme.util;

public class WeatherUtil {
	public static final Double DEFAULT_LAT = 37.123456;
	public static final Double DEFAULT_LON = 127.123456;
	public static final String API_KEY = "apikey";
	public static final String Q = "q";
	public static final String DETAILS = "details";

	public static String getDesc(String weatherText){
		// TODO : 날씨 설명 한글로
		return "오늘은 오후 6시에 소나기가 내려요! 아 근데 이거 좀 에바";
	}

	public static String getIcon(Integer iconNumber){
		// TODO : 날씨 아이콘 매칭칭
		return  "https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/weather/Sun.png";
	}

	public static String getIconDesc(Integer iconNumber){
		// TODO : 날씨 아이콘 설명
		return "흐림";
	}

	public static String getComp(Double compareVal){
		// TODO : 날씨 변화량 보여주기
		return "오늘은 어제와 비슷한 날씨가 예상됩니다.";
	}


}
