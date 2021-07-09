package com.seeme.controller;

import com.seeme.service.CovidService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
            return ResponseEntity.internalServerError().body("에러");
        }
    }

    @GetMapping("/national")
    public ResponseEntity<Object> getNational() {
        return ResponseEntity.ok().body("전국");
    }

    @GetMapping("/regional")
    public ResponseEntity<Object> getRegional() {
        return ResponseEntity.ok().body("지역");
    }
}
