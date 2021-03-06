package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherWeekResDto {
	private final String day;
	private final int amRain;
	private final int amRainPercent;
	private final String amIcon;
	private final int pmRain;
	private final int pmRainPercent;
	private final String pmIcon;
	private final int max;
	private final int min;
}
