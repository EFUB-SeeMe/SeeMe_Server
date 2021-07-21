package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class MicrodustTime {
	private final int pm10Value;
	private final int pm25Value;
	private final String startTime;
}
