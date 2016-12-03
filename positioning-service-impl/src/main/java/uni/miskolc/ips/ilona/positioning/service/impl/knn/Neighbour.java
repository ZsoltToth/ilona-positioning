package uni.miskolc.ips.ilona.positioning.service.impl.knn;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;

/**
 * This class represents a Neighbour with the Measurement and the distance.
 * 
 * @author ilona
 *
 */
public class Neighbour {
	/**
	 * The Neighbour measurement instance.
	 */
	private Measurement measurement;
	/**
	 * The distance of the measurement to a reference measurement.
	 */
	private double distance;

	/**
	 * The constructor of Neighbour class.
	 * 
	 * @param measurement
	 *            The Neighbour measurement
	 * @param distance
	 *            The distance of the measurement to a reference measurement
	 */
	public Neighbour(final Measurement measurement, final double distance) {
		super();
		this.measurement = measurement;
		this.distance = distance;
	}

	/**
	 * Get the Neighbour measurement.
	 * @return The measurement member
	 */
	public Measurement getMeasurement() {
		return measurement;
	}

	/**
	 * Set the Neighbour measurement.
	 * @param measurement The neighbour measurement
	 */
	public void setMeasurement(final Measurement measurement) {
		this.measurement = measurement;
	}

	/**
	 * Get the distance of the Neighbour.
	 * @return The distance of the Neighbour
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Set the distance of the Neighbour.
	 * @param distance  The distance value of the Neighbour
	 */
	public void setDistance(final double distance) {
		this.distance = distance;
	}

}
