package com.seeme.service;

import com.seeme.service.api.CovidOpenApi;
import com.seeme.service.api.LocationApi;
import com.seeme.domain.covid.*;
import com.seeme.util.CovidUtil;
import com.seeme.util.LocationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CovidService {
	private final LocationApi locationApi;
	private final CovidOpenApi covidOpenApi;

	public CovidResDto getMain(String location) throws ParserConfigurationException, SAXException, IOException {
		int compRegion = 0, compTotal = 0, coronicRegion = 0, coronicTotal = 0;
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

		return CovidResDto.builder()
			.location(location)
			.coronicTotal(coronicTotal)
			.coronicRegion(coronicRegion)
			.compTotal(Math.abs(compTotal))
			.compRegion(Math.abs(compRegion))
			.isIncTotal(CovidUtil.getInc(compTotal))
			.isIncRegion(CovidUtil.getInc(compRegion))
			.build();
	}

	public String getLocation(Double longitude, Double latitude) throws Exception {
		return LocationUtil.getLocation(locationApi.covertGpsToAddress(longitude, latitude));
	}

	public CovidRegionalResDto getRegional(String location) throws IOException, ParserConfigurationException, SAXException {
		List<Coronic> coronicList = new ArrayList<>();
		int newCoronic = 0, totalCoronic = 0;

		for (CovidRegionalDto regionCovid : covidOpenApi.getRegionalApi()) {
			if (regionCovid.getGubun().equals(location)) {
				coronicList.add(Coronic.builder()
					.day(regionCovid.getStdDay())
					.coronicByDay(
						Integer.parseInt(regionCovid.getLocalOccCnt())
							+ Integer.parseInt(regionCovid.getOverFlowCnt()))
					.build());
				if (totalCoronic < Integer.parseInt(regionCovid.getDefCnt())) {
					newCoronic = Integer.parseInt(regionCovid.getLocalOccCnt())
						+ Integer.parseInt(regionCovid.getOverFlowCnt());
					totalCoronic = Integer.parseInt(regionCovid.getDefCnt());
				}
			}
		}
		Collections.reverse(coronicList);

		return CovidRegionalResDto.builder()
			.newCoronic(newCoronic)
			.totalCoronic(totalCoronic)
			.coronicList(coronicList)
			.build();
	}
}
