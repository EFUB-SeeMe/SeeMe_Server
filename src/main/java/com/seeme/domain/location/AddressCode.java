package com.seeme.domain.location;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressCode {
	private final String address;
	private final String addressCode;
}
