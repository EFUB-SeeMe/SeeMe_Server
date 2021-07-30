package com.seeme.domain.weather;

import lombok.Builder;
import lombok.Getter;
import org.json.simple.JSONObject;

@Getter
@Builder
public class Weather {
	private final String date;
	private final int amRain;
	private final int amRainPercent;
	private final String amIcon;
	private final int pmRain;
	private final int pmRainPercent;
	private final String pmIcon;
	private final int max;
	private final int min;
	private final String desc;
}