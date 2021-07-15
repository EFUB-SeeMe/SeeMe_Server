package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MicrodustResDto {
	private final String address;
	private final String grade;
	private final String gradeIcon;
	private final int pm10;
	private final int pm25;
	private final String desc;
}
