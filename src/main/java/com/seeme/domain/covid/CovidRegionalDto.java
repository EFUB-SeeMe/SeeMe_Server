package com.seeme.domain.covid;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CovidRegionalDto {
	private final String stdDay; // 기준 일시
	private final String localOccCnt; // 지역 발생 수
	private final String defCnt; // 지역 누적 발생 수
	private final String gubun; // 시,도명
}
