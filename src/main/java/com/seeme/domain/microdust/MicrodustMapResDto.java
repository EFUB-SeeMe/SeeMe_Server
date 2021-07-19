package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MicrodustMapResDto {
	private final String stationName;
	private final Double lat;
	private final Double lon;
	private final String pm10;
	private final String pm25;
	private final String grade;
}
