package com.seeme.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter @Getter
@Component
@ConfigurationProperties
public class ApiConfig {
	private String covidMainServiceKey;
	private String covidMainUrl;
}
