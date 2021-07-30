package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClothesResDto {
	private final String item1;
	private final String item1Desc;
	private final String item2;
	private final String item2Desc;
	private final String item3;
	private final String item3Desc;
	private final String item4;
	private final String item4Desc;
}
