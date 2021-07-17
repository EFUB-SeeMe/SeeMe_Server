package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MicrodustDayResDto {
	private final Integer dustAm;
	private final Integer dustPm;
	private final Integer microdustAm;
	private final Integer microdustPm;
	private final String date;
}
