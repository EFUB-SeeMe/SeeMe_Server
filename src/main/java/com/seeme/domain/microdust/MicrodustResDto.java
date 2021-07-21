package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MicrodustResDto {
	private final String grade;
	private final String gradeIcon;
	private final Boolean pm10Flag;
	private final Boolean pm25Flag;
	private final Integer pm10;
	private final Integer pm25;
	private final String desc;
}
