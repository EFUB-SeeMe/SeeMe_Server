package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MicrodustDayResDto {
	private final Integer dust;
	private final Integer microdust;
	private final String date;
}
