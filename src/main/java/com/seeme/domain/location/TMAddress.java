package com.seeme.domain.location;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TMAddress {
	private final String tmX;
	private final String tmY;
	private final String sidoName;
	private final String sggName;
	private final String umdName;
}
