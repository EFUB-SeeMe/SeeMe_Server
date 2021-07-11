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
	public ResponseEntity<Object> getMain(
		@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
		try {
			if (lat == null || lon == null)
				return ResponseEntity.ok().body(covidService.getMain("서울"));
			else
				return ResponseEntity.ok()
					.body(covidService.getMain(covidService.getLocation(lat, lon)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("internal server error");
		}
	}

	@GetMapping("/national")
	public ResponseEntity<Object> getNational() {
		return ResponseEntity.ok().body("전국");
	}

	@GetMapping("/regional")
	public ResponseEntity<Object> getRegional(
		@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
			try {
				if (lat == null || lon == null)
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