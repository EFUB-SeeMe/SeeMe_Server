package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherWeekResDto {
	private final String day;
	private final Double amRain;
	private final int amRainPercent;
	private final String amIcon;
	private final Double pmRain;
	private final int pmRainPercent;
	private final String pmIcon;
	private final Double max;
	private final Double min;
}
