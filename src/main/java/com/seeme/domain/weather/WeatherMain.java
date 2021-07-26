package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherMain {
	private final Double currTemp;
	private final Double feelTemp;
	private final String iconDesc;
	private final String icon;
	private final String comp;
}