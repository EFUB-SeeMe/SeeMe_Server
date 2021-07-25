package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherMain {
	private final Double temp;
	private final Double feelTemp;
	private final String weatherText;
	private final Integer weatherIcon;
	private final Double pastTemp;
}