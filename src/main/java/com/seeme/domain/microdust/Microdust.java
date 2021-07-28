package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Microdust {
	private final String pm10Flag;
	private final String pm25Flag;
	private final String so2Flag;
	private final String coFlag;
	private final String o3Flag;
	private final String no2Flag;
	private final String khaiFlag;
	private final String pm10Value;
	private final String pm25Value;
	private final String so2Value;
	private final String coValue;
	private final String o3Value;
	private final String no2Value;
	private final String khaiValue;
	private final String pm10Grade;
	private final String pm25Grade;
	private final String pm10Value24;
	private final String pm25Value24;
}
