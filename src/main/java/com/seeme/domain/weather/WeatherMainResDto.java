package com.seeme.domain.weather;

import com.seeme.domain.ResDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeatherMainResDto {
	ResDto currentInfo;
	ResDto forecastInfo;
}
