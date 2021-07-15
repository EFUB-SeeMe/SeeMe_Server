package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MicrodustDayResDto {
	private final Integer dust_am;
	private final Integer dust_pm;
	private final Integer microdust_am;
	private final Integer microdust_pm;
	private final String date;
}
