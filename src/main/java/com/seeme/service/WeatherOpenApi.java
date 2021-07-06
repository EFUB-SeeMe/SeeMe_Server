package com.seeme.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@ConfigurationProperties(prefix = "vilage-fcst-info-service")
public class WeatherOpenApi {

	String serviceKey;

	public void setServiceKey(String serviceKey) {
		this.serviceKey = serviceKey;
	}

	public List<JSONObject> getTempFromOpenApi(String date, String nx, String ny)
		throws IOException, ParseException {
		URL url = new URL(
			"http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" +
				"?" + "serviceKey" + "=" + serviceKey +
				"&" + "pageNo" + "=" + "1" +
				"&" + "numOfRows" + "=" + "250" +
				"&" + "dataType" + "=" + "JSON" +
				"&" + "base_date" + "=" + date +
				"&" + "base_time" + "=" + "0500" +
				"&" + "nx" + "=" + nx +
				"&" + "ny" + "=" + ny
		);
		System.out.println(url.toString());

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		BufferedReader bufferedReader;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}

		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
		}
		conn.disconnect();
		bufferedReader.close();

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
		JSONObject responseObject = (JSONObject) jsonObject.get("response");
		JSONObject bodyObject = (JSONObject) responseObject.get("body");
		JSONObject itemsObject = (JSONObject) bodyObject.get("items");
		JSONArray itemObjects = (JSONArray) itemsObject.get("item");

		List<JSONObject> resultArray = new ArrayList<>();
		for (Object itemObject : itemObjects) {
			JSONObject item = (JSONObject) itemObject;
			if (item.get("category").equals("TMP")) {
				resultArray.add(item);
			}
		}

		return resultArray;
	}

}
