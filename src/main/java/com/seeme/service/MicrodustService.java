package com.seeme.service;

import com.seeme.domain.ResDto;
import com.seeme.domain.microdust.*;
import com.seeme.service.api.MicrodustOpenApi;
import com.seeme.util.ErrorMessageUtil;
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
			ResDto mainResDto = getMainResDto(microdustList);
			return MicrodustMainResDto.builder()
				.mainInfo(mainResDto)
				.totalInfo(getTotalResDto(microdustList))
				.maskInfo(getMaskResDto(mainResDto))
				.build();
		} catch (ParseException | IOException e) {
			e.printStackTrace();
			ResDto resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessageUtil.JSON_PARSING_ERROR)
				.document(null)
				.build();
			return MicrodustMainResDto.builder()
				.mainInfo(resDto).totalInfo(resDto).maskInfo(resDto).build();
		} catch (Exception e) {
			e.printStackTrace();
			ResDto resDto = ResDto.builder()
				.resultCode(500)
				.errorMessage(ErrorMessageUtil.UNKNOWN_ERROR)
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
				pmGrade = MicrodustUtil.get10GradeByValue(microdust.getPm10Value());
			}
			if (!pm25Flag && !microdust.getPm25Value().equals("-")) {
				pm25Flag = true;
				pm25 = microdust.getPm25Value();
				pmGrade = Math.max(pmGrade, MicrodustUtil.get25GradeByValue(microdust.getPm25Value()));
			}
		}

		return ResDto.builder()
			.resultCode(200)
			.errorMessage(ErrorMessageUtil.SUCCESS)
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
					.errorMessage(ErrorMessageUtil.SUCCESS)
					.document(MicrodustTotalResDto.builder()
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
			.errorMessage(ErrorMessageUtil.SUCCESS)
			.document(MicrodustTotalResDto.builder()
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

	private ResDto getMaskResDto(ResDto mainResDto) {
		MicrodustResDto microdust = (MicrodustResDto) mainResDto.getDocument();

		return ResDto.builder()
			.resultCode(200)
			.errorMessage("SUCCESS")
			.document(MicrodustMaskResDto.builder()
				.maskIcon(MicrodustUtil.getMaskIcon(microdust.getGrade()))
				.desc(MicrodustUtil.getMaskdesc(microdust.getGrade()))
				.build())
			.build();
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
			e.printStackTrace();
			return List.of("신촌로", "한강대로", "중구");
		}
	}
}
