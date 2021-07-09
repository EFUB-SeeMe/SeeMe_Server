package com.seeme.domain.covid;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CovidDto {
	private final String stdDay;
	private final String gubun;
	private final String incDec;
}
