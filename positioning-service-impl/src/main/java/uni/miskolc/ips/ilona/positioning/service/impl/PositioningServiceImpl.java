package uni.miskolc.ips.ilona.positioning.service.impl;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.positioning.exceptions.InvalidMeasurementException;
import uni.miskolc.ips.ilona.positioning.exceptions.PositioningFailureException;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;

public class PositioningServiceImpl implements PositioningService {



	public PositioningServiceImpl() {
		super();

	}

	public Position determinePosition(Measurement measurement) throws InvalidMeasurementException, PositioningFailureException{
		if(measurement.getId() == null){
			throw new InvalidMeasurementException();
		}
		return new Position(Zone.UNKNOWN_POSITION);
	}

}
