package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Clothes {
	private final int age;
	private final String top;
	private final String bottom;
	private final String shoes;
	private final String desc;
	private final String reason;
}
