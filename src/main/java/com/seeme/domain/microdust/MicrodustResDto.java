package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MicrodustResDto {
	private final String address;
	private final String grade;
	private final String gradeIcon;
	private final Integer pm10;
	private final Integer pm25;
	private final String desc;
}
