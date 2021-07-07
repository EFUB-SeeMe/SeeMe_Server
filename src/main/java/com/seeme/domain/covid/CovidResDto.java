package com.seeme.domain.covid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CovidResDto {
	private final String location;
	private final Integer coronicTotal;
	private final Integer coronicRegion;
	private final Integer compTotal;
	private final Integer compRegion;
	private final Integer isIncTotal;
	private final Integer isIncRegion;
}
