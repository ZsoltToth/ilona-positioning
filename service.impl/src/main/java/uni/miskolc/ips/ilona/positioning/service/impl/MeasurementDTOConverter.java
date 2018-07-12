package uni.miskolc.ips.ilona.positioning.service.impl;

import uni.miskolc.ips.ilona.measurement.controller.dto.MeasurementDTO;
import uni.miskolc.ips.ilona.measurement.model.measurement.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

public class MeasurementDTOConverter {

    public static Collection<Measurement> convertToMeasurements(Collection<MeasurementDTO> measurementDTOS) {
        List<Measurement> measurements = new ArrayList<>();
        for (MeasurementDTO measurementDTO : measurementDTOS) {
            measurements.add(convertToMeasurement(measurementDTO));
        }
        return measurements;
    }

    public static Collection<MeasurementDTO> convertToMeasurementDTOs(Collection<Measurement> measurements) throws DatatypeConfigurationException {
        List<MeasurementDTO> measurementDTOs = new ArrayList<>();
        for (Measurement measurement : measurements) {
            measurementDTOs.add(convertToMeasurementDTO(measurement));
        }
        return measurementDTOs;
    }

    private static BluetoothTags convertToBluetoothTags(MeasurementDTO.BluetoothTags bluetoothTagsDTO) {
        BluetoothTags bluetoothTags = new BluetoothTags();
        for (String tag : bluetoothTagsDTO.getBluetoothTag()) {
            bluetoothTags.addTag(tag);
        }
        return bluetoothTags;
    }

    private static MeasurementDTO.BluetoothTags convertToBluetoothTags(BluetoothTags bluetoothTags) {

        MeasurementDTO.BluetoothTags bluetoothTagsDTO = new MeasurementDTO.BluetoothTags();
        for (String tag : bluetoothTags.getTags()) {
            bluetoothTagsDTO.getBluetoothTag().add(tag);
        }
        return bluetoothTagsDTO;
    }

    private static GPSCoordinate convertToGpsCoordinate(MeasurementDTO.GpsCoordinates gpsCoordinatesDTO) {
        GPSCoordinate gpsCoordinate = new GPSCoordinate();
        gpsCoordinate.setAltitude(gpsCoordinatesDTO.getAltitude());
        gpsCoordinate.setLatitude(gpsCoordinatesDTO.getLatitude());
        gpsCoordinate.setLongitude(gpsCoordinatesDTO.getLongitude());
        return gpsCoordinate;
    }

    private static MeasurementDTO.GpsCoordinates convertToGpsCoordinateDTO(GPSCoordinate gpsCoordinates) {
        MeasurementDTO.GpsCoordinates gpsCoordinateDTO = new MeasurementDTO.GpsCoordinates();
        gpsCoordinateDTO.setAltitude(gpsCoordinates.getAltitude());
        gpsCoordinateDTO.setLatitude(gpsCoordinates.getLatitude());
        gpsCoordinateDTO.setLongitude(gpsCoordinates.getLongitude());
        return gpsCoordinateDTO;
    }

    private static Magnetometer convertToMagnetometer(MeasurementDTO.Magnetometer magnetometerDTO) {
        Magnetometer magnetometer = new Magnetometer();
        magnetometer.setRadian(magnetometerDTO.getRadian());
        magnetometer.setxAxis(magnetometerDTO.getXAxis());
        magnetometer.setyAxis(magnetometerDTO.getYAxis());
        magnetometer.setzAxis(magnetometerDTO.getZAxis());
        return magnetometer;
    }

    private static MeasurementDTO.Magnetometer convertToMagnetometerDTO(Magnetometer magnetometer) {
        MeasurementDTO.Magnetometer magnetometerDTO = new MeasurementDTO.Magnetometer();
        magnetometerDTO.setRadian(magnetometer.getRadian());
        magnetometerDTO.setXAxis(magnetometer.getxAxis());
        magnetometerDTO.setYAxis(magnetometer.getyAxis());
        magnetometerDTO.setZAxis(magnetometer.getzAxis());
        return magnetometerDTO;
    }

    private static WiFiRSSI convertToWiFiRSSI(MeasurementDTO.WifiRSSI wifiRSSIDTO) {
        WiFiRSSI wiFiRSSI = new WiFiRSSI();
        for (MeasurementDTO.WifiRSSI.Ap ap : wifiRSSIDTO.getAp()) {
            wiFiRSSI.setRSSI(ap.getSsid(), ap.getValue());
        }
        return wiFiRSSI;
    }

    private static MeasurementDTO.WifiRSSI convertToWiFiRSSI(WiFiRSSI wifiRSSI) {
        MeasurementDTO.WifiRSSI wiFiRSSIDTO = new MeasurementDTO.WifiRSSI();
        for (Map.Entry<String, Double> rssi : wifiRSSI.getRssiValues().entrySet()) {

            MeasurementDTO.WifiRSSI.Ap ap = new MeasurementDTO.WifiRSSI.Ap();
            ap.setSsid(rssi.getKey());
            ap.setValue(rssi.getValue());
            wiFiRSSIDTO.getAp().add(ap);
        }
        return wiFiRSSIDTO;
    }

    private static MeasurementDTO.Rfidtags convertToRfidtags(RFIDTags rfidTags) {
        MeasurementDTO.Rfidtags rfidtagsDTO = new MeasurementDTO.Rfidtags();
        for (byte[] bytes : rfidTags.getTags()) {
            rfidtagsDTO.getRfidTag().add(bytes);
        }
        return rfidtagsDTO;
    }

    private static XMLGregorianCalendar convertToXMLXmlGregorianCalendar(Date date) throws DatatypeConfigurationException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    }


    public static Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        Measurement measurement = new Measurement();

        measurement.setId(UUID.fromString(measurementDTO.getId()));

        measurement.setBluetoothTags(convertToBluetoothTags(measurementDTO.getBluetoothTags()));

        measurement.setGpsCoordinates(convertToGpsCoordinate(measurementDTO.getGpsCoordinates()));

        measurement.setMagnetometer(convertToMagnetometer(measurementDTO.getMagnetometer()));

        measurement.setPosition(PositionDTOConverter.convertToPosition(measurementDTO.getPosition()));

        measurement.setRfidtags(new RFIDTags(new HashSet<byte[]>(measurementDTO.getRfidtags().getRfidTag())));

        measurement.setTimestamp(measurementDTO.getTimestamp().toGregorianCalendar().getTime());

        measurement.setWifiRSSI(convertToWiFiRSSI(measurementDTO.getWifiRSSI()));

        return measurement;
    }

    public static MeasurementDTO convertToMeasurementDTO(Measurement measurement) throws DatatypeConfigurationException {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setId(measurement.getId().toString());
        measurementDTO.setPosition(PositionDTOConverter.convertToPositionDTO(measurement.getPosition()));
        measurementDTO.setBluetoothTags(convertToBluetoothTags(measurement.getBluetoothTags()));
        measurementDTO.setGpsCoordinates(convertToGpsCoordinateDTO(measurement.getGpsCoordinates()));
        measurementDTO.setMagnetometer(convertToMagnetometerDTO(measurement.getMagnetometer()));
        measurementDTO.setRfidtags(convertToRfidtags(measurement.getRfidtags()));
        measurementDTO.setTimestamp(convertToXMLXmlGregorianCalendar(measurement.getTimestamp()));
        measurementDTO.setWifiRSSI(convertToWiFiRSSI(measurement.getWifiRSSI()));
        return measurementDTO;
    }
}