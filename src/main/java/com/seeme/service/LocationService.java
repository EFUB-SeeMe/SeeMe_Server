package com.seeme.service;

import com.seeme.domain.location.*;
import com.seeme.service.api.LocationApi;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
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

	public UmdCodeResDto searchByLatLon(Double lat, Double lon) {
		try {
			JSONObject jsonObject = locationApi.searchByLatLon(lat, lon);
			return UmdCodeResDto.builder()
				.res(true)
				.umd(jsonObject.get("region_3depth_name").toString())
				.addressCode(jsonObject.get("code").toString())
				.build();
		} catch (Exception e) {
			return UmdCodeResDto.builder()
				.res(false)
				.build();
		}
	}

	public AddressCodeResDto searchByUmd(String umd) {
		List<AddressResDto> addressList = new ArrayList<>();
		addressRepository.findAllByBjdong(umd)
			.stream().map(Address::toAddressResDto).forEach(addressList::add);

		return AddressCodeResDto.builder()
			.totalCount(addressList.size())
			.addressList(addressList)
			.build();
	}

	public Address getAddressByCode(String code) {
		return addressRepository.findByBjdongCode(code);
	}
}
