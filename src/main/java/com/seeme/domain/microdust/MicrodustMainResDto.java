package com.seeme.domain.microdust;

import com.seeme.domain.ResDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MicrodustMainResDto {
	private final ResDto mainInfo;
	private final ResDto totalInfo;
	private final ResDto maskInfo;
}
