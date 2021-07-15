package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MicrodustTimeDto {
	private final String stationName;
	private final String address;
	private final String pm10Value;
	private final String pm25Value;
	private final String time;
}
