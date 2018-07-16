package uni.miskolc.ips.ilona.positioning.controller;

import uni.miskolc.ips.ilona.measurement.controller.dto.CoordinateDTO;
import uni.miskolc.ips.ilona.measurement.controller.dto.PositionDTO;
import uni.miskolc.ips.ilona.measurement.controller.dto.ZoneDTO;
import uni.miskolc.ips.ilona.measurement.model.position.Coordinate;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;

import java.util.UUID;

class PositionDTOConverter {

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
}