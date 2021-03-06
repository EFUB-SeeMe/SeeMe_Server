package com.seeme.domain.microdust;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class MicrodustTotalResDto {
	private final Boolean so2Flag;
	private final Boolean coFlag;
	private final Boolean o3Flag;
	private final Boolean no2Flag;
	private final Boolean caiFlag;
	private final Double so2;
	private final Double co;
	private final Double o3;
	private final Double no2;
	private final Double cai;
	private final String caiIcon;
}
