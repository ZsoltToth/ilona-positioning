package uni.miskolc.ips.ilona.positioning.service;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.positioning.exceptions.InvalidMeasurementException;
import uni.miskolc.ips.ilona.positioning.exceptions.PositioningFailureException;

public interface PositioningService {

    public Position determinePosition(Measurement measurement) throws InvalidMeasurementException, PositioningFailureException;
}
