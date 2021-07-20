package com.seeme.service;

import com.seeme.domain.location.Address;
import com.seeme.domain.location.AddressCode;
import com.seeme.domain.location.AddressCodeResDto;
import com.seeme.domain.location.AddressRepository;
import com.seeme.service.api.LocationApi;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class LocationService {

	public final LocationApi locationApi;
	public final AddressRepository addressRepository;

	public String getLatlonToUmd(Double lat, Double lon) throws Exception {
		return locationApi.covertGpsToSpecificAddress(lat, lon);
	}

	public AddressCodeResDto searchByUmd(String umd) {
		List<AddressCode> addressList = new ArrayList<>();
		addressRepository.findAllByBjdong(umd)
			.stream().map(Address::toAddressCode).forEach(addressList::add);

		return AddressCodeResDto.builder()
			.totalCount(addressList.size())
			.addressList(addressList)
			.build();
	}
}
