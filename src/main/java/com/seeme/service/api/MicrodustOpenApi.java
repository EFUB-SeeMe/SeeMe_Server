package com.seeme.service.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seeme.domain.location.TMAddress;
import com.seeme.domain.microdust.Microdust;
import com.seeme.domain.microdust.MicrodustDayDto;
import com.seeme.domain.microdust.MicrodustTimeDto;
import com.seeme.util.JSONParsingUtil;
import com.seeme.util.LocationUtil;
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
import java.lang.reflect.Type;
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
		System.out.println("stationName: " + stationList.toString()); // remove
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
				.time((Integer.parseInt(MicrodustUtil.getDataTime()) + temp) + "시")
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

	public List<MicrodustDayDto> getDayApi(Double lat, Double lon) throws IOException, ParseException, NullPointerException {
		String result = "";

		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustDayUrl())
			.queryParam(MicrodustUtil.LAT, lat)
			.queryParam(MicrodustUtil.LON, lon)
			.queryParam(MicrodustUtil.APP_ID, apiConfig.getMicrodustDayKey());
		URL url = new URL(uriComponentsBuilder.build().toUriString());

		BufferedReader bf;
		bf = new BufferedReader(new InputStreamReader(url.openStream()));
		result = bf.readLine();

		List<MicrodustDayDto> microdustDayDtoList = new ArrayList<>();

		for (int temp = 0; temp < 120; temp++) {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
			JSONArray listObjects = (JSONArray) jsonObject.get("list");
			JSONObject listObject = (JSONObject) listObjects.get(temp);
			JSONObject componentObject = (JSONObject) listObject.get("components");
			int pm25 = (int) (Float.parseFloat(componentObject.get("pm2_5").toString()));
			int pm10 = (int) (Float.parseFloat(componentObject.get("pm10").toString()));
			long dt = Long.parseLong(listObject.get("dt").toString());

			microdustDayDtoList.add(MicrodustDayDto.builder()
				.pm25(pm25)
				.pm10(pm10)
				.dt(dt)
				.build()
			);
		}
		return microdustDayDtoList;
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
	}
}
