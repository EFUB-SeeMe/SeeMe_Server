package com.seeme.domain.location;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UmdCodeResDto {
	private final boolean res;
	private final String umd;
	private final String addressCode;
}
