package com.seeme.domain.location;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AddressCodeResDto {
	private final Integer totalCount;
	private final List<AddressCode> addressList;
}
