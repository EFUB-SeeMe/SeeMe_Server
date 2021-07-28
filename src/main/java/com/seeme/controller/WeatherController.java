package com.seeme.controller;

import com.seeme.domain.location.Address;
import com.seeme.service.LocationService;
import com.seeme.service.WeatherService;
import com.seeme.util.WeatherUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.ParseException;

@Controller
@AllArgsConstructor
@RequestMapping("weather")
public class WeatherController {

	private final WeatherService weatherService;
	private final LocationService locationService;

	@GetMapping("/main")
	public ResponseEntity<Object> getMain(
		@RequestParam(required = false) String code,
		@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) throws IOException, ParseException, org.json.simple.parser.ParseException {
		if (code != null) {
			Address address = locationService.getAddressByCode(code);
			return ResponseEntity.ok().body(weatherService.getMain(
				address.getLat(), address.getLon()));
		} else if (lat != null && lon != null)
			return ResponseEntity.ok().body(weatherService.getMain(lat, lon));
		else
			return ResponseEntity.ok().body(weatherService.getMain(
				WeatherUtil.DEFAULT_LAT, WeatherUtil.DEFAULT_LON));
	}

	@GetMapping("/time")
	public ResponseEntity<Object> getTime(
		@RequestParam(required = false) String code,
		@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
		if (code != null) {
			Address address = locationService.getAddressByCode(code);
			return ResponseEntity.ok().body(weatherService.getTime(
				address.getLat(), address.getLon()));
		} else if (lat != null && lon != null)
			return ResponseEntity.ok().body(weatherService.getTime(lat, lon));
		else
			return ResponseEntity.ok().body(weatherService.getTime(
				WeatherUtil.DEFAULT_LAT, WeatherUtil.DEFAULT_LON));
	}
}
