package uni.miskolc.ips.ilona.positioning.service.impl.knn;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import org.easymock.EasyMock;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import uni.miskolc.ips.ilona.measurement.model.measurement.BluetoothTags;
import uni.miskolc.ips.ilona.measurement.model.measurement.Magnetometer;
import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.measurement.MeasurementBuilder;
import uni.miskolc.ips.ilona.measurement.model.measurement.MeasurementDistanceCalculator;
import uni.miskolc.ips.ilona.measurement.model.measurement.RFIDTags;
import uni.miskolc.ips.ilona.measurement.model.measurement.WiFiRSSI;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.service.MeasurementService;
import uni.miskolc.ips.ilona.measurement.service.exception.DatabaseUnavailableException;
import uni.miskolc.ips.ilona.positioning.service.impl.knn.Neighbour;

public class kNNTest {
	private ArrayList<Measurement> measurementsList;
	private Measurement incomingMeasurement;
	private MeasurementService measurementService;
	private MeasurementDistanceCalculator distanceCalculator;
	private int k;
	private KNNSimplePositioning simplePositioning;
	private Zone z1, z2, z3, z4;

	@Before
	public void setUp() throws DatabaseUnavailableException {
		setUpZones();
		setUpMeasurements();
		k = 3;

		distanceCalculator = EasyMock.createMock(MeasurementDistanceCalculator.class);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(0), incomingMeasurement)).andReturn(0.8);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(1), incomingMeasurement)).andReturn(1.6);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(2), incomingMeasurement)).andReturn(1.6);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(3), incomingMeasurement)).andReturn(0.8);
		EasyMock.replay(distanceCalculator);

	}
	
	
	
	
	@Test
	public void emptyMeasurementsListTest() throws DatabaseUnavailableException {
		measurementService = EasyMock.createMock(MeasurementService.class);
		EasyMock.expect(measurementService.readMeasurements()).andReturn(new ArrayList<Measurement>());
		EasyMock.replay(measurementService);

		distanceCalculator = EasyMock.createMock(MeasurementDistanceCalculator.class);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(0), incomingMeasurement)).andReturn(1.6);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(1), incomingMeasurement)).andReturn(1.6);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(2), incomingMeasurement)).andReturn(0.1);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(3), incomingMeasurement)).andReturn(1.8);
		EasyMock.replay(distanceCalculator);

		simplePositioning = new KNNSimplePositioning(distanceCalculator, measurementService, k);
		Position actual = simplePositioning.determinePosition(incomingMeasurement);
		Position expected = new Position(Zone.UNKNOWN_POSITION);
		assertThat(actual, fromSameZone(expected));

	}

	@Test
	public void majorVote() throws DatabaseUnavailableException {
		mockingMeasurementService();

		distanceCalculator = EasyMock.createMock(MeasurementDistanceCalculator.class);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(0), incomingMeasurement)).andReturn(1.6);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(1), incomingMeasurement)).andReturn(1.6);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(2), incomingMeasurement)).andReturn(0.1);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(3), incomingMeasurement)).andReturn(1.8);
		EasyMock.replay(distanceCalculator);

		simplePositioning = new KNNSimplePositioning(distanceCalculator, measurementService, k);
		Position actual = simplePositioning.determinePosition(incomingMeasurement);
		Position expected = new Position(z2);
		assertThat(actual, fromSameZone(expected));

	}

	@Test(expected = IllegalArgumentException.class)
	public void negativeKTest() throws DatabaseUnavailableException {
		mockingMeasurementService();
		distanceCalculator = EasyMock.createMock(MeasurementDistanceCalculator.class);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(0), incomingMeasurement)).andReturn(1.6);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(1), incomingMeasurement)).andReturn(1.6);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(2), incomingMeasurement)).andReturn(0.1);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(3), incomingMeasurement)).andReturn(1.8);
		EasyMock.replay(distanceCalculator);
		k = -3;
		simplePositioning = new KNNSimplePositioning(distanceCalculator, measurementService, k);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tooHighKTest() throws DatabaseUnavailableException {
		mockingMeasurementService();
		distanceCalculator = EasyMock.createMock(MeasurementDistanceCalculator.class);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(0), incomingMeasurement)).andReturn(1.6);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(1), incomingMeasurement)).andReturn(1.6);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(2), incomingMeasurement)).andReturn(0.1);
		EasyMock.expect(distanceCalculator.distance(measurementsList.get(3), incomingMeasurement)).andReturn(1.8);
		EasyMock.replay(distanceCalculator);
		k = 100;
		simplePositioning = new KNNSimplePositioning(distanceCalculator, measurementService, k);
		simplePositioning.determinePosition(incomingMeasurement);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullArgumentTest() throws DatabaseUnavailableException {
		mockingMeasurementService();
		simplePositioning = new KNNSimplePositioning(null, measurementService, k);
	}

	private void mockingMeasurementService() throws DatabaseUnavailableException {
		measurementService = EasyMock.createMock(MeasurementService.class);
		EasyMock.expect(measurementService.readMeasurements()).andReturn(measurementsList);
		EasyMock.replay(measurementService);

	}

	private void setUpMeasurements() {
		measurementsList = new ArrayList<Measurement>();
		MeasurementBuilder measbuilder = new MeasurementBuilder();
		/*
		 * 
		 */
		BluetoothTags bluetooth = new BluetoothTags(
				new HashSet<String>(Arrays.asList(new String[] { "001060AA36F8", "001060AA36F4", "001060AA36F2" })));
		Magnetometer magneto = new Magnetometer(12, 32, 23, 0.5);
		RFIDTags rfid = new RFIDTags(new HashSet<byte[]>());
		rfid.addTag(new byte[] { (byte) 12 });
		WiFiRSSI wifi = new WiFiRSSI();
		wifi.setRSSI("egy", -0.4);
		wifi.setRSSI("Ketto", -1.2);
		wifi.setRSSI("Harom", -3.2);
		measbuilder.setbluetoothTags(bluetooth);
		measbuilder.setMagnetometer(magneto);
		measbuilder.setRFIDTags(rfid);
		measbuilder.setWifiRSSI(wifi);
		measbuilder.setPosition(new Position(z2));
		measurementsList.add(measbuilder.build());
		/*
		 * 
		 */
		BluetoothTags bluetooth2 = new BluetoothTags(
				new HashSet<String>(Arrays.asList(new String[] { "001060AA36F8", "001060AA36F4", "001060AA36F2" })));
		Magnetometer magneto2 = new Magnetometer(12, 32, 23, 0.5);
		RFIDTags rfid2 = new RFIDTags(new HashSet<byte[]>());
		rfid2.addTag(new byte[] { (byte) 12, (byte) 46 });
		WiFiRSSI wifi2 = new WiFiRSSI();
		wifi2.setRSSI("egy", -0.4);
		wifi2.setRSSI("Ketto", -1.2);
		wifi2.setRSSI("Harom", -3.2);
		measbuilder.setbluetoothTags(bluetooth2);
		measbuilder.setMagnetometer(magneto2);
		measbuilder.setRFIDTags(rfid2);
		measbuilder.setWifiRSSI(wifi2);
		measbuilder.setPosition(new Position(z2));
		measurementsList.add(measbuilder.build());
		/*
		 * 
		 */
		BluetoothTags bluetooth3 = new BluetoothTags(
				new HashSet<String>(Arrays.asList(new String[] { "001060AA36F8", "001060AA36F4", "001060AA36F2" })));
		Magnetometer magneto3 = new Magnetometer(12, 32, 23, 0.5);
		RFIDTags rfid3 = new RFIDTags(new HashSet<byte[]>());
		rfid3.addTag(new byte[] { (byte) 12, (byte) 46 });
		WiFiRSSI wifi3 = new WiFiRSSI();
		wifi3.setRSSI("Egy", -0.4);
		wifi3.setRSSI("Ketto", -1.2);
		wifi3.setRSSI("Harom", -3.2);
		measbuilder.setbluetoothTags(bluetooth3);
		measbuilder.setMagnetometer(magneto3);
		measbuilder.setRFIDTags(rfid3);
		measbuilder.setWifiRSSI(wifi3);
		measbuilder.setPosition(new Position(z1));
		measurementsList.add(measbuilder.build());
		/*
		 * 
		 */
		BluetoothTags bluetooth4 = new BluetoothTags(
				new HashSet<String>(Arrays.asList(new String[] { "001060AA36F8", "001060AA36F4", "001060AA36F2" })));
		Magnetometer magneto4 = new Magnetometer(12, 32, 23, 0.5);
		RFIDTags rfid4 = new RFIDTags(new HashSet<byte[]>());
		rfid4.addTag(new byte[] { (byte) 12, (byte) 46 });
		WiFiRSSI wifi4 = new WiFiRSSI();
		wifi4.setRSSI("egy", -0.4);
		wifi4.setRSSI("Ketto", -1.2);
		wifi4.setRSSI("Harom", -3.2);
		measbuilder.setbluetoothTags(bluetooth4);
		measbuilder.setMagnetometer(magneto4);
		measbuilder.setRFIDTags(rfid4);
		measbuilder.setWifiRSSI(wifi4);
		measbuilder.setPosition(new Position(z2));
		measurementsList.add(measbuilder.build());
		/*
		 * 
		 */
		wifi4.setRSSI("Negy", -12.2);
		wifi4.setRSSI("Ot", -6.2);
		measbuilder.setWifiRSSI(wifi4);
		measbuilder.setPosition(new Position(z3));
		incomingMeasurement = measbuilder.build();

	}

	private void setUpZones() {
		z1 = new Zone("101");
		z2 = new Zone("102");
		z3 = new Zone("103");
	}

	private Matcher<Position> fromSameZone(final Position expected) {
		return new BaseMatcher<Position>() {
			public boolean matches(final Object actual) {
				final Position actualPosition = (Position) actual;
				return expected.getZone() == actualPosition.getZone();
			}

			public void describeTo(final Description description) {
				description.appendText("getNumber should return ").appendValue(expected);
			}
		};
	};

}
