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

import static com.seeme.util.MicrodustUtil.*;

@Service
@AllArgsConstructor
public class MicrodustService {

	private final MicrodustOpenApi microdustOpenApi;
	private final AddressRepository addressRepository;

	public MicrodustMainResDto getMain(List<String> measuringStationList) {
		ResDto main = getMainResDto(measuringStationList);
		ResDto total = getTotalResDto(measuringStationList);
		ResDto mask = getMaskResDto(measuringStationList);

		return MicrodustMainResDto.builder()
			.mainInfo(main)
			.totalInfo(total)
			.maskInfo(mask)
			.build();
	}

	private ResDto getMainResDto(List<String> measuringStationList) {
		ResDto resDto;
		try {
			resDto = ResDto.builder()
				.resultCode(200)
				.errorMessage(ErrorMessage.SUCCESS)
				.document(getMainApi(measuringStationList))
				.build();
		} catch (ParseException | IOException e) {
			resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.JSON_PARSING_ERROR)
				.document(null)
				.build();
		} catch (Exception e) {
			resDto = ResDto.builder()
				.resultCode(500)
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
			.grade(MicrodustUtil.getGrade(pmGrade))
			.gradeIcon(MicrodustUtil.getPmGradeIcon(pmGrade))
			.desc(MicrodustUtil.getDesc(pmGrade))
			.build();
	}

	private ResDto getTotalResDto(List<String> measuringStationList) {

		ResDto resDto;
		try {
			resDto = ResDto.builder()
				.resultCode(200)
				.errorMessage(ErrorMessage.SUCCESS)
				.document(getTotalApi(measuringStationList))
				.build();
		} catch (ParseException | IOException e) {
			resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.JSON_PARSING_ERROR)
				.document(null)
				.build();
		} catch (Exception e) {
			resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.UNKNOWN_ERROR)
				.document(null)
				.build();
		}

		return resDto;
	}

	private MicrodustTotalResDto getTotalApi(List<String> measuringStationList) throws IOException, ParseException {
		int index = 0, cnt = 0;
		String[] values = {"pm10Value", "pm25Value", "coValue", "no2Value", "o3Value", "so2Value", "khaiValue", "pm10Value24", "pm25Value24",};
		while (index < 3) {
			JSONObject jsonObject = microdustOpenApi.getTotalApi(measuringStationList, index++);
			for (String value : values) {
				if (!jsonObject.get(value).equals("-")) {
					cnt += 1;
				}
			}
			if (cnt == values.length)
				break;
			cnt = 0;
		}

		JSONObject jsonObject = microdustOpenApi.getTotalApi(measuringStationList, --index);
		ArrayList<Boolean> boolFlags = new ArrayList<>();
		for (String value : values) {
			if (jsonObject.get(value).equals("-")) {
				boolFlags.add(false);
			} else {
				boolFlags.add(true);
			}
		}

		return MicrodustTotalResDto.builder()
			.pm10(Double.parseDouble(jsonObject.get("pm10Value").toString())).pm10Flag(boolFlags.get(0))
			.pm25(Double.parseDouble(jsonObject.get("pm25Value").toString())).pm25Flag(boolFlags.get(1))
			.co(Double.parseDouble(jsonObject.get("coValue").toString())).coFlag(boolFlags.get(2))
			.no2(Double.parseDouble(jsonObject.get("no2Value").toString())).no2Flag(boolFlags.get(3))
			.o3(Double.parseDouble(jsonObject.get("o3Value").toString())).o3Flag(boolFlags.get(4))
			.so2(Double.parseDouble(jsonObject.get("so2Value").toString())).so2Flag(boolFlags.get(5))
			.cai(Double.parseDouble(jsonObject.get("khaiValue").toString())).caiFlag(boolFlags.get(6))
			.caiIcon(MicrodustUtil.getCaiIcon(Double.parseDouble(jsonObject.get("khaiValue").toString())))
			.build();
	}

	private ResDto getMaskResDto(List<String> measuringStationList) {

		ResDto resDto;
		try {
			resDto = ResDto.builder()
				.resultCode(200)
				.errorMessage(null)
				.document(getMaskApi(measuringStationList))
				.build();
		} catch (ParseException | IOException e) {
			resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.JSON_PARSING_ERROR)
				.document(null)
				.build();
		} catch (Exception e) {
			resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.UNKNOWN_ERROR)
				.document(null)
				.build();
		}

		return resDto;
	}

	private MicrodustMaskResDto getMaskApi(List<String> measuringStationList) throws IOException, ParseException {
		JSONObject jsonObject = microdustOpenApi.getTotalApi(measuringStationList, 0);
		String pm10 = (String) jsonObject.get("pm10Value");
		int pm10Value = Integer.parseInt(pm10);
		String maskIcon = getMaskIcon(pm10Value);
		String desc = getMaskdesc(maskIcon);

		return MicrodustMaskResDto.builder()
			.maskIcon(maskIcon)
			.desc(desc)
			.build();
	}

	private String getMaskIcon(int pm10) {
		if (pm10 >=0 && pm10 <= 15)
			return MicrodustUtil.MASK_DENTAL;
		else if (pm10 <= 35)
			return MicrodustUtil.MASK_KF80;
		else
			return MicrodustUtil.MASK_KF94;
	}

	private String getMaskdesc(String maskIcon) {
		switch (maskIcon) {
			case MASK_DENTAL:
				return "미세먼지 좋아요~ 덴탈마스크 추천!";
			case MASK_KF80:
				return "미세먼지가 꽤 있어요 ㅠㅠ kf80 추천!";
			case MASK_KF94:
				return "미세먼지 위험해요.. kf94 추천!";
			default:
				return "error";
		}
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
