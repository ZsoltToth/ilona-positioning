package uni.miskolc.ips.ilona.positioning.service.impl;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.persist.PositionDAO;
import uni.miskolc.ips.ilona.measurement.persist.MeasurementDAO;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;

public class PositioningServiceImpl implements PositioningService {

	private PositionDAO zoneDAO;
	private MeasurementDAO measurementDAO;

	public PositioningServiceImpl(PositionDAO zoneDAO,
			MeasurementDAO measurementDAO) {
		super();
		this.zoneDAO = zoneDAO;
		this.measurementDAO = measurementDAO;
	}

	public Position determinePosition(Measurement measurement) {
		// TODO Auto-generated method stub
		return null;
	}

}
