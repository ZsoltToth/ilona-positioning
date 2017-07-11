package uni.miskolc.ips.ilona.positioning.service;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;

public interface PositioningService {

	Position determinePosition(Measurement measurement);
}
