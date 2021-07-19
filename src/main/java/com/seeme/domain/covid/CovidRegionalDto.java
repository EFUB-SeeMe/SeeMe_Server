package com.seeme.domain.covid;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CovidRegionalDto {
	private final String stdDay;
	private final String localOccCnt;
	private final String overFlowCnt;
	private final String defCnt;
	private final String gubun;
}
