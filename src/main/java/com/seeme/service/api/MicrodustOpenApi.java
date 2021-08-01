package com.seeme.service.api;

import com.google.gson.Gson;
import com.seeme.config.ApiConfig;
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

	public Microdust getMainApi(List<String> stationList, int index) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustMainUrl())
			.queryParam(MicrodustUtil.SERVICE_KEY, apiConfig.getMicrodustMainKey())
			.queryParam(MicrodustUtil.RETURN_TYPE, "json")
			.queryParam(MicrodustUtil.NUM_OF_ROWS, 1)
			.queryParam(MicrodustUtil.PAGE_NO, 1)
			.queryParam(MicrodustUtil.STATION_NAME, URLEncoder.encode(stationList.get(index), StandardCharsets.UTF_8))
			.queryParam(MicrodustUtil.DATA_TERM, "DAILY")
			.queryParam(MicrodustUtil.VERSION, 1.1);

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
		JSONObject responseObject = (JSONObject) jsonObject.get("response");
		JSONObject bodyObject = (JSONObject) responseObject.get("body");
		JSONArray jsonArray = (JSONArray) bodyObject.get("items");

		return new Gson().fromJson(jsonArray.get(0).toString(), Microdust.class);
	}

	public List<String> getStationList(Double lat, Double lon) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustStationUrl())
			.query(locationApi.covertWGS84ToTM(lat, lon))
			.queryParam(MicrodustUtil.SERVICE_KEY, apiConfig.getMicrodustMainKey())
			.queryParam(MicrodustUtil.RETURN_TYPE, "json")
			.queryParam(MicrodustUtil.VERSION, "1.0");

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

		return stationList;
	}

	public List<MicrodustDay> getDayApi(String geo) throws IOException, ParseException, NullPointerException {
		String result;

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
			day = day.substring(5, 7) + "." + day.substring(8, 10);

			microdustDayList.add(MicrodustDay.builder()
				.pm10(pm10)
				.pm25(pm25)
				.day(day)
				.build()
			);
		}
		return microdustDayList;
	}
}
