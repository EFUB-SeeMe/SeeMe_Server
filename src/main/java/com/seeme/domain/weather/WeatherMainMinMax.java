package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherMainMinMax {
	private final int min;
	private final int max;
	private final String desc;
}