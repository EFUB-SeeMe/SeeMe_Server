package com.seeme.service;


import com.seeme.domain.covid.Coronic;
import com.seeme.domain.covid.CovidRegionalDto;
import com.seeme.domain.covid.CovidRegionalResDto;
import com.seeme.domain.microdust.MicrodustDayResDto;
import lombok.AllArgsConstructor;

import com.seeme.api.LocationApi;
import com.seeme.api.MicrodustOpenApi;
import com.seeme.domain.microdust.Microdust;
import com.seeme.domain.microdust.MicrodustResDto;
import com.seeme.domain.microdust.MicrodustTimeDto;
import com.seeme.domain.microdust.MicrodustTimeResDto;
import com.seeme.util.MicrodustUtil;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;

import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static com.seeme.util.MicrodustUtil.getDataTime;

@Service
@AllArgsConstructor
public class MicrodustService {

	private final LocationApi locationApi;
	private final MicrodustOpenApi microdustOpenApi;

	public MicrodustResDto getMain(String measuringStation, String address) throws IOException, ParseException {
		Microdust microdust = microdustOpenApi.getMainApi(measuringStation);
		System.out.println(microdust.toString()); // after dev, remove
		return MicrodustResDto.builder()
			.address(address)
			.pm10(microdust.getPm10Value())
			.pm25(microdust.getPm25Value())
			.grade(MicrodustUtil.getGrade(microdust.getPmGrade()))
			.gradeIcon(MicrodustUtil.GRADE_ICON)
			.desc(MicrodustUtil.getDesc(microdust.getPmGrade()))
			.build();
	}

	public List<MicrodustTimeResDto> getTime(String measuringStation) throws IOException, ParseException, ParserConfigurationException, SAXException {
		List<MicrodustTimeResDto> microdustTimeResDtoList = new ArrayList<>();
		int pm10 = 12, pm25 = 10;

		for (MicrodustTimeDto microdustTimeDto : microdustOpenApi.getTimeApi(measuringStation)) {
			if (microdustTimeDto.getStationName().equals(measuringStation)) {
				microdustTimeResDtoList.add(MicrodustTimeResDto.builder()
						.time(microdustTimeDto.getTime())
						.pm10(Integer.parseInt(microdustTimeDto.getPm10Value()))
						.pm25(Integer.parseInt(microdustTimeDto.getPm25Value()))
						.build());
			}
		}

		return microdustTimeResDtoList;
	}

	public String getMeasuringStation(Double lat, Double lon) throws IOException, ParseException {
		return microdustOpenApi.getMeasuringStation(lat, lon);
	}

	public String getAddress(Double lat, Double lon) throws Exception {
		return locationApi.covertGpsToSpecificAddress(lat, lon);
	}

	public MicrodustDayResDto getDay(Double lat, Double lon) throws ParserConfigurationException, SAXException, IOException {

		int dust_am = 0, dust_pm = 0, microdust_am = 0, microdust_pm = 0;
		String date="";

		return MicrodustDayResDto.builder()
			.dust_am(dust_am)
			.dust_pm(dust_pm)
			.microdust_am(microdust_am)
			.microdust_pm(microdust_pm)
			.date(date)
			.build();
	}


}
