package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherMain {
	private final int currTemp;
	private final int feelTemp;
	private final String iconDesc;
	private final String icon;
	private final String comp;
}