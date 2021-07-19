package com.seeme.domain.covid;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CovidRegionalResDto {
	private final Integer newCoronic;
	private final Integer totalCoronic;
	private final List<Coronic> coronicList;
}

