package com.seeme.service;

import com.seeme.domain.ResDto;
import com.seeme.domain.microdust.*;
import com.seeme.service.api.MicrodustOpenApi;
import com.seeme.util.ErrorMessage;
import com.seeme.util.MicrodustUtil;
import lombok.AllArgsConstructor;
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

	public MicrodustMainResDto getMain(List<String> measuringStationList) {
		try {
			System.out.println(measuringStationList);
			List<Microdust> microdustList = getMainApi(measuringStationList);
			return MicrodustMainResDto.builder()
				.mainInfo(getMainResDto(microdustList))
				.totalInfo(getTotalResDto(microdustList))
				.maskInfo(getMaskResDto(microdustList))
				.build();
		} catch (ParseException | IOException e) {
			e.printStackTrace();
			ResDto resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.JSON_PARSING_ERROR)
				.document(null)
				.build();
			return MicrodustMainResDto.builder()
				.mainInfo(resDto).totalInfo(resDto).maskInfo(resDto).build();
		} catch (Exception e) {
			e.printStackTrace();
			ResDto resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessage.UNKNOWN_ERROR)
				.document(null)
				.build();

			return MicrodustMainResDto.builder()
				.mainInfo(resDto).totalInfo(resDto).maskInfo(resDto).build();
		}
	}

	private List<Microdust> getMainApi(List<String> measuringStationList) throws IOException, ParseException {
		List<Microdust> microdustList = new ArrayList<>();
		int index = 0;
		while (index < 3)
			microdustList.add(microdustOpenApi.getMainApi(measuringStationList, index++));
		return microdustList;
	}

	private ResDto getMainResDto(List<Microdust> microdustList) {
		int pmGrade = -1;
		String pm10 = "-1", pm25 = "-1";
		boolean pm10Flag = false, pm25Flag = false;
		for (Microdust microdust : microdustList) {
			if (pm10Flag && pm25Flag)
				break;
			if (!pm10Flag && !microdust.getPm10Value().equals("-")) {
				pm10Flag = true;
				pm10 = microdust.getPm10Value();
				pmGrade = Integer.parseInt(microdust.getPm10Grade());
			}
			if (!pm25Flag && !microdust.getPm25Value().equals("-")) {
				pm25Flag = true;
				pm25 = microdust.getPm25Value();
				pmGrade = Math.max(pmGrade, Integer.parseInt(microdust.getPm25Grade()));
			}
		}

		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(MicrodustResDto.builder()
				.pm10Flag(pm10Flag)
				.pm25Flag(pm25Flag)
				.pm10(Integer.parseInt(pm10))
				.pm25(Integer.parseInt(pm25))
				.grade(MicrodustUtil.getGrade(pmGrade))
				.gradeIcon(MicrodustUtil.getPmGradeIcon(pmGrade))
				.desc(MicrodustUtil.getDesc(pmGrade))
				.build())
			.build();
	}

	private ResDto getTotalResDto(List<Microdust> microdustList) {
		for (Microdust microdust : microdustList) {
			if (!microdust.getCoValue().equals("-") && !microdust.getNo2Value().equals("-") &&
				!microdust.getO3Value().equals("-") && !microdust.getSo2Value().equals("-") &&
				!microdust.getPm10Value24().equals("-") && !microdust.getPm25Value24().equals("-") &&
				!microdust.getKhaiValue().equals("-"))

				return ResDto.builder()
					.resultCode(200)
					.errorMessage(ErrorMessage.SUCCESS)
					.document(MicrodustTotalResDto.builder()
						.pm10(Double.parseDouble(microdust.getPm10Value())).pm10Flag(true)
						.pm25(Double.parseDouble(microdust.getPm25Value())).pm25Flag(true)
						.co(Double.parseDouble(microdust.getCoValue())).coFlag(true)
						.no2(Double.parseDouble(microdust.getNo2Value())).no2Flag(true)
						.o3(Double.parseDouble(microdust.getO3Value())).o3Flag(true)
						.so2(Double.parseDouble(microdust.getSo2Value())).so2Flag(true)
						.cai(Double.parseDouble(microdust.getKhaiValue())).caiFlag(true)
						.caiIcon(MicrodustUtil.getCaiIcon(Double.parseDouble(microdust.getKhaiValue())))
						.build())
					.build();
		}

		Microdust microdust = microdustList.get(0);
		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessage.SUCCESS)
			.document(MicrodustTotalResDto.builder()
				.pm10(MicrodustUtil.getDouble(microdust.getPm10Value()))
				.pm10Flag(MicrodustUtil.getBool(microdust.getPm10Value()))
				.pm25(MicrodustUtil.getDouble(microdust.getPm25Value()))
				.pm25Flag(MicrodustUtil.getBool(microdust.getPm25Value()))
				.co(MicrodustUtil.getDouble(microdust.getCoValue()))
				.coFlag(MicrodustUtil.getBool(microdust.getCoValue()))
				.no2(MicrodustUtil.getDouble(microdust.getNo2Value()))
				.no2Flag(MicrodustUtil.getBool(microdust.getNo2Value()))
				.o3(MicrodustUtil.getDouble(microdust.getO3Value()))
				.o3Flag(MicrodustUtil.getBool(microdust.getO3Value()))
				.so2(MicrodustUtil.getDouble(microdust.getSo2Value()))
				.so2Flag(MicrodustUtil.getBool(microdust.getSo2Value()))
				.cai(MicrodustUtil.getDouble(microdust.getKhaiValue()))
				.caiFlag(MicrodustUtil.getBool(microdust.getKhaiValue()))
				.caiIcon(MicrodustUtil.getCaiIcon(MicrodustUtil.getDouble(microdust.getKhaiValue())))
				.build())
			.build();

	}

	private ResDto getMaskResDto(List<Microdust> microdustList) {
		String pm10 = "-";
		for (Microdust microdust : microdustList) {
			if (!microdust.getPm10Value().equals("-")) {
				pm10 = microdust.getPm10Value();
				break;
			}
		}
		int pm10Value = (pm10.equals("-")) ? 1 : Integer.parseInt(pm10);
		String maskIcon = getMaskIcon(pm10Value);
		String desc = getMaskdesc(maskIcon);

		return ResDto.builder()
			.resultCode(200)
			.errorMessage("SUCCESS")
			.document(MicrodustMaskResDto.builder()
				.maskIcon(maskIcon)
				.desc(desc)
				.build())
			.build();
	}

	private String getMaskIcon(int pm10) {
		if (pm10 >= 0 && pm10 <= 15)
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

	public List<String> getStationList(Double lat, Double lon) {
		try {
			return microdustOpenApi.getStationList(lat, lon);
		} catch (Exception e) {
			return List.of("신촌로", "한강대로", "중구");
		}
	}
}
