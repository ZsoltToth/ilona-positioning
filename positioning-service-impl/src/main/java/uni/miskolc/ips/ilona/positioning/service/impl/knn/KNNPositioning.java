package uni.miskolc.ips.ilona.positioning.service.impl.knn;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.measurement.MeasurementDistanceCalculator;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.service.MeasurementService;
import uni.miskolc.ips.ilona.measurement.service.exception.DatabaseUnavailableException;

import java.util.ArrayList;

import java.util.Collections;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * 
 * @author judit The KNNPositioning is an abstract class of the k-NN algorithm.
 *         The KNNPositioning implements the PositioningServices interface. k is
 *         the number of Neighbours the vote is based on.
 */
public abstract class KNNPositioning implements PositioningService {
	/**
	 * The logger of the class.
	 */
	private static final Logger LOG = LogManager.getLogger(KNNPositioning.class);
	/**
	 * The distance calculating strategy.
	 */
	private final MeasurementDistanceCalculator distanceCalculator;
	/**
	 * A service for access to the measurements in the database.
	 */
	private final MeasurementService measurementservice;
	/**
	 * The k parameter of the k-NN algorithm.
	 */
	private int k;

	/**
	 * 
	 * @param distanceCalculator
	 *            The distance function used in k-NN.
	 * @param measurementservice
	 *            The measurementservice provide access to the measurements in
	 *            the database
	 * @param k
	 *            is the k parameter of the k Nearest Neighbour algorithm.
	 */
	public KNNPositioning(final MeasurementDistanceCalculator distanceCalculator,
			final MeasurementService measurementservice, final int k) throws IllegalArgumentException {
		super();
		if(distanceCalculator == null || measurementservice == null ){
			throw new IllegalArgumentException();
		}
		this.distanceCalculator = distanceCalculator;
		this.measurementservice = measurementservice;
		setK(k);
	}

	/**
	 * getK return the k member of the instance.
	 * 
	 * @return k The k member
	 */
	public final int getK() {
		return k;
	}

	/**
	 * setK() set the new k value.
	 * 
	 * @param k
	 *            the given k value.
	 */
	public final void setK(final int k) throws IllegalArgumentException{
		if (k > 0) {
			LOG.info(String.format("k has bet set from %d to %d", this.k, k));
			this.k = k;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @param measurement
	 *            The measurement we want to know the Position.
	 * @return position The estimated position of the measurement.
	 */
	public final Position determinePosition(final Measurement measurement) throws IllegalArgumentException {
		final ArrayList<Measurement> measurements;
		try {
			measurements = new ArrayList<Measurement>(measurementservice.readMeasurements());
		} catch (DatabaseUnavailableException e) {
			return new Position(Zone.UNKNOWN_POSITION);
		}
		if (measurements.size() == 0){
			return new Position(Zone.UNKNOWN_POSITION);}
		if(k > measurements.size()){
			throw new IllegalArgumentException();
		}

		ArrayList<Neighbour> neighbours = getNeighbourList(measurements, measurement);
		final ArrayList<Neighbour> kNearestNeighbours = getKNearestNeighbour(neighbours);
		Position result = getMajorVote(kNearestNeighbours);
		LOG.info(String.format("The position of measurement "+ measurement.toString()+" is position "+result.getZone()));
		return result;
	}

	/**
	 * 
	 * @param measurements
	 *            The measurements from the database
	 * @param measurement
	 *            The measurement we want to determine neighbours
	 * @return The list of Neighbours which contains the distances of the
	 *         measurements and measurement parameter
	 */
	private ArrayList<Neighbour> getNeighbourList(final ArrayList<Measurement> measurements,
			final Measurement measurement) {
		ArrayList<Neighbour> neighbours = new ArrayList<Neighbour>();

		for (Measurement m : measurements) {
			double distance = distanceCalculator.distance(m, measurement);
			neighbours.add(new Neighbour(m, distance));
		}
		return neighbours;

	}

	/**
	 * getKNearestNeighbour sort the list, ant return with the k first element.
	 * 
	 * @param neighbours
	 *            The list of the neighbours
	 * @return the list of k Nearest Neighbour
	 */
	private ArrayList<Neighbour> getKNearestNeighbour(final ArrayList<Neighbour> neighbours) {
		NeighbourComparator nc = new NeighbourComparator();
		Collections.sort(neighbours, nc);
		return new ArrayList<Neighbour>(neighbours.subList(0, k));
	}

	/**
	 * Calculates the vote of the neighbours.
	 * 
	 * @param nearestneighbours
	 *            The list of the k Nearest neighbour
	 * @return the votes of the neighbours
	 */
	protected abstract Position getMajorVote(ArrayList<Neighbour> nearestneighbours);

	/**
	 * 
	 * @param votes
	 *            The array of the votes
	 * @return the index of the maximum value
	 */
	protected final int getIndexOfMaxValue(final double[] votes) {
		double maxvalue = votes[0];
		int maxindex = 0;
		for (int i = 1; i < votes.length; i++) {
			if (maxvalue < votes[i]) {
				maxvalue = votes[i];
				maxindex = i;
			}
		}
		return maxindex;
	}

}