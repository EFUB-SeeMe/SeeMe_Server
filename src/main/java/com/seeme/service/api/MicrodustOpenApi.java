package com.seeme.service.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seeme.domain.microdust.Microdust;
import com.seeme.domain.microdust.MicrodustDayDto;
import com.seeme.domain.microdust.MicrodustTimeDto;
import com.seeme.util.JSONParsingUtil;
import com.seeme.util.MicrodustUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

	public Microdust getMainApi(List<String> stationList) throws IOException, ParseException {
		int index = 0, pmGrade = -1;
		String pm10 = "-1", pm25 = "-1";
		boolean pm10Flag = false, pm25Flag = false;
		while (index < 3) {
			if (pm10Flag && pm25Flag)
				break;
			JSONObject jsonObject = getMicrodust(stationList, index++);
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

		return Microdust.builder()
			.pm10Value(pm10)
			.pm25Value(pm25)
			.pm10Grade(String.valueOf(pmGrade))
			.build();
	}

	private JSONObject getMicrodust(List<String> stationList, int index) throws IOException, ParseException {
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
		return getStationListByJSON(sb);
	}

	public List<String> getStationListByTM(String tmX, String tmY) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustStationUrl())
			.queryParam(MicrodustUtil.SERVICE_KEY, apiConfig.getMicrodustMainKey())
			.queryParam(MicrodustUtil.RETURN_TYPE, "json")
			.queryParam(MicrodustUtil.TM_X, tmX)
			.queryParam(MicrodustUtil.TM_Y, tmY)
			.queryParam(MicrodustUtil.VERSION, "1.0");
		System.out.println(uriComponentsBuilder.build());

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);
		return getStationListByJSON(sb);
	}

	public List<String> getStationListByJSON(StringBuilder sb) throws ParseException {
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

	public List<MicrodustTimeDto> getTimeApi(String measuringStation) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustMainUrl())
			.queryParam(MicrodustUtil.SERVICE_KEY, apiConfig.getMicrodustMainKey())
			.queryParam(MicrodustUtil.RETURN_TYPE, "json")
			.queryParam(MicrodustUtil.NUM_OF_ROWS, 20)
			.queryParam(MicrodustUtil.PAGE_NO, 1)
			.queryParam(MicrodustUtil.STATION_NAME, URLEncoder.encode(measuringStation, StandardCharsets.UTF_8))
			.queryParam(MicrodustUtil.DATA_TERM, "DAILY")
			.queryParam(MicrodustUtil.VERSION, 1.1);

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		List<MicrodustTimeDto> microdustTimeDtoList = new ArrayList<>();

		for (int temp = 0; temp < 20; temp++) {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
			JSONObject responseObject = (JSONObject) jsonObject.get("response");
			JSONObject bodyObject = (JSONObject) responseObject.get("body");
			JSONArray itemsObjects = (JSONArray) bodyObject.get("items");
			JSONObject itemObject = (JSONObject) itemsObjects.get(temp);
			microdustTimeDtoList.add(MicrodustTimeDto.builder()
				.stationName(measuringStation)
				.time((Integer.parseInt(MicrodustUtil.getDataTime()) + temp) + "?‹œ")
				.pm10Value24(Integer.parseInt(itemObject.get("pm10Value24").toString()))
				.pm25Value24(Integer.parseInt(itemObject.get("pm25Value24").toString()))
				.build()
			);
		}
		return microdustTimeDtoList;
	}

	public String getTagValue(String tag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
		Node nValue = nlList.item(0);
		if (nValue == null)
			return null;
		return nValue.getNodeValue();
	}

	public List<MicrodustDayDto> getDayApi(String geo) throws IOException, ParseException, NullPointerException {
		String result = "";

		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustDayUrl() + "/geo:" + geo + "/")
			.queryParam(MicrodustUtil.TOKEN, apiConfig.getMicrodustDayKey());
		URL url = new URL(uriComponentsBuilder.build().toUriString());

		BufferedReader bf;
		bf = new BufferedReader(new InputStreamReader(url.openStream()));
		result = bf.readLine();
		List<MicrodustDayDto> microdustDayDtoList = new ArrayList<>();

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

			int pm10 = AQItoPM10(Integer.parseInt(pm10Object.get("avg").toString()));
			int pm25 = AQItoPM25(Integer.parseInt(pm25Object.get("avg").toString()));
			String day = (pm25Object.get("day").toString());

			microdustDayDtoList.add(MicrodustDayDto.builder()
				.pm10(pm10)
				.pm25(pm25)
				.day(day)
				.build()
			);
		}
		return microdustDayDtoList;
	}

	public int AQItoPM25(int avg) {
		double conMax = 0, conMin = 0;
		int aqiMax = 0, aqiMin = 0;
		if (avg <= 50) {
			conMax = 12.0;
			conMin = 0.0;
			aqiMax = 50;
			aqiMin = 0;
		} else if (avg <= 100) {
			conMax = 35.4;
			conMin = 12.1;
			aqiMax = 100;
			aqiMin = 51;
		} else if (avg <= 150) {
			conMax = 55.4;
			conMin = 35.5;
			aqiMax = 150;
			aqiMin = 101;
		} else if (avg <= 200) {
			conMax = 150.4;
			conMin = 55.5;
			aqiMax = 200;
			aqiMin = 151;
		} else if (avg <= 300) {
			conMax = 250.4;
			conMin = 150.5;
			aqiMax = 300;
			aqiMin = 201;
		} else if (avg <= 400) {
			conMax = 350.4;
			conMin = 250.5;
			aqiMax = 400;
			aqiMin = 301;
		} else if (avg <= 500) {
			conMax = 500.4;
			conMin = 350.5;
			aqiMax = 500;
			aqiMin = 401;
		}
		return (int) Math.round((avg - aqiMin) * (conMax - conMin) / (aqiMax - aqiMin) + conMin);
	}

	public int AQItoPM10(int avg) {
		double conMax = 0, conMin = 0;
		int aqiMax = 0, aqiMin = 0;
		if (avg <= 50) {
			conMax = 54.0;
			conMin = 0.0;
			aqiMax = 50;
			aqiMin = 0;
		} else if (avg <= 100) {
			conMax = 154.0;
			conMin = 55.0;
			aqiMax = 100;
			aqiMin = 51;
		} else if (avg <= 150) {
			conMax = 254.0;
			conMin = 155.0;
			aqiMax = 150;
			aqiMin = 101;
		} else if (avg <= 200) {
			conMax = 354.0;
			conMin = 255.0;
			aqiMax = 200;
			aqiMin = 151;
		} else if (avg <= 300) {
			conMax = 424.0;
			conMin = 355.0;
			aqiMax = 300;
			aqiMin = 201;
		} else if (avg <= 400) {
			conMax = 504.0;
			conMin = 425.0;
			aqiMax = 400;
			aqiMin = 301;
		} else if (avg <= 500) {
			conMax = 604.0;
			conMin = 505.0;
			aqiMax = 500;
			aqiMin = 401;
		}
		return (int) Math.round((avg - aqiMin) * (conMax - conMin) / (aqiMax - aqiMin) + conMin);
	}


	public List<Microdust> getMap() throws IOException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustMapUrl())
			.queryParam(MicrodustUtil.SERVICE_KEY, apiConfig.getMicrodustMapKey())
			.queryParam(MicrodustUtil.RETURN_TYPE, "json")
			.queryParam(MicrodustUtil.NUM_OF_ROWS, "600")
			.queryParam(MicrodustUtil.PAGE_NO, "1")
			.queryParam(MicrodustUtil.SIDO_NAME, URLEncoder.encode("? „êµ?", StandardCharsets.UTF_8))
			.queryParam(MicrodustUtil.VERSION, "1.0");

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
		jsonObject = (JSONObject) jsonObject.get("response");
		jsonObject = (JSONObject) jsonObject.get("body");
		JSONArray jsonArray = (JSONArray) jsonObject.get("items");

		return new Gson().fromJson(jsonArray.toString(),
			new TypeToken<List<Microdust>>() {
			}.getType());
	}
}
