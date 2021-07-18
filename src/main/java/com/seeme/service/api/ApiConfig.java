package com.seeme.service.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties
public class ApiConfig {
	private String covidMainKey;
	private String covidMainUrl;
	private String locationUrl;
	private String locationKey;
	private String convertWGS84ToTMAuthUrl;
	private String convertWGS84ToTMAuthId;
	private String convertWGS84ToTMAuthKey;
	private String convertWGS84ToTMUrl;
	private String microdustStationUrl;
	private String microdustMainUrl;
	private String microdustMainKey;
	private String specificAddressUrl;
	private String specificAddressKey;
	private String TMAddressUrl;
	private String TMAddressKey;
	private String microdustDayUrl;
	private String microdustDayKey;
	private String microdustTimeUrl;
	private String microdustTimeKey;
}
