package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MicrodustTimeResDto {
	private final String time;
	private final Integer pm10;
	private final Integer pm25;
}
