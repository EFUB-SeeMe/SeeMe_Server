package com.seeme.service;

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

	private final MicrodustOpenApi microdustOpenApi;

	public MicrodustResDto getMain(String measuringStation) throws IOException, ParseException {
		Microdust microdust = microdustOpenApi.getMainApi(measuringStation);
		System.out.println(microdust.toString()); // after dev, remove
		return MicrodustResDto.builder()
			.address(microdust.getAddress())
			.pm10(microdust.getPm10Value())
			.pm25(microdust.getPm25Value())
			.grade(MicrodustUtil.getGrade(microdust.getPmGrade()))
			.gradeIcon(MicrodustUtil.getGradeIcon(microdust.getPmGrade()))
			.desc(MicrodustUtil.getDesc(microdust.getPmGrade()))
			.build();
	}

	public String getMeasuringStation(Double lat, Double lon) throws IOException, ParseException {
		return microdustOpenApi.getMeasuringStation(lat, lon);
	}
}

