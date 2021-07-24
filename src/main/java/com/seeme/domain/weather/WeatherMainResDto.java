package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherMainResDto {
	private final String icon;
	private final String iconDesc;
	private final int currTemp;
	private final int feelTemp;
	private final int max;
	private final int min;
	private final String desc;
	private final String comp;
}
