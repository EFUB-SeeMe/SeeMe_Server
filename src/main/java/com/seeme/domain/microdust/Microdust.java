package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Microdust {
	private final String stationName;
	private final String address;
	private final int pm10Value;
	private final int pm25Value;
	private final int pmGrade;
}
