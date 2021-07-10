package com.seeme.api;

import com.seeme.util.LocationUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class LocationApi {

	private final ApiConfig apiConfig;

	public String covertGpsToAddress(Double latitude, Double longitude) throws Exception {
		UriComponentsBuilder uriComponentsBuilder =
			UriComponentsBuilder
				.fromUriString(apiConfig.getLocationUrl())
				.queryParam(LocationUtil.LATITUDE_LONGITUDE, LocationUtil.getLatLonValue(latitude, longitude))
				.queryParam(LocationUtil.LANGUAGE, LocationUtil.LANGUAGE_VALUE)
				.queryParam(LocationUtil.KEY, apiConfig.getLocationKey());

		StringBuilder jsonString = new StringBuilder();
		String buf;
		URL url = new URL(uriComponentsBuilder.build().toUriString());
		URLConnection conn = url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(
			conn.getInputStream(), StandardCharsets.UTF_8));
		while ((buf = br.readLine()) != null) {
			jsonString.append(buf);
		}
		JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonString.toString());
		JSONArray jsonArray = (JSONArray) jsonObject.get("results");
		jsonObject = (JSONObject) jsonArray.get(0);
		return (String) jsonObject.get("formatted_address");
	}

}
