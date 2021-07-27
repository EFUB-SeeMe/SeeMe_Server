package com.seeme.domain.location;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double lat;
	private Double lon;

	private String sido;
	private String sigungu;

	private String bjdong;
	private String bjdongCode;

	public AddressCode toAddressCode() {
		return AddressCode.builder()
			.address(sido + " " + sigungu + " " + bjdong)
			.addressCode(bjdongCode)
			.build();
	}
}
