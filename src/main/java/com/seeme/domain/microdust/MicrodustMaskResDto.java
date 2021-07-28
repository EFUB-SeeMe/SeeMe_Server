package com.seeme.domain.microdust;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MicrodustMaskResDto {
	private final String maskIcon;
	private final String desc;
}
