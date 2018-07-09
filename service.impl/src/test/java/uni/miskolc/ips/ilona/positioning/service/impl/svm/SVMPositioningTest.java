package uni.miskolc.ips.ilona.positioning.service.impl.svm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uni.miskolc.ips.ilona.measurement.controller.dto.ZoneDTO;
import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.positioning.model.svm.SupportVectorMachine;
import uni.miskolc.ips.ilona.positioning.service.gateway.ZoneQueryService;

public class SVMPositioningTest {

	static ZoneDTO zone1, zone2, zone3, zone4, zone5, zone6, zone7, zone8, zone9, zone10, zone11, zone12, zone13, zone14,
			zone15, zone16, zone17, zone18, zone19, zone20, zone21, zone22;
	ZoneQueryService zoneGateway;

	@Test
	public void determinePositionOfJSONTest() throws FileNotFoundException, IOException, Exception {
		SupportVectorMachine svm = new SupportVectorMachine("src/resources/training_set.arff");
		String serializedPath = "src/resources/svm.ser";
		SupportVectorMachine.serializeSupportVectorMachine(svm, serializedPath);
		SVMPositioning svmPositioning = new SVMPositioning(serializedPath, zoneGateway);
		System.out.println(svm.getHeader());
		Measurement measurement = instanceFromJSON();
		Position result = svmPositioning.determinePosition(measurement);
		Assert.assertNotNull(result);

	}


