package com.seeme.controller;

import com.seeme.domain.location.Address;
import com.seeme.domain.microdust.MicrodustTimeResDto;
import com.seeme.service.MicrodustService;
import com.seeme.util.MicrodustUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("microdust")
public class MicrodustController {

	public final MicrodustService microdustService;

	@GetMapping("/main")
	public ResponseEntity<Object> getMain(
		@RequestParam(required = false) String code,
		@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
		try {
			if (code != null) {
				Address address = microdustService.getAddressByCode(code);
				return ResponseEntity.ok().body(microdustService.getMain(
					microdustService.getStationList(address.getLat(), address.getLon())));
			} else if (lat != null && lon != null)
				return ResponseEntity.ok().body(microdustService.getMain(
					microdustService.getStationList(lat, lon)));
			else
				return ResponseEntity.ok().body(microdustService.getMain(
					MicrodustUtil.DEFAULT_STATION));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("internal server error");
		}
	}

	@GetMapping("/day")
	public ResponseEntity<Object> getDay(
		@RequestParam(required = false) String code,
		@RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
		try {
			if (code != null) {
				Address address = microdustService.getAddressByCode(code);
				return ResponseEntity.ok().body(microdustService.getDay(
					address.getLat() + ";" + address.getLon()));
			} else if (lat != null && lon != null)
				return ResponseEntity.ok().body(microdustService.getDay(lat + ";" + lon));
			else
				return ResponseEntity.ok().body(microdustService.getDay(MicrodustUtil.DEFAULT_GEO));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("internal server error");
		}
	}

	/*
	@GetMapping("/time")
	public ResponseEntity<Object> getTime(
		@RequestParam(required = false) Double lat, Double lon) {
		String location = lat + "," + lon;
		try {
			if (lat == null || lon == null) {
				MicrodustTimeResDto microdustTimeResDto = microdustService.getFirstTime(microdustService.getStationList(37.56197784552834, 126.9468124393769));
				List<MicrodustTimeResDto> microdustTimeResDtoList = microdustService.getOtherTime("37.56197784552834,126.9468124393769");
				microdustTimeResDtoList.add(0, microdustTimeResDto);
				return ResponseEntity.ok().body(microdustService.getOtherTime("37.56197784552834,126.9468124393769"));
			} else {
				MicrodustTimeResDto microdustTimeResDto = microdustService.getFirstTime(microdustService.getStationList(lat, lon));
				microdustService.getOtherTime(location).add(0, microdustTimeResDto);
				return ResponseEntity.ok().body(microdustService.getOtherTime(location));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("internal server error");
		}
	}

	@GetMapping("/map")
	public ResponseEntity<Object> getMap() {
		try {
			return ResponseEntity.ok().body(microdustService.getMap());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("internal server error");
		}
	}
	 */
}
