package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Microdust {
	private final String stationName;
	private final String pm10Value;
	private final String pm25Value;
	private final String pm10Grade;
}
