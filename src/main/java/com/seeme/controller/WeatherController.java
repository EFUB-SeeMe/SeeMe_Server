package com.seeme.controller;

import com.seeme.service.WeatherService;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/weather")
public class WeatherController {

	private final WeatherService weatherService;

	@GetMapping("/temp")
	public ResponseEntity<Object> getTemperature() throws IOException, ParseException {
		return ResponseEntity.accepted().body(weatherService.getTemperature("53", "127"));
	}
}
