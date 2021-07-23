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
	private final Boolean caiFlag;
	private final Double pm10;
	private final Double pm25;
	private final Double so2;
	private final Double co;
	private final Double o3;
	private final Double no2;
	private final Double cai;
	private final String caiIcon;
}
