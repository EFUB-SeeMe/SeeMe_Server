package com.seeme.service;

import com.seeme.service.api.LocationApi;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LocationService {

	public final LocationApi locationApi;

	public String getLatlonToUmd(Double lat, Double lon) throws Exception {
		return locationApi.covertGpsToSpecificAddress(lat, lon);
	}
}
