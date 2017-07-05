package uni.miskolc.ips.ilona.positioning.service.impl.classification.bayes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.measurement.MeasurementDistanceCalculator;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.service.MeasurementService;
import uni.miskolc.ips.ilona.measurement.service.exception.DatabaseUnavailableException;
import uni.miskolc.ips.ilona.positioning.exceptions.InvalidMeasurementException;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;

/**
 * It is based on the Bayes theorem. P(A|B) = ( P(B|A) * P(A) ) / ( P(B) ) Where
 * <dl>
 * <dt>A</dt>
 * <dd>Position</dd>
 * <dt>B</dt>
 * <dd>Sensor</dd>
 * </dl>
 * 
 * 
 * @author tamas13
 *
 */
public class NaiveBayesPositioningService implements PositioningService {
	private static final Logger LOG = LogManager.getLogger(NaiveBayesPositioningService.class);
	private MeasurementService measurementservice;
	private MeasurementDistanceCalculator measDistanceCalculator;
	private double maxMeasurementDistance;
	private Map<UUID, Double> measurementsdistance;

	public NaiveBayesPositioningService(MeasurementService measurementservice,
			MeasurementDistanceCalculator measDistanceCalculator, double maxMeasurementDistance) {
		super();
		this.measurementservice = measurementservice;
		this.measDistanceCalculator = measDistanceCalculator;
		this.maxMeasurementDistance = maxMeasurementDistance;
	}

	public Position determinePosition(Measurement measurement) throws InvalidMeasurementException {
		if(measurement.getId() == null){
			throw new InvalidMeasurementException();
		}
		Collection<Position> positionswithzone = null;
		Collection<Measurement> measurements;
		measurementsdistance = new HashMap<>();
		try {
			// positionswithzone = this
			// .positionsWithZone(positionservice.readPositions());
			measurements = measurementservice.readMeasurements();
		} catch (DatabaseUnavailableException e) {
			LOG.warn(e.getMessage());
			return new Position(Zone.UNKNOWN_POSITION);
		}
		Collection<Position> positions = new ArrayList<>();
		for (Measurement m : measurements) {
			positions.add(m.getPosition());
		}
		positionswithzone = this.positionsWithZone(positions);

		if (positionswithzone.isEmpty()) {
			LOG.warn("No position with zone");
			return new Position(Zone.UNKNOWN_POSITION);
		}
		
		calculatedistances(measurements, measurement);
		
		Position bestFit = new Position(Zone.UNKNOWN_POSITION);
		double bestFitProbability = -1;
		for (Position each : positionswithzone) {
			double currentProbability = probabilityOfPositionIfSensor(measurement, each, measurements);
			if (bestFitProbability < currentProbability) {
				bestFit = each;
				bestFitProbability = currentProbability;

			}
		}
		LOG.info(String.format("The position of %s measurement is %s with %f probability", measurement, bestFit.getZone(),bestFitProbability));
		LOG.warn(measurement.getId() + "," + measurement.getPosition().getZone().getName() + ","
				+ measurement.getPosition().getZone().getId() + "," + bestFit.getZone().getName() + ","
				+ bestFit.getZone().getId()+" probability: "+bestFitProbability);
		return bestFit;
	}

	private void calculatedistances(Collection<Measurement> measurements, Measurement meas) {
		for (Measurement each : measurements) {
			double distance = measDistanceCalculator.distance(each, meas);
			measurementsdistance.put(each.getId(), distance);
		}
		
	}

	private Collection<Position> positionsWithZone(Collection<Position> positions) {
		Collection<Position> result = new ArrayList<Position>();
		for (Position each : positions) {
			if (each.getZone() == null) {
				continue;
			}
			result.add(each);
		}
		return result;
	}

	private double probabilityOfSimilarSensors(Measurement meas, Collection<Measurement> measurements) {
		double result = 0.0;
		int matches = 0;

		for (Measurement each : measurements) {
			double distance = measurementsdistance.get(each.getId());
			if (distance <= maxMeasurementDistance && distance != -1) {
				matches++;
			}
		}

		result = (double) matches / (double) measurements.size();

		return result;
	}

	private double probabilityIfPosition(Position position, Collection<Measurement> measurements) {
		double result = 0.0;
		int matches = 0;

		if (position.getZone() == null) {
			return result;
		}
		for (Measurement each : measurements) {
			if (each.getPosition().getZone() == null) {
				continue;
			}
			if (each.getPosition().getZone().getId().equals(position.getZone().getId())) {
				matches++;
			}
		}
		result = (double) matches / (double) measurements.size();
		return result;

	}

	private double probabilityOfPositionIfSensor(Measurement meas, Position position,
			Collection<Measurement> measurements) {
		double result = (probabilityOfSensorIfPosition(meas, position, measurements)
				* probabilityIfPosition(position, measurements)) / probabilityOfSimilarSensors(meas, measurements);
		return result;
	}

	private double probabilityOfSensorIfPosition(Measurement meas, Position position,
			Collection<Measurement> measurements) {
		double result = -1.0;
		int matches = 0;
		int cases = 0;
		
		Collection<Measurement> measurementswithPosition = new ArrayList<Measurement>();
		for (Measurement each : measurements) {
			if (!each.getPosition().getZone().getId().equals(position.getZone().getId())) {
				continue;
			}
			measurementswithPosition.add(each);
		}


		for (Measurement each : measurementswithPosition) {
			cases++;
			double distance = measurementsdistance.get(each.getId());
			if (distance <= maxMeasurementDistance && distance != -1) {
				matches++;
			}
		}
		result = (double) matches / (double) cases;
		if (matches == 0) {
			return 0;
		}
		return result;
	}

}
