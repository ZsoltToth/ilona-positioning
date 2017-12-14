package uni.miskolc.ips.ilona.positioning.service.impl.knn;

import java.util.ArrayList;

import uni.miskolc.ips.ilona.measurement.model.measurement.MeasurementDistanceCalculator;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.positioning.service.gateway.MeasurementGateway;
import uni.miskolc.ips.ilona.positioning.model.knn.Neighbour;
/**
 * 
 * @author ilona
 *
 */
public class KNNWeightedPositioning extends KNNPositioning {
	/**
	 * The constructor of the KNNWeightedPositioning class.
	 * 
	 * @param distanceCalculator
	 *            The distance function used in k-NN.
	 * @param measurementGateway
	 *            The measurementGateway provide the measurements
	 * @param k
	 *            is the k parameter of the k Nearest Neighbour algorithm.
	 */
	public KNNWeightedPositioning(final MeasurementDistanceCalculator distanceCalculator,
								  final MeasurementGateway measurementGateway, final int k) {
		super(distanceCalculator, measurementGateway, k);
	}

	@Override
	protected final Position doGetMajorVote(final ArrayList<Neighbour> nearestneighbours) {
		ArrayList<Zone> zones = new ArrayList<Zone>();
		int maxsize = nearestneighbours.size();
		double[] votes = new double[maxsize];
		Position result = null;
		for (Neighbour n : nearestneighbours) {
			Zone zone = n.getMeasurement().getPosition().getZone();
			if (!zones.contains(zone)) {
				zones.add(zone);
				votes[zones.indexOf(zone)] = 1 / n.getDistance();
			} else {
				votes[zones.indexOf(zone)] += 1 / n.getDistance();
			}
		}
		int maxindex = getIndexOfMaxValue(votes);
		result = new Position(zones.get(maxindex));
		return result;

	}

}