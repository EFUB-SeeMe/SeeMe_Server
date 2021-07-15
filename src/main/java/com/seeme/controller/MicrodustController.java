package com.seeme.controller;

import com.seeme.service.MicrodustService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("microdust")
public class MicrodustController {

	public final MicrodustService microdustService;

	@GetMapping("/main")
	public ResponseEntity<Object> getMain(
			@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
		try {
			if (lat == null || lon == null)
				return ResponseEntity.ok().body(microdustService.getMain("삼산"));
			else
				return ResponseEntity.ok()
						.body(microdustService.getMain(microdustService.getMeasuringStation(lat, lon)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("internal server error");
		}
	}

	@GetMapping("/time")
	public ResponseEntity<Object> getTime(
			@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
		try {
			if (lat == null || lon == null)
				return ResponseEntity.ok().body(microdustService.getTime("삼산"));
			else
				return ResponseEntity.ok().body(microdustService.getTime(microdustService.getMeasuringStation(lat, lon)));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("internal server error");
		}
	}
}
