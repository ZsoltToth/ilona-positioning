package uni.miskolc.ips.ilona.positioning.service.impl.classification.bayes;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uni.miskolc.ips.ilona.measurement.model.measurement.*;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.positioning.exceptions.InvalidMeasurementException;
import uni.miskolc.ips.ilona.positioning.service.gateway.MeasurementGateway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertNotNull;

/**
 * Created by tamas on 2017.07.13..
 */
public class NaiveBayesTest {

    private MeasurementGateway measurementGateway;
    private MeasurementDistanceCalculator distanceCalculator;
    private ArrayList<Measurement> measurementsList;
    private Measurement incomingMeasurement;
    private Zone z1,z2,z3;
    private NaiveBayesPositioningService bayesPositioning;

    @Before
    public void setUp(){
        setUpZones();
        setUpMeasurements();


        distanceCalculator = EasyMock.createMock(MeasurementDistanceCalculator.class);
        EasyMock.expect(distanceCalculator.distance(measurementsList.get(0), incomingMeasurement)).andReturn(0.8).anyTimes();
        EasyMock.expect(distanceCalculator.distance(measurementsList.get(1), incomingMeasurement)).andReturn(1.6).anyTimes();
        EasyMock.expect(distanceCalculator.distance(measurementsList.get(2), incomingMeasurement)).andReturn(1.6).anyTimes();
        EasyMock.expect(distanceCalculator.distance(measurementsList.get(3), incomingMeasurement)).andReturn(0.8).anyTimes();
        EasyMock.replay(distanceCalculator);
    }

    @Test
    public void emptyMeasurementsListTest() throws InvalidMeasurementException {
        measurementGateway = EasyMock.createMock(MeasurementGateway.class);
        EasyMock.expect(measurementGateway.listMeasurements()).andReturn(new ArrayList<Measurement>());
        EasyMock.replay(measurementGateway);

        bayesPositioning = new NaiveBayesPositioningService(measurementGateway,distanceCalculator,100);
        Position actual = bayesPositioning.determinePosition(incomingMeasurement);
        Position expected = new Position(Zone.UNKNOWN_POSITION);
        Assert.assertEquals(actual.getZone(),expected.getZone());

    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeMaxDistanceTest() {
        mockingMeasurementGateway();
        bayesPositioning = new NaiveBayesPositioningService( measurementGateway, distanceCalculator,-100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullGatewayTest() {
        bayesPositioning = new NaiveBayesPositioningService( null, distanceCalculator,100);
    }


    @Test
    public void workingAsExpected() throws InvalidMeasurementException {
        mockingMeasurementGateway();
        bayesPositioning = new NaiveBayesPositioningService( measurementGateway, distanceCalculator,100);
        Position result =bayesPositioning.determinePosition(incomingMeasurement);
        assertNotNull(result);

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

    private void mockingMeasurementGateway() {
        measurementGateway = EasyMock.createMock(MeasurementGateway.class);
        EasyMock.expect(measurementGateway.listMeasurements()).andReturn(measurementsList);
        EasyMock.replay(measurementGateway);

    }


}
