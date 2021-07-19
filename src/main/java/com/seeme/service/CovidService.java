package com.seeme.service;

import com.seeme.service.api.CovidOpenApi;
import com.seeme.service.api.LocationApi;
import com.seeme.domain.covid.*;
import com.seeme.util.LocationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
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
		List<Coronic> coronicList = new ArrayList<>();

		int newCoronic = 0, totalCoronic = 0;

		for (CovidRegionalDto RegionCovid : covidOpenApi.getRegionalApi()) {
			if (RegionCovid.getGubun().equals(location)) {
				coronicList.add(Coronic.builder()
						.day(RegionCovid.getStdDay())
						.coronicByDay(Integer.parseInt(RegionCovid.getLocalOccCnt())+Integer.parseInt(RegionCovid.getOverFlowCnt()))
						.build());
				if (totalCoronic < Integer.parseInt(RegionCovid.getDefCnt())) {
					newCoronic = Integer.parseInt(RegionCovid.getLocalOccCnt())+Integer.parseInt(RegionCovid.getOverFlowCnt());
					totalCoronic = Integer.parseInt(RegionCovid.getDefCnt());
				}
			}
		}

		return CovidRegionalResDto.builder()
			.newCoronic(newCoronic)
			.totalCoronic(totalCoronic)
			.coronicList(coronicList)
			.build();
	}

	public CovidRegionalResDto getNational() throws ParserConfigurationException, SAXException, IOException {
		List<Coronic> coronicList = new ArrayList<>();

		int newCoronic = 0, totalCoronic = 0;

		for (CovidRegionalDto regionCovid : covidOpenApi.getRegionalApi()) {
			if (regionCovid.getGubun().equals("합계")) {
				coronicList.add(Coronic.builder()
						.day(regionCovid.getStdDay())
						.coronicByDay(Integer.parseInt(regionCovid.getLocalOccCnt()))
						.build());
				if (totalCoronic < Integer.parseInt(regionCovid.getDefCnt())) {
					newCoronic = Integer.parseInt(regionCovid.getLocalOccCnt());
					totalCoronic = Integer.parseInt(regionCovid.getDefCnt());
				}
			}
		}

		return CovidRegionalResDto.builder()
				.newCoronic(newCoronic)
				.totalCoronic(totalCoronic)
				.coronicList(coronicList)
				.build();
	}

}
