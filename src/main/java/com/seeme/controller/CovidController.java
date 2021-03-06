package com.seeme.controller;

import com.seeme.service.CovidService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("covid")
public class CovidController {

	public final CovidService covidService;

	@GetMapping("/main")
	public ResponseEntity<Object> getMain(@RequestParam(required = false) String location,
		@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
		try {
			if (location != null)
				return ResponseEntity.ok().body(covidService.getMain(location));
			else if (lat == null || lon == null)
				return ResponseEntity.ok().body(covidService.getMain("서울"));
			else
				return ResponseEntity.ok()
					.body(covidService.getMain(covidService.getLocation(lat, lon)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("internal server error");
		}
	}

	@GetMapping("/nation")
	public ResponseEntity<Object> getNational() {
		try {
			return ResponseEntity.ok().body(covidService.getRegional("합계"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("internal server error");
		}

	}

	@GetMapping("/region")
	public ResponseEntity<Object> getRegional(@RequestParam(required = false) String location,
		@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
		try {
			if (location != null)
				return ResponseEntity.ok().body(covidService.getRegional(location));
			else if (lat == null || lon == null)
				return ResponseEntity.ok().body(covidService.getRegional("서울"));
			else
				return ResponseEntity.ok()
					.body(covidService.getRegional(covidService.getLocation(lat, lon)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("internal server error");
		}
	}
}