package com.seeme.service;

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

	public MicrodustTimeResDto getTime(String measuringStation) throws IOException, ParseException, ParserConfigurationException, SAXException {
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

		return MicrodustTimeResDto.builder()
				.time(getDataTime())
				.pm10(pm10)
				.pm25(pm25)
				.build();

	}

	public String getMeasuringStation(Double lat, Double lon) throws IOException, ParseException {
		return microdustOpenApi.getMeasuringStation(lat, lon);
	}
}

