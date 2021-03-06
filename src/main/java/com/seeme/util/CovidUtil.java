package com.seeme.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CovidUtil {
	public static final String SERVICE_KEY = "serviceKey";
	public static final String START_CREATE_DT = "startCreateDt";
	public static final String END_CREATE_DT = "endCreateDt";
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	public static String getCovidMainEndCreateDt() {
		Calendar cal = Calendar.getInstance();
		return DATE_FORMAT.format(cal.getTime());
	}

	public static String getCovidMainCreateCreateDt() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		return DATE_FORMAT.format(cal.getTime());
	}

	public static String getCovidRegionalCreateCreateDt() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		return DATE_FORMAT.format(cal.getTime());
	}

	public static int getInc(int compTotal) {
		if (0 < compTotal) return -1;
		else if (compTotal < 0) return 1;
		else return 0;
	}
}
