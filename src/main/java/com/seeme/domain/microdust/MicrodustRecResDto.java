package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MicrodustRecResDto {
	private final String maskIcon;
	private final String desc;
	private final Boolean caiFlag;
	private final Double cai;
}