	private Measurement instanceFromJSON() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = "{\r\n \"id\":\"7f210fdb-7018-454c-978b-9a595ee39130\",\r\n   \"timestamp\":1456588248000,\r\n"
				+ "\"position\":{\r\n\"coordinate\":{\r\n\"x\":23.0,\r\n\"y\":8.0,\r\n\"z\":4.4\r\n  "
				+ "},\r\n      \"zone\":{\r\n\"id\":\"07a25de0-a013-486d-9463-404a348e05ee\",\r\n        "
				+ "\"name\":\"1st Floor East Corridor\"\r\n},\r\n\"uuid\":\"11a89987-bc93-411b-9d75-af3c5a7312da\"\r\n},\r\n  "
				+ "\"wifiRSSI\":{\r\n\"rssiValues\":{\r\n\"dd\":-81.0,\r\n\"aut-sams-1\":-79.0,\r\n       "
				+ "\"doa208\":-75.0,\r\n\"TP-LINK_B2765A\":-72.0,\r\n\"IITAP3\":-86.0,\r\n\"doa200\":-85.0,\r\n\"GEIAKFSZ\""
				+ ":-79.0,\r\n\"IITAP2\":-82.0,\r\n\"IITAP2-GUEST\":-81.0,\r\n\"KRZ\":-85.0,\r\n\"library114\":-86.0,\r\n\"IITAP1\":-74.0,\r\n "
				+ "\"IITAP3-GUEST\":-86.0,\r\n\"109\":-65.0,\r\n\"IITAP1-GUEST\":-82.0\r\n}\r\n},\r\n \"magnetometer\":{\r\n "
				+ "\"xAxis\":-0.5067219138145447,\r\n\"yAxis\":-0.964530348777771,\r\n\"zAxis\":0.055498506873846054,\r\n\"radian\":0.0\r\n},"
				+ "\r\n\"bluetoothTags\":{\r\n\"tags\":[\r\n\"EV3 00:16:53:4C:FA:60\",\r\n\"IZE 00:16:53:4C:B1:F9\",\r\n"
				+ "\"EV3BD 00:16:53:4C:F5:2D\",\r\n\"EV3 00:16:53:4C:FA:67\",\r\n\"JOE 00:16:53:4C:F9:A4\",\r\n\"DANI 6B:C2:26:12:62:60\",\r\n"
				+ "\"EV3 00:16:53:4C:F2:6A\",\r\n\"MrEv3 00:16:53:4C:B4:EB\"\r\n]\r\n},\r\n\"gpsCoordinates\":null,\r\n\"rfidtags\":{\r\n"
				+ "\"tags\":[\r\n\r\n]\r\n}\r\n}";
		Measurement result = mapper.readValue(json, Measurement.class);
		if (result.getPosition() == null) {
			result.setPosition(new Position(Zone.UNKNOWN_POSITION));
		}
		return result;

	}

	@Before
	public void mockingZoneGateway()  {
		zone1 = new ZoneDTO();
        zone1.setName("Lab101");
		zone1.setId("5e27bae6-076f-4e5d-acb0-11a2cc2b9e0d");
		zone2 = new ZoneDTO();
		zone2.setName("Lab102");
		zone2.setId("93f32509-0f74-4f2c-a45a-90858ca646d8");
		zone3 = new ZoneDTO();
		zone3.setName("Lab103");
		zone3.setId("8933b3c7-54ac-4fc3-a2e5-8c7fdc7bfae3");
		zone4 = new ZoneDTO();
		zone4.setName("1st Floor East Corridor");
		zone4.setId("07a25de0-a013-486d-9463-404a348e05ee");
		zone5 = new ZoneDTO();
		zone5.setName("2nd Floor East Corridor");
		zone5.setId("14fc835a-ee28-4b78-9c59-9ee0f759ce56");
		zone6 = new ZoneDTO();
		zone6.setName("Ground Floor East Corridor");
		zone6.setId("1501dc2f-55e3-44bd-8f15-8c26a8c7410d");
		zone7 = new ZoneDTO();
		zone7.setName("Lab 104");
		zone7.setId("43f5e995-b6f9-4e6f-b40a-9f6bca793ecd");
		zone8 = new ZoneDTO();
		zone8.setName("1st Floor West Corridor");
		zone8.setId("544bbbff-2c9c-4d7f-aad5-1742e2f26935");
		zone9 = new ZoneDTO();
		zone9.setName("Lab 115");
		zone9.setId("59376e57-9d03-4b9d-af9b-36d6f617a935");
		zone10 = new ZoneDTO();
		zone10.setName("1st Floor North Corridor");
		zone10.setId("5eed44d0-c401-46a0-935c-b3161177a00f");
		zone11 = new ZoneDTO();
		zone11.setName("2nd Floor North Corridor");
		zone11.setId("79d7a2dc-831e-4d07-98d7-327dbbad3884");
		zone12 = new ZoneDTO();
		zone12.setName("Ground Floor North Corridor");
		zone12.setId("7b8681b3-9d64-4b7e-918d-074bd146d9e6");
		zone13 = new ZoneDTO();
		zone13.setName("Ground Floor Lobby");
		zone13.setId("8e4181f2-8e1c-467d-8cc2-4580bf5cb76c");
		zone14 = new ZoneDTO();
		zone14.setName("Lab 106");
		zone14.setId("a377b162-49fb-4140-bfe1-2565a2260764");
		zone15 = new ZoneDTO();
		zone15.setName("Lecture Hall 205");
		zone15.setId("aa7b48e2-ab67-4888-b5c9-1edc21c13d63");
		zone16 = new ZoneDTO();
		zone16.setName("2nd Floor Lobby");
		zone16.setId("b33055ec-4fda-4cf1-ae44-b3fd54f94467");
		zone17 = new ZoneDTO();
		zone17.setName("Office 107b");
		zone17.setId("bef00f27-9e13-4416-a380-c04010c377cb");
		zone18 = new ZoneDTO();
		zone18.setName("Ground Floor West Corridor");
		zone18.setId("e2d1a25f-5496-49c6-85b7-ccb3601a9971");
		zone19 = new ZoneDTO();
		zone19.setName("1st Floor Lobby");
		zone19.setId("e48c8f34-716c-477f-a448-9c209d635fbf");
		zone20 = new ZoneDTO();
		zone20.setName("2nd Floor West Corridor");
		zone20.setId("f44d88f6-8067-4934-ac94-c38bfdc8bb7f");
		zone21 = new ZoneDTO();
		zone21.setName("Lecture Hall XXVI");
		zone21.setId("fff52967-5b13-4935-afa0-44c375cb84db");
		zone22 = new ZoneDTO();
		zone22.setName("1st Floor Kitchen");
		zone22.setId("12d4a4a3-b9b9-4ad8-99d2-c64ec3a2de0f");

		Collection<ZoneDTO> zones = new ArrayList<ZoneDTO>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -4409254956643080632L;

			{
				add(zone1);
				add(zone2);
				add(zone3);
				add(zone4);
				add(zone5);
				add(zone6);
				add(zone7);
				add(zone8);
				add(zone9);
				add(zone10);
				add(zone11);
				add(zone12);
				add(zone13);
				add(zone14);
				add(zone15);
				add(zone16);
				add(zone17);
				add(zone18);
				add(zone19);
				add(zone20);
				add(zone21);
				add(zone22);
			}
		};

		zoneGateway = EasyMock.createMock(ZoneQueryService.class);
		EasyMock.expect(zoneGateway.listZones()).andReturn(zones).anyTimes();
		EasyMock.replay(zoneGateway);
	}

}
