package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TempResDto {
	String time;
	String temperature;
	String icon;
}
