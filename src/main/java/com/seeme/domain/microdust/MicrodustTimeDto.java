package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MicrodustTimeDto {
	private final String stationName;
	private final Integer pm10Value24;
	private final Integer pm25Value24;
	private final String time;
}
