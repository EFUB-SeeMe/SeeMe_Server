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
	public ResponseEntity<Object> getMain() {
		try {
			return ResponseEntity.ok().body(covidService.getMain());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("서버에러");
		}
	}

	@GetMapping("/national")
	public ResponseEntity<Object> getNational() {
		return ResponseEntity.ok().body("전국");
	}

	@GetMapping("/regional")
	public ResponseEntity<Object> getRegional()(
	@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
		try {
			if (lat == null || lon == null)
				return ResponseEntity.ok().body(covidService.getRegonal("서울"));
			else
				return ResponseEntity.ok()
					.body(covidService.getRegional(covidService.getLocation(lat, lon)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("internal server error");
		}
	}
}