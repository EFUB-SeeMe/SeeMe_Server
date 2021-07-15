package com.seeme.service;

import com.seeme.api.LocationApi;
import com.seeme.api.MicrodustOpenApi;
import com.seeme.domain.microdust.Microdust;
import com.seeme.domain.microdust.MicrodustResDto;
import com.seeme.util.MicrodustUtil;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import java.io.IOException;

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

	public String getMeasuringStation(Double lat, Double lon) throws IOException, ParseException {
		return microdustOpenApi.getMeasuringStation(lat, lon);
	}

	public String getAddress(Double lat, Double lon) throws Exception {
		return locationApi.covertGpsToSpecificAddress(lat, lon);
	}
}

