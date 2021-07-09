package com.seeme.service;

import com.seeme.domain.CovidNationalResDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Service
@AllArgsConstructor
public class CovidService {

    public CovidNationalResDto getMain() throws ParserConfigurationException, SAXException, IOException {
        String location = getMainLocation();

        return CovidNationalResDto.builder()
                .location(location)
    }

    private String getMainLocation() {
        return "남양주";
    }

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH");
}
