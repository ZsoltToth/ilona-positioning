package uni.miskolc.ips.ilona.positioning.service.impl.neuralnetwork;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uni.miskolc.ips.ilona.measurement.model.measurement.BluetoothTags;
import uni.miskolc.ips.ilona.measurement.model.measurement.Magnetometer;
import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.measurement.MeasurementBuilder;
import uni.miskolc.ips.ilona.measurement.model.measurement.RFIDTags;
import uni.miskolc.ips.ilona.measurement.model.measurement.WiFiRSSI;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.positioning.service.gateway.ZoneGateway;
import uni.miskolc.ips.ilona.positioning.model.MeasurementToInstanceConverter;
import uni.miskolc.ips.ilona.positioning.model.neuralnetwork.NeuralNetwork;
import weka.core.Instance;

public class NeuralNetworkTest {
	static Zone zone1, zone2, zone3, zone4, zone5, zone6, zone7, zone8, zone9, zone10, zone11, zone12, zone13, zone14,
			zone15, zone16, zone17, zone18, zone19, zone20, zone21;
	ZoneGateway zoneGateway;
	String bluetoothTrainingSetPath = "src/resources/bluetoothtrainingset.arff";
	String magnetometerTrainingSetPath = "src/resources/magnetometertrainingset.arff";
	String wifiTrainingSetPath = "src/resources/wifitrainingset.arff";
	String trainingSetPath = "src/resources/trainingset.arff";

	@Test
	public void determinePositionTest() throws FileNotFoundException, IOException, Exception {
		NeuralNetwork neuralnetwork = new NeuralNetwork(0.6, 0.7, 140, "13", trainingSetPath);
		String serializedPath = "src/resources/neuralnetwork2.ser";
		NeuralNetwork.serializeNeuralNetwork(neuralnetwork, serializedPath);
		NeuralNetworkPositioning neuralNetworkPositioning = new NeuralNetworkPositioning(zoneGateway, serializedPath);
		Measurement measurement = instanceFromJSON();
		Position result = neuralNetworkPositioning.determinePosition(measurement);
		Assert.assertNotNull(result);
	}

