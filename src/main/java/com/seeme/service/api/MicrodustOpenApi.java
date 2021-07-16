package com.seeme.service.api;

import com.seeme.domain.microdust.Microdust;
import com.seeme.domain.microdust.MicrodustTimeDto;
import com.seeme.util.JSONParsingUtil;
import com.seeme.util.MicrodustUtil;
import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MicrodustOpenApi {

	private final ApiConfig apiConfig;
	private final LocationApi locationApi;

	public Microdust getMainApi(List<String> stationList) throws IOException, ParseException {
		int index = 0, pm10 = -1, pm25 = -1, pmGrade = -1;
		boolean pm10Flag = false, pm25Flag = false;
		while (index++ < 3) {
			if (pm10Flag && pm25Flag)
				break;
			JSONObject microdust = getMicrodust(stationList, index);
			if (!pm10Flag && !microdust.get("pm10Value").equals("-")) {
				pm10Flag = true;
				pm10 = Integer.parseInt(microdust.get("pm10Value").toString());
				pmGrade = Integer.parseInt(microdust.get("pm10Grade").toString());
			}
			if (!pm25Flag && !microdust.get("pm25Value").equals("-")) {
				pm25Flag = true;
				pm25 = Integer.parseInt(microdust.get("pm25Value").toString());
				pmGrade = Math.max(pmGrade, Integer.parseInt(microdust.get("pm25Grade").toString()));
			}
		}

		return Microdust.builder()
			.pm10Value(pm10)
			.pm25Value(pm25)
			.pmGrade(pmGrade)
			.build();
	}

	private JSONObject getMicrodust(List<String> stationList, int index) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustMainUrl())
			.queryParam(MicrodustUtil.SERVICE_KEY, apiConfig.getMicrodustMainKey())
			.queryParam(MicrodustUtil.RETURN_TYPE, "json")
			.queryParam(MicrodustUtil.NUM_OF_ROWS, 1)
			.queryParam(MicrodustUtil.PAGE_NO, 1)
			.queryParam(MicrodustUtil.STATION_NAME, URLEncoder.encode(stationList.get(index), StandardCharsets.UTF_8))
			.queryParam(MicrodustUtil.DATA_TERM, "DAILY")
			.queryParam(MicrodustUtil.VERSION, 1.0);
		System.out.println(uriComponentsBuilder.build());

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
		JSONObject responseObject = (JSONObject) jsonObject.get("response");
		JSONObject bodyObject = (JSONObject) responseObject.get("body");
		JSONArray itemsObjects = (JSONArray) bodyObject.get("items");
		return (JSONObject) itemsObjects.get(0);
	}

	public List<MicrodustTimeDto> getTimeApi(List<String> measuringStation) throws IOException, ParserConfigurationException, SAXException {
		List<MicrodustTimeDto> microdustTimeDtoList = new ArrayList<>();
		return microdustTimeDtoList;
	}

	private String getTagValue(String tag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
		Node nValue = nlList.item(0);
		if (nValue == null)
			return null;
		return nValue.getNodeValue();
	}

	public List<String> getStationList(Double lat, Double lon) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustStationUrl())
			.query(locationApi.covertWGS84ToTM(lat, lon))
			.queryParam(MicrodustUtil.SERVICE_KEY, apiConfig.getMicrodustMainKey())
			.queryParam(MicrodustUtil.RETURN_TYPE, "json")
			.queryParam(MicrodustUtil.VERSION, "1.0");
		System.out.println(uriComponentsBuilder.build());

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
		JSONObject responseObject = (JSONObject) jsonObject.get("response");
		JSONObject bodyObject = (JSONObject) responseObject.get("body");
		JSONArray itemsObject = (JSONArray) bodyObject.get("items");

		int total = 3;
		List<String> stationList = new ArrayList<>();
		System.out.print("stationName: "); // remove
		while (total-- > 0) {
			JSONObject itemObject = (JSONObject) itemsObject.get(0);
			stationList.add(itemObject.get("stationName").toString());
			System.out.print(itemObject.get("stationName").toString() + " "); // remove
		}
		System.out.println(); // remove
		return stationList;
	}

	public List<String> getStationListByTM(String tmX, String tmY) throws IOException, ParseException {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
			.fromUriString(apiConfig.getMicrodustStationUrl())
			.queryParam(MicrodustUtil.SERVICE_KEY, apiConfig.getMicrodustMainKey())
			.queryParam(MicrodustUtil.RETURN_TYPE, "json")
			.queryParam(MicrodustUtil.TM_X, tmX)
			.queryParam(MicrodustUtil.TM_Y, tmY)
			.queryParam(MicrodustUtil.VERSION, "1.0");
		System.out.println(uriComponentsBuilder.build());

		StringBuilder sb = JSONParsingUtil.convertJSONToSB(uriComponentsBuilder);

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
		JSONObject responseObject = (JSONObject) jsonObject.get("response");
		JSONObject bodyObject = (JSONObject) responseObject.get("body");
		JSONArray itemsObject = (JSONArray) bodyObject.get("items");

		int index = 0;
		List<String> stationList = new ArrayList<>();
		System.out.print("stationName: "); // remove
		while (index++ < 3) {
			JSONObject itemObject = (JSONObject) itemsObject.get(index);
			stationList.add(itemObject.get("stationName").toString());
			System.out.print(itemObject.get("stationName").toString() + " "); // remove
		}
		System.out.println(); // remove
		return stationList;
	}
}
