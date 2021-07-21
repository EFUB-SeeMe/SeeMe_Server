package com.seeme.service.api;

import com.seeme.domain.microdust.*;
import com.seeme.util.JSONParsingUtil;
import com.seeme.util.MicrodustUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MicrodustOpenApi {

	private final ApiConfig apiConfig;
	private final LocationApi locationApi;

	public JSONObject getMainApi(List<String> stationList, int index) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustMainUrl())
			.queryParam(MicrodustUtil.SERVICE_KEY, apiConfig.getMicrodustMainKey())
			.queryParam(MicrodustUtil.RETURN_TYPE, "json")
			.queryParam(MicrodustUtil.NUM_OF_ROWS, 1)
			.queryParam(MicrodustUtil.PAGE_NO, 1)
			.queryParam(MicrodustUtil.STATION_NAME, URLEncoder.encode(stationList.get(index), StandardCharsets.UTF_8))
			.queryParam(MicrodustUtil.DATA_TERM, "DAILY")
			.queryParam(MicrodustUtil.VERSION, 1.0);
		System.out.println(uriComponentsBuilder.build());

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
		JSONObject responseObject = (JSONObject) jsonObject.get("response");
		JSONObject bodyObject = (JSONObject) responseObject.get("body");
		JSONArray itemsObjects = (JSONArray) bodyObject.get("items");
		return (JSONObject) itemsObjects.get(0);
	}

	public List<String> getStationList(Double lat, Double lon) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustStationUrl())
			.query(locationApi.covertWGS84ToTM(lat, lon))
			.queryParam(MicrodustUtil.SERVICE_KEY, apiConfig.getMicrodustMainKey())
			.queryParam(MicrodustUtil.RETURN_TYPE, "json")
			.queryParam(MicrodustUtil.VERSION, "1.0");
		System.out.println(uriComponentsBuilder.build());

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
		JSONObject responseObject = (JSONObject) jsonObject.get("response");
		JSONObject bodyObject = (JSONObject) responseObject.get("body");
		int totalCount = Integer.parseInt(bodyObject.get("totalCount").toString());
		JSONArray itemsObject = (JSONArray) bodyObject.get("items");

		int index = 0;
		List<String> stationList = new ArrayList<>();
		while (index < totalCount) {
			JSONObject itemObject = (JSONObject) itemsObject.get(index++);
			stationList.add(itemObject.get("stationName").toString());
		}
		System.out.println("stationName: " + stationList); // remove
		return stationList;
	}

	public MicrodustOtherResDto getOtherApi(List<String> measuringStationList) {
		// TODO: call api, parsing data, and return valid value;

		return MicrodustOtherResDto.builder()
			.co(20).coFlag(true)
			.no2(18).no2Flag(true)
			.o3(15).o3Flag(true)
			.so2(10).so2Flag(true)
			.build();
	}

	public List<MicrodustDay> getDayApi(String geo) throws IOException, ParseException, NullPointerException {
		String result = "";

		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustDayUrl() + "/geo:" + geo + "/")
			.queryParam(MicrodustUtil.TOKEN, apiConfig.getMicrodustDayKey());
		URL url = new URL(uriComponentsBuilder.build().toUriString());

		BufferedReader bf;
		bf = new BufferedReader(new InputStreamReader(url.openStream()));
		result = bf.readLine();
		List<MicrodustDay> microdustDayList = new ArrayList<>();

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
		JSONObject dataObject = (JSONObject) jsonObject.get("data");
		JSONObject forecastObject = (JSONObject) dataObject.get("forecast");
		JSONObject dailyObject = (JSONObject) forecastObject.get("daily");
		JSONArray pm10Objects = (JSONArray) dailyObject.get("pm10");
		JSONArray pm25Objects = (JSONArray) dailyObject.get("pm25");

		for (int temp = 0; temp < 5; temp++) {
			JSONObject pm10Object = (JSONObject) pm10Objects.get(temp);
			JSONObject pm25Object = (JSONObject) pm25Objects.get(temp);

			int pm10 = MicrodustUtil.AQItoPM10(Integer.parseInt(pm10Object.get("avg").toString()));
			int pm25 = MicrodustUtil.AQItoPM25(Integer.parseInt(pm25Object.get("avg").toString()));
			String day = (pm25Object.get("day").toString());
			day = day.substring(5, 7)+"."+day.substring(8,10);

			microdustDayList.add(MicrodustDay.builder()
				.pm10(pm10)
				.pm25(pm25)
				.day(day)
				.build()
			);
		}
		return microdustDayList;
	}

	/*
	public MicrodustTime getFirstTimeApi(List<String> stationList) throws IOException, ParseException {
		int index = 0, pm10 = -1, pm25 = -1, pmGrade = -1;
		boolean pm10Flag = false, pm25Flag = false;
		while (index < 3) {
			if (pm10Flag && pm25Flag)
				break;
			JSONObject microdust = getMicrodust(stationList, index++);
			if (!pm10Flag && !microdust.get("pm10Value").equals("-")) {
				pm10Flag = true;
				pm10 = Integer.parseInt(microdust.get("pm10Value").toString());
				pmGrade = Integer.parseInt(microdust.get("pm10Grade").toString());
			}
			if (!pm25Flag && !microdust.get("pm25Value").equals("-")) {
				pm25Flag = true;
				pm25 = Integer.parseInt(microdust.get("pm25Value").toString());
				pmGrade = Math.max(pmGrade, Integer.parseInt(microdust.get("pm25Grade").toString()));
			}
		}

		return MicrodustTime.builder()
			.startTime("현재")
			.pm10Value(pm10)
			.pm25Value(pm25)
			.build();
	}


	public List<MicrodustTime> getOtherTimeApi(String location) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustTimeUrl())
			.queryParam(MicrodustUtil.LOCATION, location)
			.queryParam(MicrodustUtil.FIELDS, "particulateMatter10")
			.queryParam(MicrodustUtil.FIELDS, "particulateMatter25")
			.queryParam(MicrodustUtil.TIME_STEPS, "1h")
			.queryParam(MicrodustUtil.UNITS, "metric")
			.queryParam(MicrodustUtil.API_KEY, apiConfig.getMicrodustTimeKey());
		URL url = new URL(uriComponentsBuilder.build().toUriString());

		BufferedReader bf;
		bf = new BufferedReader(new InputStreamReader(url.openStream()));
		String result = bf.readLine();

		List<MicrodustTime> microdustTimeList = new ArrayList<>();

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
		JSONObject dataObject = (JSONObject) jsonObject.get("data");
		JSONArray timelinesObject = (JSONArray) dataObject.get("timelines");
		JSONObject timeObject = (JSONObject) timelinesObject.get(0);
		JSONArray intervalsObjects = (JSONArray) timeObject.get("intervals");

		for (int temp = 0; temp < 48; temp++) {
			JSONObject listObject = (JSONObject) intervalsObjects.get(temp);
			JSONObject pmObject = (JSONObject) listObject.get("values");
			int pm10 = (int) Math.round(Double.parseDouble(pmObject.get("particulateMatter10").toString()));
			int pm25 = (int) Math.round(Double.parseDouble(pmObject.get("particulateMatter25").toString()));
			String startTime = listObject.get("startTime").toString().substring(11, 18);
			String[] clock = startTime.split(":");
			String time = MicrodustUtil.getTime(clock[0]) + "시";

			microdustTimeList.add(MicrodustTime.builder()
				.startTime(time)
				.pm25Value(pm25)
				.pm10Value(pm10)
				.build()
			);
		}
		return microdustTimeList;
	}

	public List<Microdust> getMap() throws IOException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustMapUrl())
			.queryParam(MicrodustUtil.SERVICE_KEY, apiConfig.getMicrodustMapKey())
			.queryParam(MicrodustUtil.RETURN_TYPE, "json")
			.queryParam(MicrodustUtil.NUM_OF_ROWS, "600")
			.queryParam(MicrodustUtil.PAGE_NO, "1")
			.queryParam(MicrodustUtil.SIDO_NAME, URLEncoder.encode("전국", StandardCharsets.UTF_8))
			.queryParam(MicrodustUtil.VERSION, "1.0");

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
		jsonObject = (JSONObject) jsonObject.get("response");
		jsonObject = (JSONObject) jsonObject.get("body");
		JSONArray jsonArray = (JSONArray) jsonObject.get("items");

		return new Gson().fromJson(jsonArray.toString(),
			new TypeToken<List<Microdust>>() {
			}.getType());
	} */
}
