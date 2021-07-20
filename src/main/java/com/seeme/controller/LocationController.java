package com.seeme.controller;

import com.seeme.service.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("location")
public class LocationController {

	public final LocationService locationService;

	@GetMapping("/latLonToUmd")
	public String getLatlonToUmd (@RequestParam Double lat, @RequestParam Double lon) {
		try {
			return locationService.getLatlonToUmd(lat, lon);
		} catch (Exception e) {
			e.printStackTrace();
			return "지원하지 않는 위치입니다.";
		}
	}

	@GetMapping("/search")
	public ResponseEntity<Object> searchByUmd (@RequestParam String umd) {
		try {
			return ResponseEntity.ok().body(locationService.searchByUmd(umd));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("internal server error");
		}
	}
}
