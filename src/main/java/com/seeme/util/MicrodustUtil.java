package com.seeme.util;

import java.util.Arrays;
import java.util.List;

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
    public static final String TOKEN = "token";
    public static final String SIDO_NAME = "sidoName";
    public static final List<String> DEFAULT_STATION = Arrays.asList("중구", "한강대로", "청계천로");
    public static final String DEFAULT_GEO = "37.56197784552834;126.9468124393769";

    public static final String MASK_DENTAL = "https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/mask/dental.png";
    public static final String MASK_KF80 = "https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/mask/kf80.png";
    public static final String MASK_KF94 = "https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/mask/kf94.png";

    public static final String MICRODUST_ICON_PREFIX = "https://seeme-icon.s3.ap-northeast-2.amazonaws.com/icon/microdust";

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

    public static int AQItoPM25(int avg) {
        double conMax = 0, conMin = 0;
        int aqiMax = 0, aqiMin = 0;
        if (avg <= 50) {
            conMax = 12.0;
            conMin = 0.0;
            aqiMax = 50;
            aqiMin = 0;
        } else if (avg <= 100) {
            conMax = 35.4;
            conMin = 12.1;
            aqiMax = 100;
            aqiMin = 51;
        } else if (avg <= 150) {
            conMax = 55.4;
            conMin = 35.5;
            aqiMax = 150;
            aqiMin = 101;
        } else if (avg <= 200) {
            conMax = 150.4;
            conMin = 55.5;
            aqiMax = 200;
            aqiMin = 151;
        } else if (avg <= 300) {
            conMax = 250.4;
            conMin = 150.5;
            aqiMax = 300;
            aqiMin = 201;
        } else if (avg <= 400) {
            conMax = 350.4;
            conMin = 250.5;
            aqiMax = 400;
            aqiMin = 301;
        } else if (avg <= 500) {
            conMax = 500.4;
            conMin = 350.5;
            aqiMax = 500;
            aqiMin = 401;
        }
        return (int) Math.round((avg - aqiMin) * (conMax - conMin) / (aqiMax - aqiMin) + conMin);
    }

    public static int AQItoPM10(int avg) {
        double conMax = 0, conMin = 0;
        int aqiMax = 0, aqiMin = 0;
        if (avg <= 50) {
            conMax = 54.0;
            conMin = 0.0;
            aqiMax = 50;
            aqiMin = 0;
        } else if (avg <= 100) {
            conMax = 154.0;
            conMin = 55.0;
            aqiMax = 100;
            aqiMin = 51;
        } else if (avg <= 150) {
            conMax = 254.0;
            conMin = 155.0;
            aqiMax = 150;
            aqiMin = 101;
        } else if (avg <= 200) {
            conMax = 354.0;
            conMin = 255.0;
            aqiMax = 200;
            aqiMin = 151;
        } else if (avg <= 300) {
            conMax = 424.0;
            conMin = 355.0;
            aqiMax = 300;
            aqiMin = 201;
        } else if (avg <= 400) {
            conMax = 504.0;
            conMin = 425.0;
            aqiMax = 400;
            aqiMin = 301;
        } else if (avg <= 500) {
            conMax = 604.0;
            conMin = 505.0;
            aqiMax = 500;
            aqiMin = 401;
        }
        return (int) Math.round((avg - aqiMin) * (conMax - conMin) / (aqiMax - aqiMin) + conMin);
    }

    public static String getCaiIcon(double cai) {
        String iconName;
        if (cai >= 0 && cai <= 50)
            iconName = "good";
        else if (cai <= 100)
            iconName = "soso";
        else if (cai <= 250)
            iconName = "bad";
        else if (cai >= 251)
            iconName = "so+bad";
        else
            iconName = "error";
        return MICRODUST_ICON_PREFIX + "/cai/" + iconName + ".png";
    }

	public static String getPmGradeIcon(int pmGrade) {
          String iconName;
        if (pmGrade == 1)
            iconName = "good";
        else if (pmGrade == 2)
            iconName = "soso";
        else if (pmGrade == 3)
            iconName = "bad";
        else if (pmGrade == 4)
            iconName = "so+bad";
        else
            iconName = "error";
        return MICRODUST_ICON_PREFIX + "/pm/" + iconName + ".png";
	}

	public static Double getDouble(String value) {
        return Double.parseDouble(value.equals("-") ? "-1" : value);
	}

    public static Boolean getBool(String value) {
        return !value.equals("-");
    }

	public static int get10GradeByValue(String pm10Value) {
        int pm10;
        try {
            pm10 = Integer.parseInt(pm10Value);
        } catch (Exception e) {
            return -1;
        }
        if (0 <= pm10 && pm10 <= 30)
            return 1;
        else if (pm10 <= 80)
            return 2;
        else if (pm10 <= 150)
            return 3;
        else
            return 4;
	}

    public static int get25GradeByValue(String pm25Value) {
        int pm25;
        try {
            pm25 = Integer.parseInt(pm25Value);
        } catch (Exception e) {
            return -1;
        }
        if (0 <= pm25 && pm25 <= 15)
            return 1;
        else if (pm25 <= 35)
            return 2;
        else if (pm25 <= 75)
            return 3;
        else
            return 4;
    }
}
