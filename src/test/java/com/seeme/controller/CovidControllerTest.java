package com.seeme.controller;

import com.seeme.service.CovidService;
import com.seeme.service.LocationService;
import com.seeme.service.api.CovidOpenApi;
import com.seeme.service.api.LocationApi;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
class CovidControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	private CovidService covidService;

	@Test
	void getNation() throws Exception {
		mvc.perform(get("/covid/nation"))
			.andExpect(status().isOk());
	}

	@Test
	void getMainByLocation() throws Exception {
		mvc.perform(get("/covid/main").param("location", "인천"))
			.andExpect(status().isOk());
	}

	@Test
	void getRegionByLatLon() throws Exception {
		mvc.perform(get("/covid/region").param("lat", "37.55509759237768").param("lon", "126.93688074904087"))
			.andExpect(status().isOk());
	}
}