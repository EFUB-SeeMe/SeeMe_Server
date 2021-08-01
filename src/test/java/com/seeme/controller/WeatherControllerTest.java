package com.seeme.controller;

import com.seeme.service.LocationService;
import com.seeme.service.MicrodustService;
import com.seeme.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.transaction.Transactional;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
class WeatherControllerTest {

	@Autowired
	MockMvc mvc;
	@Autowired
	WebApplicationContext ctx;

	@Autowired
	private WeatherService weatherService;
	@Autowired
	private LocationService locationService;

	@BeforeEach()
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.alwaysDo(print())
			.build();
	}

	@Test
	void getMain() throws Exception {
		mvc.perform(get("/weather/main").param("code", "2824510700"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.currentInfo.resultCode", is(200)))
			.andExpect(jsonPath("$.minmaxInfo.resultCode", is(200)))
			.andExpect(jsonPath("$.ootdInfo.resultCode", is(200)))
			.andExpect(jsonPath("$.weekInfo.resultCode", is(200)));

	}

	@Test
	void getTime() throws Exception {
		mvc.perform(get("/weather/time").param("code", "2824510700"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.tempInfo.resultCode", is(200)))
			.andExpect(jsonPath("$.rainInfo.resultCode", is(200)));

	}
}