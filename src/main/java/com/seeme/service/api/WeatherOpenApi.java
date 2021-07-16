package com.seeme.service.api;

import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WeatherOpenApi {

	private final ApiConfig apiConfig;

	public List<JSONObject> getTempFromOpenApi(String date, String nx, String ny) {
		return new ArrayList<>();
	}

}
