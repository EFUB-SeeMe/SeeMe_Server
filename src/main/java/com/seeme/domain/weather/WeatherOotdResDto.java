package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherOotdResDto {
	private final ClothesResDto age10;
	private final ClothesResDto age20;
	private final ClothesResDto age30;
	private final ClothesResDto age40;
	private final ClothesResDto age50;
}
