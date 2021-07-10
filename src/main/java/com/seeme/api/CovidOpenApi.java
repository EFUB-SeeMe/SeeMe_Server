package com.seeme.api;

import com.seeme.domain.covid.CovidDto;
import com.seeme.util.CovidUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CovidOpenApi {

	private final ApiConfig apiConfig;

	public List<CovidDto> getMainApi() throws IOException, ParserConfigurationException, SAXException {

		String endCreateDt = CovidUtil.getCovidMainEndCreateDt();
		String startCreateDt = CovidUtil.getCovidMainCreateCreateDt();
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getCovidMainUrl())
			.queryParam(CovidUtil.SERVICE_KEY, apiConfig.getCovidMainKey())
			.queryParam(CovidUtil.START_CREATE_DT, startCreateDt)
			.queryParam(CovidUtil.END_CREATE_DT, endCreateDt);
		URL url = new URL(uriComponentsBuilder.build().toUriString());

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(String.valueOf(url));

		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("item");

		List<CovidDto> covidList = new ArrayList<>();
		for (int temp = 0; temp < 38; temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				covidList.add(
					CovidDto.builder()
						.stdDay(getTagValue("stdDay", eElement))
						.gubun(getTagValue("gubun", eElement))
						.incDec(getTagValue("incDec", eElement))
						.build()
				);
			}
		}
		return covidList;
	}

	private String getTagValue(String tag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
		Node nValue = nlList.item(0);
		if (nValue == null)
			return null;
		return nValue.getNodeValue();
	}
}