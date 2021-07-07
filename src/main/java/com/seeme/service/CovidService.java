package com.seeme.service;

import com.seeme.domain.covid.CovidResDto;
import org.springframework.stereotype.Service;

@Service
public class CovidService {

	public CovidResDto getMain() {
		return CovidResDto.builder()
			.location("인천시 계양구")
			.compRegion(6)
			.compTotal(128)
			.coronicRegion(42)
			.coronicTotal(789)
			.isIncRegion(-1)
			.isIncTotal(1)
			.build();
	}
}
