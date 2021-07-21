package com.seeme.domain.microdust;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class MicrodustOtherResDto {
	private final Boolean pm10Flag;
	private final Boolean pm25Flag;
	private final Boolean so2Flag;
	private final Boolean coFlag;
	private final Boolean o3Flag;
	private final Boolean no2Flag;
	private final Integer pm10;
	private final Integer pm25;
	private final Integer so2;
	private final Integer co;
	private final Integer o3;
	private final Integer no2;
}
