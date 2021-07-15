package com.seeme.service;

import com.seeme.domain.covid.Coronic;
import com.seeme.domain.covid.CovidRegionalDto;
import com.seeme.domain.covid.CovidRegionalResDto;
import com.seeme.domain.microdust.MicrodustDayResDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MicrodustService {
	public MicrodustDayResDto getDay() throws ParserConfigurationException, SAXException, IOException {

		int dust_am = 0, dust_pm = 0, microdust_am = 0, microdust_pm = 0;
		String date="";

		return MicrodustDayResDto.builder()
			.dust_am(dust_am)
			.dust_pm(dust_pm)
			.microdust_am(microdust_am)
			.microdust_pm(microdust_pm)
			.date(date)
			.build();
	}
}
