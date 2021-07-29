package com.seeme.domain.location;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResDto {
	private final String address;
	private final String addressCode;
	private final Double lat;
	private final Double lon;
}