	@Test
	public void deserializeNeuralNetworkIsTheSame() throws FileNotFoundException, IOException, Exception {
		NeuralNetwork expected = new NeuralNetwork(0.6, 0.7, 140, "13", trainingSetPath);
		String serializedPath = "src/resources/neuralnetworkExample.ser";
		NeuralNetwork.serializeNeuralNetwork(expected, serializedPath);
		NeuralNetwork actual = NeuralNetwork.deserialization(serializedPath);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void deserializedNeuralNetworkGivesTheSameResult() throws FileNotFoundException, IOException, Exception {
		NeuralNetwork neuralnetwork = new NeuralNetwork(0.6, 0.7, 140, "13", trainingSetPath);
		String serializedPath = "src/resources/neuralnetwork.ser";
		NeuralNetwork.serializeNeuralNetwork(neuralnetwork, serializedPath);
		NeuralNetworkPositioning neuralNetworkPositioning = new NeuralNetworkPositioning(zoneGateway, serializedPath);
		Measurement measurement = instanceFromJSON();
		Instance instance = MeasurementToInstanceConverter.convertMeasurementToInstance(measurement, neuralnetwork.getHeader());
		double cls = neuralnetwork.getMultilayerPerceptron().classifyInstance(instance);
		Zone zoneresult = zoneGateway.getZoneById(UUID.fromString(instance.classAttribute().value((int) cls)));
		Position expected = new Position(zoneresult);
		Position actual = neuralNetworkPositioning.determinePosition(measurement);
		Assert.assertEquals(expected.getZone().getId(), actual.getZone().getId());
	}

	@Test
	public void determinePositionForNullSensorsTest() throws FileNotFoundException, IOException, Exception {
		NeuralNetwork magnetoNeuralNetwork = new NeuralNetwork(0.6, 0.7, 140, "13", magnetometerTrainingSetPath);
		NeuralNetwork bluetoothneuralnetwork = new NeuralNetwork(0.6, 0.7, 140, "13", bluetoothTrainingSetPath);
		NeuralNetwork wifineuralnetwork = new NeuralNetwork(0.6, 0.7, 140, "13", "src/resources/wifitrainingset.arff");
		String magnetoserializedPath = "src/resources/Mneuralnetwork.ser";
		String bluetoothserializedPath = "src/resources/BTneuralnetwork.ser";
		String wifiserializedPath = "src/resources/Wneuralnetwork.ser";

		NeuralNetwork.serializeNeuralNetwork(magnetoNeuralNetwork, magnetoserializedPath);
		NeuralNetwork.serializeNeuralNetwork(bluetoothneuralnetwork, bluetoothserializedPath);
		NeuralNetwork.serializeNeuralNetwork(wifineuralnetwork, wifiserializedPath);

		Measurement measurement = instanceFromJSON();
		NeuralNetworkPositioningOverSensors positioningOverSensors = new NeuralNetworkPositioningOverSensors(
                zoneGateway, 0.6, 0.3, 0.5, bluetoothserializedPath, magnetoserializedPath, wifiserializedPath);
		Position result = positioningOverSensors.determinePosition(measurement);
		Assert.assertEquals(true, zoneGateway.listZones().contains(result.getZone()));

	}

	@Ignore
	public void createSensorNeuralNetwork() throws FileNotFoundException, IOException, Exception {
		NeuralNetwork magnetoNeuralNetwork = new NeuralNetwork(0.6, 0.7, 140, "13",
				"/home/ilona/probaworkspace/neuralnetwork/magnetometertrainingset.arff");
		NeuralNetwork bluetoothneuralnetwork = new NeuralNetwork(0.6, 0.7, 140, "13",
				"/home/ilona/probaworkspace/neuralnetwork/bluetoothtrainingset.arff");
		NeuralNetwork wifineuralnetwork = new NeuralNetwork(0.6, 0.7, 140, "13",
				"/home/ilona/probaworkspace/neuralnetwork/wifitrainingset.arff");
		String magnetoserializedPath = "/home/ilona/probaworkspace/neuralnetwork/Mneuralnetwork.ser";
		String bluetoothserializedPath = "/home/ilona/probaworkspace/neuralnetwork/BTneuralnetwork.ser";
		String wifiserializedPath = "/home/ilona/probaworkspace/neuralnetwork/Wneuralnetwork.ser";
		NeuralNetwork.serializeNeuralNetwork(magnetoNeuralNetwork, magnetoserializedPath);
		NeuralNetwork.serializeNeuralNetwork(bluetoothneuralnetwork, bluetoothserializedPath);
		NeuralNetwork.serializeNeuralNetwork(wifineuralnetwork, wifiserializedPath);

	}

	@Ignore
	public void determinePositionOfJSONTest() throws FileNotFoundException, IOException, Exception {
		NeuralNetwork neuralnetwork = new NeuralNetwork(0.9, 0.7, 280, "18",
				"/home/ilona/probaworkspace/neuralnetwork/trainingset.txt");
		String serializedPath = "src/resources/neuralnetwork.ser";
		NeuralNetwork.serializeNeuralNetwork(neuralnetwork, serializedPath);
		NeuralNetworkPositioning neuralNetworkPositioning = new NeuralNetworkPositioning(zoneGateway, serializedPath);
		Measurement measurement = instanceFromJSON();
		Position result = neuralNetworkPositioning.determinePosition(measurement);
		Assert.assertNotNull(result);

	}

	@Ignore
	public void deserializeNeuralNetwork() throws JsonParseException, JsonMappingException, IOException {
		String serializedPath = "src/resources/Wneuralnetwork.ser";
		NeuralNetworkPositioning neuralNetworkPositioning = new NeuralNetworkPositioning(zoneGateway, serializedPath);
		Measurement measurement = instanceFromJSON();
	}

	@Test
	public void deserializeJustSerializedObject() throws FileNotFoundException, IOException, Exception {
		NeuralNetwork neuralnetwork = new NeuralNetwork(0.7, 0.6, 140, "13", trainingSetPath);
		String targetPath = "/tmp/Wneuralnetwork.ser";
		NeuralNetwork.serializeNeuralNetwork(neuralnetwork, targetPath);
		NeuralNetwork e = NeuralNetwork.deserialization(targetPath);

		System.out.println("Deserialized NeuralNetwork...");
		System.out.println("LR: " + e.getLearningRate());
		System.out.println("M: " + e.getMomentum());
		System.out.println("TT: " + e.getTrainingTime());
		System.out.println("HL: " + e.getHiddenLayers());

	}

	@Before
	public void mockingZoneService() {
		zone1 = new Zone("Lab101");
		zone1.setId(UUID.fromString("5e27bae6-076f-4e5d-acb0-11a2cc2b9e0d"));
		zone2 = new Zone("Lab102");
		zone2.setId(UUID.fromString("93f32509-0f74-4f2c-a45a-90858ca646d8"));
		zone3 = new Zone("Lab103");
		zone3.setId(UUID.fromString("8933b3c7-54ac-4fc3-a2e5-8c7fdc7bfae3"));
		zone4 = new Zone("1st Floor East Corridor");
		zone4.setId(UUID.fromString("07a25de0-a013-486d-9463-404a348e05ee"));
		zone5 = new Zone("2nd Floor East Corridor");
		zone5.setId(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56"));
		zone6 = new Zone("Ground Floor East Corridor");
		zone6.setId(UUID.fromString("1501dc2f-55e3-44bd-8f15-8c26a8c7410d"));
		zone7 = new Zone("Lab 104");
		zone7.setId(UUID.fromString("43f5e995-b6f9-4e6f-b40a-9f6bca793ecd"));
		zone8 = new Zone("1st Floor West Corridor");
		zone8.setId(UUID.fromString("544bbbff-2c9c-4d7f-aad5-1742e2f26935"));
		zone9 = new Zone("Lab 115");
		zone9.setId(UUID.fromString("59376e57-9d03-4b9d-af9b-36d6f617a935"));
		zone10 = new Zone("1st Floor North Corridor");
		zone10.setId(UUID.fromString("5eed44d0-c401-46a0-935c-b3161177a00f"));
		zone11 = new Zone("2nd Floor North Corridor");
		zone11.setId(UUID.fromString("79d7a2dc-831e-4d07-98d7-327dbbad3884"));
		zone12 = new Zone("Ground Floor North Corridor");
		zone12.setId(UUID.fromString("7b8681b3-9d64-4b7e-918d-074bd146d9e6"));
		zone13 = new Zone("Ground Floor Lobby");
		zone13.setId(UUID.fromString("8e4181f2-8e1c-467d-8cc2-4580bf5cb76c"));
		zone14 = new Zone("Lab 106");
		zone14.setId(UUID.fromString("a377b162-49fb-4140-bfe1-2565a2260764"));
		zone15 = new Zone("Lecture Hall 205");
		zone15.setId(UUID.fromString("aa7b48e2-ab67-4888-b5c9-1edc21c13d63"));
		zone16 = new Zone("2nd Floor Lobby");
		zone16.setId(UUID.fromString("b33055ec-4fda-4cf1-ae44-b3fd54f94467"));
		zone17 = new Zone("Office 107b");
		zone17.setId(UUID.fromString("bef00f27-9e13-4416-a380-c04010c377cb"));
		zone18 = new Zone("Ground Floor West Corridor");
		zone18.setId(UUID.fromString("e2d1a25f-5496-49c6-85b7-ccb3601a9971"));
		zone19 = new Zone("1st Floor Lobby");
		zone19.setId(UUID.fromString("e48c8f34-716c-477f-a448-9c209d635fbf"));
		zone20 = new Zone("2nd Floor West Corridor");
		zone20.setId(UUID.fromString("f44d88f6-8067-4934-ac94-c38bfdc8bb7f"));
		zone21 = new Zone("Lecture Hall XXVI");
		zone21.setId(UUID.fromString("fff52967-5b13-4935-afa0-44c375cb84db"));

		Collection<Zone> zones = new ArrayList<Zone>() {
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
			}
		};

        zoneGateway = EasyMock.createMock(ZoneGateway.class);
		EasyMock.expect(zoneGateway.getZoneById(zone1.getId())).andReturn(zone1).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone2.getId())).andReturn(zone2).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone3.getId())).andReturn(zone3).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone4.getId())).andReturn(zone4).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone5.getId())).andReturn(zone5).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone6.getId())).andReturn(zone6).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone7.getId())).andReturn(zone7).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone8.getId())).andReturn(zone8).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone9.getId())).andReturn(zone9).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone10.getId())).andReturn(zone10).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone11.getId())).andReturn(zone11).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone12.getId())).andReturn(zone12).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone13.getId())).andReturn(zone13).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone14.getId())).andReturn(zone14).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone15.getId())).andReturn(zone15).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone16.getId())).andReturn(zone16).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone17.getId())).andReturn(zone17).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone18.getId())).andReturn(zone18).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone19.getId())).andReturn(zone19).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone20.getId())).andReturn(zone20).anyTimes();
		EasyMock.expect(zoneGateway.getZoneById(zone21.getId())).andReturn(zone21).anyTimes();
		EasyMock.expect(zoneGateway.listZones()).andReturn(zones);
		EasyMock.replay(zoneGateway);
	}

	private Measurement createMeasurement() {
		MeasurementBuilder measbuilder = new MeasurementBuilder();

		BluetoothTags bluetooth = new BluetoothTags(new HashSet<String>(
				Arrays.asList(new String[] { "00:16:53:4c:fa:67", "00:16:53:4c:f5:2d", "00:10:60:AA:36:F2" })));
		Magnetometer magneto = new Magnetometer(12, 32, 23, 0.5);
		RFIDTags rfid = new RFIDTags(new HashSet<byte[]>());
		rfid.addTag(new byte[] { (byte) 12 });
		WiFiRSSI wifi = new WiFiRSSI();
		wifi.setRSSI("ait-l15", -0.4);
		wifi.setRSSI("n", -1.2);
		wifi.setRSSI("bosch_telemetry", -3.2);
		measbuilder.setbluetoothTags(bluetooth);
		measbuilder.setMagnetometer(magneto);
		measbuilder.setRFIDTags(rfid);
		measbuilder.setWifiRSSI(wifi);
		Measurement result = measbuilder.build();
		return result;
	}

	private Measurement instanceFromJSON() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = "{\r\n \"id\":\"7f210fdb-7018-454c-978b-9a595ee39130\",\r\n   \"timestamp\":1456588248000,\r\n"
				+ "\"position\":{\r\n\"coordinate\":{\r\n\"x\":23.0,\r\n\"y\":8.0,\r\n\"z\":4.4\r\n  "
				+"},\r\n      \"zone\":{\r\n\"id\":\"07a25de0-a013-486d-9463-404a348e05ee\",\r\n        "
				+"\"name\":\"1st Floor East Corridor\"\r\n},\r\n\"uuid\":\"11a89987-bc93-411b-9d75-af3c5a7312da\"\r\n},\r\n  "
				+"\"wifiRSSI\":{\r\n\"rssiValues\":{\r\n\"dd\":-81.0,\r\n\"aut-sams-1\":-79.0,\r\n       "
				+"\"doa208\":-75.0,\r\n\"TP-LINK_B2765A\":-72.0,\r\n\"IITAP3\":-86.0,\r\n\"doa200\":-85.0,\r\n\"GEIAKFSZ\""
				+":-79.0,\r\n\"IITAP2\":-82.0,\r\n\"IITAP2-GUEST\":-81.0,\r\n\"KRZ\":-85.0,\r\n\"library114\":-86.0,\r\n\"IITAP1\":-74.0,\r\n "
				+"\"IITAP3-GUEST\":-86.0,\r\n\"109\":-65.0,\r\n\"IITAP1-GUEST\":-82.0\r\n}\r\n},\r\n \"magnetometer\":{\r\n "
				+"\"xAxis\":-0.5067219138145447,\r\n\"yAxis\":-0.964530348777771,\r\n\"zAxis\":0.055498506873846054,\r\n\"radian\":0.0\r\n},"
				+"\r\n\"bluetoothTags\":{\r\n\"tags\":[\r\n\"EV3 00:16:53:4C:FA:60\",\r\n\"IZE 00:16:53:4C:B1:F9\",\r\n"
				+"\"EV3BD 00:16:53:4C:F5:2D\",\r\n\"EV3 00:16:53:4C:FA:67\",\r\n\"JOE 00:16:53:4C:F9:A4\",\r\n\"DANI 6B:C2:26:12:62:60\",\r\n"
				+"\"EV3 00:16:53:4C:F2:6A\",\r\n\"MrEv3 00:16:53:4C:B4:EB\"\r\n]\r\n},\r\n\"gpsCoordinates\":null,\r\n\"rfidtags\":{\r\n"
				+"\"tags\":[\r\n\r\n]\r\n}\r\n}";
		Measurement result = mapper.readValue(json, Measurement.class);
		if (result.getPosition() == null) {
			result.setPosition(new Position(Zone.UNKNOWN_POSITION));
		}
		return result;

	}

}
