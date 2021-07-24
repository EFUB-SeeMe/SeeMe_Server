package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherRainResDto {
	private final String time;
	private final int rain;
	private final int percent;
	private final String icon;
}
