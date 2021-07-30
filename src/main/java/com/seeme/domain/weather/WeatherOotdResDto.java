package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherOotdResDto {
	private final Clothes age10;
	private final Clothes age20;
	private final Clothes age30;
	private final Clothes age40;
	private final Clothes age50;
}
