package com.seeme.domain.weather;

import com.seeme.domain.ResDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherTimeResDto {
	private final ResDto tempInfo;
	private final ResDto rainInfo;
}
