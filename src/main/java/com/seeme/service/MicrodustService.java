package com.seeme.service;

import com.seeme.domain.ResDto;
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

    public MicrodustMainResDto getMain(List<String> measuringStationList) {
        ResDto main = getMainResDto(measuringStationList);
        ResDto total = getOtherResDto(measuringStationList);

        return MicrodustMainResDto.builder()
            .mainInfo(main)
            .totalInfo(total)
            .maskInfo(getRecResDto(main, total))
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
            .grade(String.valueOf(pmGrade))
            .gradeIcon(MicrodustUtil.getPmGradeIcon(pmGrade))
            .desc(MicrodustUtil.getDesc(pmGrade))
            .build();
    }

    private ResDto getOtherResDto(List<String> measuringStationList) {
        ResDto resDto;
        try {
            resDto = ResDto.builder()
                .resultCode(200)
                .errorMessage(ErrorMessage.SUCCESS)
                .document(microdustOpenApi.getOtherApi(measuringStationList, 0))
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

    private ResDto getRecResDto(ResDto microdustResDto, ResDto otherResDto) {
        return ResDto.builder()
            .resultCode(200)
            .errorMessage(ErrorMessage.SUCCESS)
            .document(MicrodustRecResDto.builder()
                .maskIcon(getMaskIcon(microdustResDto))
                .desc(getdesc(microdustResDto))
                .build())
            .build();
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
