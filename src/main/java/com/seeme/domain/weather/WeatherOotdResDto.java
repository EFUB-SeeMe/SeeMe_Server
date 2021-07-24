package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherOotdResDto {
	private final String umbrellaIcon;
	private final Boolean umbrellaFlag;
	private final Clothes clothes;
}
