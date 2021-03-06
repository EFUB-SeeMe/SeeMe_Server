package com.seeme.service.api;

import com.seeme.config.ApiConfig;
import com.seeme.util.JSONParsingUtil;
import com.seeme.util.LocationUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
@AllArgsConstructor
public class LocationApi {

	private final ApiConfig apiConfig;

	public String covertGpsToAddress(Double latitude, Double longitude) throws Exception {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getLocationUrl())
			.queryParam(LocationUtil.LATITUDE_LONGITUDE, LocationUtil.getLatLonValue(latitude, longitude))
			.queryParam(LocationUtil.LANGUAGE, LocationUtil.LANGUAGE_VALUE)
			.queryParam(LocationUtil.KEY, apiConfig.getLocationKey());

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
		JSONArray jsonArray = (JSONArray) jsonObject.get("results");
		jsonObject = (JSONObject) jsonArray.get(0);
		return (String) jsonObject.get("formatted_address");
	}

	public String covertGpsToSpecificAddress(Double lat, Double lon) throws Exception {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getSpecificAddressUrl())
			.queryParam(LocationUtil.X, lon)
			.queryParam(LocationUtil.Y, lat);

		StringBuilder sb = JSONParsingUtil.convertJSONToSBWithAuth(
			uriComponentsBuilder, apiConfig.getSpecificAddressKey());
		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
		JSONArray jsonArray = (JSONArray) jsonObject.get("documents");
		jsonObject = (JSONObject) jsonArray.get(0);
		return LocationUtil.getRegion2AndRegion3(jsonObject);
	}


	public JSONObject searchByLatLon(Double lat, Double lon) throws IOException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getSpecificAddressUrl())
			.queryParam(LocationUtil.X, lon)
			.queryParam(LocationUtil.Y, lat);

		StringBuilder sb = JSONParsingUtil.convertJSONToSBWithAuth(
			uriComponentsBuilder, apiConfig.getSpecificAddressKey());
		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
		JSONArray jsonArray = (JSONArray) jsonObject.get("documents");
		return (JSONObject) jsonArray.get(0);
	}

	public String covertWGS84ToTM(Double lat, Double lon) throws IOException {
		String accessToken = getTMAccessToken();

		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getConvertWGS84ToTMUrl())
			.queryParam(LocationUtil.ACCESS_TOKEN, accessToken)
			.queryParam(LocationUtil.SRC, LocationUtil.WGS84_CODE)
			.queryParam(LocationUtil.DST, LocationUtil.GRS80_CODE)
			.queryParam(LocationUtil.POS_X, lon)
			.queryParam(LocationUtil.POS_Y, lat);

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
		jsonObject = (JSONObject) jsonObject.get("result");
		return "tmX=" + jsonObject.get("posX").toString() +
			"&tmY=" + jsonObject.get("posY").toString();
	}

	private String getTMAccessToken() throws IOException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getConvertWGS84ToTMAuthUrl())
			.queryParam(LocationUtil.CONSUMER_ID, apiConfig.getConvertWGS84ToTMAuthId())
			.queryParam(LocationUtil.CONSUMER_KEY, apiConfig.getConvertWGS84ToTMAuthKey());

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		JSONObject jsonObject = (JSONObject) JSONValue.parse(sb.toString());
		jsonObject = (JSONObject) jsonObject.get("result");
		return jsonObject.get("accessToken").toString();
	}
}

