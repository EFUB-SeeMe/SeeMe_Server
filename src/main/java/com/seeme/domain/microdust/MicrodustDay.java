package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MicrodustDay {
	private final Integer pm10;
	private final Integer pm25;
	private final String day;
}