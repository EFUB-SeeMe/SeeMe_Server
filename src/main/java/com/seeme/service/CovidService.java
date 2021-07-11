package com.seeme.service;

import com.seeme.api.CovidOpenApi;
import com.seeme.api.LocationApi;
import com.seeme.domain.covid.*;
import com.seeme.util.LocationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class CovidService {
	private final LocationApi locationApi;
	private final CovidOpenApi covidOpenApi;

	public CovidResDto getMain(String location) throws ParserConfigurationException, SAXException, IOException {
		int compRegion = 0, compTotal = 0, coronicRegion = 0,
			coronicTotal = 0, isIncRegion = 0, isIncTotal = 0;
		boolean isTotalRecentValueBinding = false, isRegionRecentValueBinding = false;

		for (CovidDto covid : covidOpenApi.getMainApi()) {
			if (covid.getGubun().equals("합계")) {
				if (!isTotalRecentValueBinding) {
					coronicTotal = Integer.parseInt(covid.getIncDec());
					isTotalRecentValueBinding = true;
				} else compTotal = Integer.parseInt(covid.getIncDec()) - coronicTotal;
			} else if (covid.getGubun().equals(location)) {
				if (!isRegionRecentValueBinding) {
					coronicRegion = Integer.parseInt(covid.getIncDec());
					isRegionRecentValueBinding = true;
				} else compRegion = Integer.parseInt(covid.getIncDec()) - coronicRegion;
			}
		}

		if (0 < compRegion)
			isIncRegion = 1;
		else if (compRegion < 0)
			isIncRegion = -1;
		if (0 < compTotal)
			isIncTotal = 1;
		else if (compTotal < 0)
			isIncTotal = -1;

		return CovidResDto.builder()
			.location(location)
			.coronicTotal(coronicTotal)
			.coronicRegion(coronicRegion)
			.compTotal(Math.abs(compTotal))
			.compRegion(Math.abs(compRegion))
			.isIncTotal(isIncTotal)
			.isIncRegion(isIncRegion)
			.build();
	}

	public String getLocation(Double longitude, Double latitude) throws Exception {
		return LocationUtil.getLocation(locationApi.covertGpsToAddress(longitude, latitude));
	}

	public CovidRegionalResDto getRegional(String location) throws IOException, ParserConfigurationException, SAXException {
		// list는 index를 integer로 받아서 형식이 안 맞음
		// Hashmap 등 다른 방법 고려해야 함
		List<CoronicList> coronicList = null;

		int newCoronic = 0, totalCoronic = 0, coronic = 0;
		String day="";

		for (CovidRegionalDto RegionCovid : covidOpenApi.getRegionalApi()) {
			if (RegionCovid.getGubun().equals(location)) {
				day = RegionCovid.getStdDay();
				coronic = Integer.parseInt(RegionCovid.getLocalOccCnt());
				coronicList.add(day, coronic);

				newCoronic = Integer.parseInt(RegionCovid.getLocalOccCnt());
				totalCoronic = Integer.parseInt(RegionCovid.getDefCnt());
			}
		}

		return CovidRegionalResDto.builder()
			.newCoronic(newCoronic)
			.totalCoronic(totalCoronic)
			.CoronicList(coronicList)
			.build();
	}

}
