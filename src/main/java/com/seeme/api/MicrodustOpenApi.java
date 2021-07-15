package com.seeme.api;

import com.seeme.domain.microdust.Microdust;
import com.seeme.util.JSONParsingUtil;
import com.seeme.util.MicrodustUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class MicrodustOpenApi {

	private final ApiConfig apiConfig;
	private final LocationApi locationApi;

	public Microdust getMainApi(String measuringStation) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustMainUrl())
			.queryParam(MicrodustUtil.SERVICE_KEY, apiConfig.getMicrodustMainKey())
			.queryParam(MicrodustUtil.RETURN_TYPE, "json")
			.queryParam(MicrodustUtil.NUM_OF_ROWS, 1)
			.queryParam(MicrodustUtil.PAGE_NO, 1)
			.queryParam(MicrodustUtil.STATION_NAME, URLEncoder.encode(measuringStation, StandardCharsets.UTF_8))
			.queryParam(MicrodustUtil.DATA_TERM, "DAILY")
			.queryParam(MicrodustUtil.VERSION, 1.0);
		System.out.println(uriComponentsBuilder.build());

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
		JSONObject responseObject = (JSONObject) jsonObject.get("response");
		JSONObject bodyObject = (JSONObject) responseObject.get("body");
		JSONArray itemsObjects = (JSONArray) bodyObject.get("items");
		JSONObject itemObject = (JSONObject) itemsObjects.get(0);

		return Microdust.builder()
			.stationName(measuringStation)
			.pm10Value(Integer.parseInt(itemObject.get("pm10Value").toString()))
			.pm25Value(Integer.parseInt(itemObject.get("pm25Value").toString()))
			.pmGrade(MicrodustUtil.getGrade
				(itemObject.get("pm10Grade").toString(), itemObject.get("pm25Grade").toString()))
			.build();
	}

	public String getMeasuringStation(Double lat, Double lon) throws IOException, ParseException {
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
		JSONArray itemsObject = (JSONArray) bodyObject.get("items");
		JSONObject itemObject = (JSONObject) itemsObject.get(0);
		System.out.println("측정소의 주소: " + itemObject.get("addr") + ", stationName: " + itemObject.get("stationName"));
		return itemObject.get("stationName").toString();
	}
}
