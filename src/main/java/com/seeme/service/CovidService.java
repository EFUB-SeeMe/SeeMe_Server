package com.seeme.service;


import com.seeme.domain.CovidNationalResDto;

import com.seeme.api.CovidOpenApi;
import com.seeme.domain.covid.CovidDto;
import com.seeme.domain.covid.CovidResDto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import java.text.SimpleDateFormat;

@Service
@AllArgsConstructor
public class CovidService {

    public CovidNationalResDto getMain() throws ParserConfigurationException, SAXException, IOException {
        String location = getMainLocation();

        return CovidNationalResDto.builder()
                .location(location)
    }

    private String getMainLocation() {
        return "남양주";
    }

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH");

	private final CovidOpenApi covidOpenApi;

	public CovidResDto getMain() throws ParserConfigurationException, SAXException, IOException {

		String location = getMainLocation();
		int compRegion = 0, compTotal = 0, coronicRegion = 0,
			coronicTotal = 0, isIncRegion = 0, isIncTotal = 0;
		boolean isTotalRecentValueBinding = false, isRegionRecentValueBinding = false;;

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

	private String getMainLocation() {
		return "인천";
	}
}
