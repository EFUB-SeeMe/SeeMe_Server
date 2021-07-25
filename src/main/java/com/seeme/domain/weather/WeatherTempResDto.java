package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherTempResDto {
	private final String time;
	private final String temperature;
	private final String icon;
}
