package com.seeme.controller;

import com.seeme.service.LocationService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
class LocationControllerTest {

	@Autowired
	MockMvc mvc;
	@Autowired
	WebApplicationContext ctx;

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
	void getLatlonToAddress() throws Exception {
		mvc.perform(get("/location/latlon2address")
			.param("lat", "37.12335")
			.param("lon", "127.23412"))
			.andExpect(status().isOk())
			.andExpect(content().string("용인시 처인구 이동읍"));
	}

	@Test
	void searchByLatLon() throws Exception {
		mvc.perform(get("/location/latlon")
			.param("lat", "37.12335")
			.param("lon", "127.23412"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.res", is(true)));
	}

	@Test
	void searchByUmd() throws Exception {
		mvc.perform(get("/location/umd")
			.param("umd", "남산동"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalCount", is(7)));
	}
}
