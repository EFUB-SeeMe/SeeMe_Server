package com.seeme.domain.location;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity(name = "address")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double lat;
	private Double lon;

	private String sd;
	private String sgg;

	private String bjdong;
	private String bjdongCode;
	private String hjdong;
	private String hjdongCode;

	public AddressCode toAddressCode() {
		return AddressCode.builder()
			.address(sd + " " + sgg + " " + bjdong)
			.addressCode(bjdongCode)
			.build();
	}
}
