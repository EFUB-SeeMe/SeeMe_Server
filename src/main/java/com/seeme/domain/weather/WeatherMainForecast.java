package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherMainForecast {
	private final Double min;
	private final Double max;
	private final String desc;
}