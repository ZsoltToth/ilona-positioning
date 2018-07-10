package uni.miskolc.ips.ilona.positioning.controller;

import uni.miskolc.ips.ilona.measurement.controller.dto.CoordinateDTO;
import uni.miskolc.ips.ilona.measurement.controller.dto.MeasurementDTO;
import uni.miskolc.ips.ilona.measurement.controller.dto.PositionDTO;
import uni.miskolc.ips.ilona.measurement.controller.dto.ZoneDTO;
import uni.miskolc.ips.ilona.measurement.model.measurement.*;
import uni.miskolc.ips.ilona.measurement.model.position.Coordinate;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;

import java.util.HashSet;
import java.util.UUID;

public class DTOConverter {

    public static PositionDTO convertToPositionDTO(Position position) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(position.getUUID().toString());
        ZoneDTO zoneDTO = new ZoneDTO();
        zoneDTO.setId(position.getZone().getId().toString());
        zoneDTO.setName(position.getZone().getName());
        positionDTO.setZone(zoneDTO);
        CoordinateDTO coordinateDTO = new CoordinateDTO();
        coordinateDTO.setX(position.getCoordinate().getX());
        coordinateDTO.setY(position.getCoordinate().getY());
        coordinateDTO.setZ(position.getCoordinate().getZ());
        positionDTO.setCoordinate(coordinateDTO);
        return positionDTO;
    }

    public static Position convertToPosition(PositionDTO positionDTO) {
        Position position = new Position();
        position.setUUID(UUID.fromString(positionDTO.getId()));
        Coordinate coordinate = new Coordinate();
        coordinate.setX(position.getCoordinate().getX());
        coordinate.setY(position.getCoordinate().getY());
        coordinate.setZ(position.getCoordinate().getZ());
        position.setCoordinate(coordinate);
        Zone zone = new Zone();
        zone.setId(UUID.fromString(positionDTO.getZone().getId()));
        zone.setName(positionDTO.getZone().getName());
        position.setZone(zone);
        return position;
    }

    public static BluetoothTags convertToBluetoothTags(MeasurementDTO.BluetoothTags bluetoothTagsDTO) {
        BluetoothTags bluetoothTags = new BluetoothTags();
        for (String tag : bluetoothTagsDTO.getBluetoothTag()) {
            bluetoothTags.addTag(tag);
        }
        return bluetoothTags;
    }

    public static GPSCoordinate convertToGpsCoordinate(MeasurementDTO.GpsCoordinates gpsCoordinatesDTO) {
        GPSCoordinate gpsCoordinate = new GPSCoordinate();
        gpsCoordinate.setAltitude(gpsCoordinatesDTO.getAltitude());
        gpsCoordinate.setLatitude(gpsCoordinatesDTO.getLatitude());
        gpsCoordinate.setLongitude(gpsCoordinatesDTO.getLongitude());
        return gpsCoordinate;
    }

    public static Magnetometer convertToMagnetometer(MeasurementDTO.Magnetometer magnetometerDTO) {
        Magnetometer magnetometer = new Magnetometer();
        magnetometer.setRadian(magnetometerDTO.getRadian());
        magnetometer.setxAxis(magnetometerDTO.getXAxis());
        magnetometer.setyAxis(magnetometerDTO.getYAxis());
        magnetometer.setzAxis(magnetometerDTO.getZAxis());
        return magnetometer;
    }

    public static WiFiRSSI convertToWiFiRSSI(MeasurementDTO.WifiRSSI wifiRSSIDTO) {
        WiFiRSSI wiFiRSSI = new WiFiRSSI();
        for (MeasurementDTO.WifiRSSI.Ap ap : wifiRSSIDTO.getAp()) {
            wiFiRSSI.setRSSI(ap.getSsid(), ap.getValue());
        }
        return wiFiRSSI;
    }

    public static Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        Measurement measurement = new Measurement();

        measurement.setId(UUID.fromString(measurementDTO.getId()));

        measurement.setBluetoothTags(convertToBluetoothTags(measurementDTO.getBluetoothTags()));

        measurement.setGpsCoordinates(convertToGpsCoordinate(measurementDTO.getGpsCoordinates()));

        measurement.setMagnetometer(convertToMagnetometer(measurementDTO.getMagnetometer()));

        measurement.setPosition(convertToPosition(measurementDTO.getPosition()));

        measurement.setRfidtags(new RFIDTags(new HashSet<byte[]>(measurementDTO.getRfidtags().getRfidTag())));

        measurement.setTimestamp(measurementDTO.getTimestamp().toGregorianCalendar().getTime());

        measurement.setWifiRSSI(convertToWiFiRSSI(measurementDTO.getWifiRSSI()));

        return measurement;
    }
}
