package com.seeme.controller;

import com.seeme.domain.location.Address;
import com.seeme.service.LocationService;
import com.seeme.service.MicrodustService;
import com.seeme.util.MicrodustUtil;
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

    public final LocationService locationService;
    public final MicrodustService microdustService;

    @GetMapping("/main")
    public ResponseEntity<Object> getMain(
        @RequestParam(required = false) String code,
        @RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
        if (code != null) {
            Address address = locationService.getAddressByCode(code);
            return ResponseEntity.ok().body(microdustService.getMain(
                microdustService.getStationList(address.getLat(), address.getLon())));
        } else if (lat != null && lon != null)
            return ResponseEntity.ok().body(microdustService.getMain(
                microdustService.getStationList(lat, lon)));
        else
            return ResponseEntity.ok().body(microdustService.getMain(
                MicrodustUtil.DEFAULT_STATION));
    }

    @GetMapping("/day")
    public ResponseEntity<Object> getDay(
        @RequestParam(required = false) String code,
        @RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
        try {
            if (code != null) {
                Address address = locationService.getAddressByCode(code);
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
}
