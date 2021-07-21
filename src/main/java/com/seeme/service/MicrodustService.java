package com.seeme.service;

import com.seeme.domain.ResDto;
import com.seeme.domain.location.Address;
import com.seeme.domain.location.AddressRepository;
import com.seeme.domain.microdust.*;
import com.seeme.service.api.MicrodustOpenApi;
import com.seeme.util.ErrorMessage;
import com.seeme.util.MicrodustUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MicrodustService {

	private final MicrodustOpenApi microdustOpenApi;
	private final AddressRepository addressRepository;

	public List<ResDto> getMain(List<String> measuringStationList) {
		List<ResDto> resDtoList = new ArrayList<>();
		resDtoList.add(getMainResDto(measuringStationList));
		resDtoList.add(getOtherResDto(measuringStationList));
		resDtoList.add(getRecResDto(resDtoList.get(0), resDtoList.get(1)));
		return resDtoList;
	}

	private ResDto getMainResDto(List<String> measuringStationList) {
		ResDto resDto;
		try {
			resDto = ResDto.builder()
				.resultCode(200).response("main")
				.errorMessage(null)
//				.document(getMainApi(measuringStationList)) // front를 위해서 임시로 막아둠
				.document(MicrodustResDto.builder()
					.desc(MicrodustUtil.getDesc(1))
					.pm10Flag(true)
					.pm10(16)
					.pm25Flag(true)
					.pm25(14)
					.gradeIcon(MicrodustUtil.GRADE_ICON)
					.grade(MicrodustUtil.getGrade("1"))
					.build())
				.build();
//		} catch (ParseException | IOException e) { // front를 위해서 임시로 막아둠
//			resDto = ResDto.builder()
//				.resultCode(500).response("main")
//				.errorMessage(ErrorMessage.WRONG_JSON_PARSING)
//				.document(null)
//				.build();
		} catch (Exception e) {
			resDto = ResDto.builder()
				.resultCode(500).response("main")
				.errorMessage(ErrorMessage.UNKNOWN_ERROR)
				.document(null)
				.build();
		}

		return resDto;
	}

	private MicrodustResDto getMainApi(List<String> measuringStationList) throws IOException, ParseException {
		int index = 0, pmGrade = -1;
		String pm10 = "-1", pm25 = "-1";
		boolean pm10Flag = false, pm25Flag = false;
		while (index < 3) {
			if (pm10Flag && pm25Flag)
				break;
			JSONObject jsonObject = microdustOpenApi.getMainApi(measuringStationList, index++);
			if (!pm10Flag && !jsonObject.get("pm10Value").equals("-")) {
				pm10Flag = true;
				pm10 = jsonObject.get("pm10Value").toString();
				pmGrade = Integer.parseInt(jsonObject.get("pm10Grade").toString());
			}
			if (!pm25Flag && !jsonObject.get("pm25Value").equals("-")) {
				pm25Flag = true;
				pm25 = jsonObject.get("pm25Value").toString();
				pmGrade = Math.max(pmGrade, Integer.parseInt(jsonObject.get("pm25Grade").toString()));
			}
		}

		return MicrodustResDto.builder()
			.pm10Flag(pm10Flag)
			.pm25Flag(pm25Flag)
			.pm10(Integer.parseInt(pm10))
			.pm25(Integer.parseInt(pm25))
			.grade(String.valueOf(pmGrade))
			.gradeIcon(MicrodustUtil.GRADE_ICON)
			.desc(MicrodustUtil.getDesc(pmGrade))
			.build();
	}

	private ResDto getOtherResDto(List<String> measuringStationList) {
		ResDto resDto;
		try {
			resDto = ResDto.builder()
				.resultCode(200).response("other")
				.errorMessage(null)
				.document(microdustOpenApi.getOtherApi(measuringStationList))
				.build();
		} catch (Exception e) {
			resDto = ResDto.builder()
				.resultCode(500).response("other")
				.errorMessage(ErrorMessage.UNKNOWN_ERROR)
				.document(null)
				.build();
		}

		return resDto;
	}

	private ResDto getRecResDto(ResDto microdustResDto, ResDto otherResDto) {
		double cai = getCai((MicrodustOtherResDto) otherResDto.getDocument());
		boolean caiFlag = MicrodustUtil.getCaiFlag(cai);

		return ResDto.builder()
			.resultCode(200).response("rec")
			.errorMessage(null)
			.document(MicrodustRecResDto.builder()
				.cai(cai)
				.caiFlag(caiFlag)
				.maskIcon(getMaskIcon(microdustResDto))
				.desc(getdesc(microdustResDto))
				.build())
			.build();
	}

	private double getCai(MicrodustOtherResDto other) {
		// TODO: add logic;
		// 계산할 수 없을 때 -1을 return
		return 40.193423;
	}

	private String getMaskIcon(ResDto microdustResDto) {
		// TODO: add logic;
		// MicrodustUtil.MASK_KF80, MicrodustUtil.MASK_KF94
		return MicrodustUtil.MASK_DENTAL;
	}

	private String getdesc(ResDto microdustResDto) {
		// TODO: add logic;
		return "미세먼지 좋아요~ 덴탈마스크 추천!";
	}

	public List<MicrodustDayResDto> getDay(String geo) throws IOException, ParseException {
		List<MicrodustDayResDto> microdustDayResDtoList = new ArrayList<>();

		for (MicrodustDay microdustDay : microdustOpenApi.getDayApi(geo)) {
			microdustDayResDtoList.add(MicrodustDayResDto.builder()
				.dust(microdustDay.getPm10())
				.microdust(microdustDay.getPm25())
				.date(microdustDay.getDay())
				.build());
		}

		return microdustDayResDtoList;
	}

	public List<String> getStationList(Double lat, Double lon) throws IOException, ParseException {
		return microdustOpenApi.getStationList(lat, lon);
	}

	public Address getAddressByCode(String code) {
		return addressRepository.findByBjdongCode(code);
	}

	/*
	public MicrodustTimeResDto getFirstTime(List<String> measuringStationList) throws IOException, ParseException {
		MicrodustTimeDto microdust = microdustOpenApi.getFirstTimeApi(measuringStationList);
		return MicrodustTimeResDto.builder()
			.time(microdust.getStartTime())
			.pm10(microdust.getPm10Value())
			.pm25(microdust.getPm25Value())
			.build();
	}

	public List<MicrodustTimeResDto> getOtherTime(String location) throws IOException, ParseException {
		List<MicrodustTimeResDto> microdustTimeResDtoList = new ArrayList<>();
		for (MicrodustTimeDto microdustTimeDto : microdustOpenApi.getOtherTimeApi(location)) {
			microdustTimeResDtoList.add(MicrodustTimeResDto.builder()
				.time(microdustTimeDto.getStartTime())
				.pm10(microdustTimeDto.getPm10Value())
				.pm25(microdustTimeDto.getPm25Value())
				.build()
			);
		}
		return microdustTimeResDtoList;
	}

	public String getAddress(Double lat, Double lon) throws Exception {
		return locationApi.covertGpsToSpecificAddress(lat, lon);
	}

	@Transactional
	public List<MicrodustMapResDto> getMap() throws IOException {
		Map<String, MicrodustStation> map = new HashMap<>();
		microdustStationRepository.findAll().forEach(station -> map.put(station.getName(), station));

		List<MicrodustMapResDto> microdustMapList = new ArrayList<>();
		for (Microdust microdust : microdustOpenApi.getMap()) {
			MicrodustStation station = map.get(microdust.getStationName());
			microdustMapList.add(MicrodustMapResDto.builder()
				.stationName(microdust.getStationName())
				.lat(station.getX())
				.lon(station.getY())
				.pm10(microdust.getPm10Value())
				.pm25(microdust.getPm25Value())
				.grade(microdust.getPm10Grade())
				.build());
		}

		return microdustMapList;
	}
	*/
}
