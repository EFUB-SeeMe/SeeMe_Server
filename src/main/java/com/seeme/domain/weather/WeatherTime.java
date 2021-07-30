package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherTime {
	private final String time;
	private final String rain;
	private final String percent;
	private final String temperature;
	private final String tempIcon;
	private final String rainIcon;
}
