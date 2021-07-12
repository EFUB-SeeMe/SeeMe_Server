package com.seeme.domain.covid;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Coronic {
	private final String day;
	private final Integer coronicByDay;
}
