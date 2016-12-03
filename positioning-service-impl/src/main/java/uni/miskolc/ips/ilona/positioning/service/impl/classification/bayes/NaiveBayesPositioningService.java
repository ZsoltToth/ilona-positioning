package uni.miskolc.ips.ilona.positioning.service.impl.classification.bayes;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.measurement.MeasurementDistanceCalculator;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.service.MeasurementService;
import uni.miskolc.ips.ilona.measurement.service.PositionService;
import uni.miskolc.ips.ilona.measurement.service.exception.DatabaseUnavailableException;
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
	private static final Logger LOG = LogManager
			.getLogger(NaiveBayesPositioningService.class);
	private MeasurementService measurementservice;
	private PositionService positionservice;
	private MeasurementDistanceCalculator measDistanceCalculator;
	private double maxMeasurementDistance;

	public NaiveBayesPositioningService(MeasurementService measurementservice,
			PositionService positionservice,
			MeasurementDistanceCalculator measDistanceCalculator,
			double maxMeasurementDistance) {
		super();
		this.measurementservice = measurementservice;
		this.measDistanceCalculator = measDistanceCalculator;
		this.maxMeasurementDistance = maxMeasurementDistance;
		this.positionservice = positionservice;
	}

	public Position determinePosition(Measurement measurement) {
		Collection<Position> positionswithzone = null;
		Collection<Measurement> measurements;
		try {
			positionswithzone = this
					.positionsWithZone(positionservice.readPositions());
			measurements = measurementservice
					.readMeasurements();
		} catch (DatabaseUnavailableException e) {
			return new Position(Zone.UNKNOWN_POSITION);
		}
		if (positionswithzone.isEmpty()) {
			return new Position(Zone.UNKNOWN_POSITION);
		}
		Position bestFit = new Position(Zone.UNKNOWN_POSITION);
		double bestFitProbability = -1;
		for (Position each : positionswithzone) {
			double currentProbability = probabilityOfPositionIfSensor(
					measurement, each, measurements);
			if (bestFitProbability < currentProbability) {
				bestFit = each;
				bestFitProbability = currentProbability;

			}
		}
		LOG.info(String.format("The position of %s measurement is %s",
				measurement, bestFit.getZone()));

		return bestFit;
	}

	private Collection<Position> positionsWithZone(
			Collection<Position> positions) {
		Collection<Position> result = new ArrayList<Position>();
		for (Position each : positions) {
			if (each.getZone() == null) {
				continue;
			}
			result.add(each);
		}
		return result;
	}

	private double probabilityOfSimilarSensors(Measurement meas,
			Collection<Measurement> measurements) {
		double result = 0.0;
		int matches = 0;

		for (Measurement each : measurements) {
			if (measDistanceCalculator.distance(each, meas) <= maxMeasurementDistance
					&& (measDistanceCalculator.distance(each, meas)) != -1) {
				matches++;
			}
		}

		result = (double) matches / (double) measurements.size();

		return result;
	}

	private double probabilityIfPosition(Position position,
			Collection<Measurement> measurements) {
		double result = 0.0;
		int matches = 0;

		if (position.getZone() == null) {
			return result;
		}
		for (Measurement each : measurements) {
			if (each.getPosition().getZone() == null) {
				continue;
			}
			if (each.getPosition().getZone().getId()
					.equals(position.getZone().getId())) {
				matches++;
			}
		}
		result = (double) matches / (double) measurements.size();
		return result;

	}

	private double probabilityOfPositionIfSensor(Measurement meas,
			Position position, Collection<Measurement> measurements) {
		double result = (probabilityOfSensorIfPosition(meas, position,
				measurements) * probabilityIfPosition(position, measurements))
				/ probabilityOfSimilarSensors(meas, measurements);
		return result;
	}

	private double probabilityOfSensorIfPosition(Measurement meas,
			Position position, Collection<Measurement> measurements) {
		double result = -1.0;
		Collection<Measurement> measurementswithPosition = new ArrayList<Measurement>();
		for (Measurement each : measurements) {
			if (!each.getPosition().equals(position)) {
				continue;
			}
			measurementswithPosition.add(each);
		}

		int matches = 0;
		int cases = 0;

		for (Measurement each : measurementswithPosition) {
			cases++;
			if (measDistanceCalculator.distance(each, meas) <= maxMeasurementDistance
					&& (measDistanceCalculator.distance(each, meas) != -1)) {
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
