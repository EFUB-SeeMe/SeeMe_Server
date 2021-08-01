package com.seeme.controller;

import com.seeme.service.LocationService;
import com.seeme.service.MicrodustService;
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
class MicrodustControllerTest {

	@Autowired
	MockMvc mvc;
	@Autowired
	WebApplicationContext ctx;

	@Autowired
	private LocationService locationService;
	@Autowired
	private MicrodustService microdustService;

	@BeforeEach()
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.alwaysDo(print())
			.build();
	}

	@Test
	void getMain() throws Exception {
		mvc.perform(get("/microdust/main").param("code", "2824510700"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.mainInfo.resultCode", is(200)))
			.andExpect(jsonPath("$.totalInfo.resultCode", is(200)))
			.andExpect(jsonPath("$.maskInfo.resultCode", is(200)));
	}

	@Test
	void getDayByCode() throws Exception {
		mvc.perform(get("/microdust/day")
			.param("code", "2824510700"))
			.andExpect(status().isOk());
	}
}