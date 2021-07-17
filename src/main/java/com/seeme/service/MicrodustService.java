package com.seeme.service;

import com.seeme.domain.microdust.*;
import com.seeme.service.api.LocationApi;
import com.seeme.service.api.MicrodustOpenApi;
import com.seeme.domain.location.TMAddress;
import com.seeme.util.MicrodustUtil;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MicrodustService {

	private final LocationApi locationApi;
	private final MicrodustOpenApi microdustOpenApi;

	public MicrodustResDto getMain(List<String> measuringStationList, String address) throws IOException, ParseException {
		Microdust microdust = microdustOpenApi.getMainApi(measuringStationList);
		System.out.println(microdust.toString()); // remove
		return MicrodustResDto.builder()
			.address(address)
			.pm10(microdust.getPm10Value())
			.pm25(microdust.getPm25Value())
			.grade(MicrodustUtil.getGrade(microdust.getPmGrade()))
			.gradeIcon(MicrodustUtil.GRADE_ICON)
			.desc(MicrodustUtil.getDesc(microdust.getPmGrade()))
			.build();
	}

	public List<MicrodustTimeResDto> getTime(String measuringStation) throws IOException, ParseException {
		List<MicrodustTimeResDto> microdustTimeResDtoList = new ArrayList<>();

		for (MicrodustTimeDto microdustTimeDto : microdustOpenApi.getTimeApi(measuringStation)) {
			microdustTimeResDtoList.add(MicrodustTimeResDto.builder()
				.time(microdustTimeDto.getTime())
				.pm10(microdustTimeDto.getPm10Value24())
				.pm25(microdustTimeDto.getPm25Value24())
				.build());
		}

		return microdustTimeResDtoList;
	}

	public List<MicrodustDayResDto> getDay(double lat, double lon) throws IOException, ParseException {
		List<MicrodustDayResDto> microdustDayResDtoList = new ArrayList<>();

		for (MicrodustDayDto microdustDayDto : microdustOpenApi.getDayApi(lat, lon)){
			microdustDayResDtoList.add(MicrodustDayResDto.builder()
				.dustAm(10)
				.dustPm(10)
				.microdustAm(10)
				.microdustPm(10)
				.date("07-16")
				.build());
		}

		return microdustDayResDtoList;
	}

	public List<String> getStationList(Double lat, Double lon) throws IOException, ParseException {
		return microdustOpenApi.getStationList(lat, lon);
	}

	public String getAddress(Double lat, Double lon) throws Exception {
		return locationApi.covertGpsToSpecificAddress(lat, lon);
	}

	public String getAddressByTM(TMAddress tmAddress) {
		return tmAddress.getSggName() + " " + tmAddress.getUmdName();
	}

	public List<String> getStationListByTM(String tmX, String tmY) throws IOException, ParseException {
		return microdustOpenApi.getStationListByTM(tmX, tmY);
	}

	public TMAddress getTMAddress(String location) throws IOException {
		return locationApi.getTMAddress(location);
	}
}
