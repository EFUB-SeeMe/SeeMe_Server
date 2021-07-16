package com.seeme.util;

public class LocationUtil {
	public static final String KEY = "key";
	public static final String LANGUAGE = "language";
	public static final String LATITUDE_LONGITUDE = "latlng";
	public static final Object LANGUAGE_VALUE = "ko";

	public static final String CONSUMER_ID = "consumer_key";
	public static final String CONSUMER_KEY = "consumer_secret";

	public static final String ACCESS_TOKEN = "accessToken";
	public static final String SRC = "src";
	public static final String WGS84_CODE = "4326";
	public static final String DST = "dst";
	public static final String GRS80_CODE = "5179";
	public static final String POS_X = "posX";
	public static final String POS_Y = "posY";
	public static final String X = "x";
	public static final String Y = "y";

	public static final String UMD_NAME = "umdName";

	public static String getLatLonValue(Double latitude, Double longitude) {
		return latitude.toString() + "," + longitude.toString();
	}

	public static String getLocation(String address) {
		String longName = address.split(" ")[1], shortName = "서울";
		switch (longName) {
			case "서울특별시":
				shortName = "서울";
				break;
			case "인천광역시":
				shortName = "인천";
				break;
			case "경기도":
				shortName = "경기";
				break;
			case "제주특별자치도":
				shortName = "제주";
				break;
			case "경상남도":
				shortName = "경남";
				break;
			case "경상북도":
				shortName = "경북";
				break;
			case "전라남도":
				shortName = "전남";
				break;
			case "전라북도":
				shortName = "전북";
				break;
			case "충청남도":
				shortName = "충남";
				break;
			case "충청북도":
				shortName = "충북";
				break;
			case "강원도":
				shortName = "강원";
				break;
			case "세종특별자치시":
				shortName = "세종";
				break;
			case "울산광역시":
				shortName = "울산";
				break;
			case "대전광역시":
				shortName = "대전";
				break;
			case "광주광역시":
				shortName = "광주";
				break;
			case "대구광역시":
				shortName = "대구";
				break;
			case "부산광역시":
				shortName = "부산";
				break;
		}
		return shortName;
	}
}
