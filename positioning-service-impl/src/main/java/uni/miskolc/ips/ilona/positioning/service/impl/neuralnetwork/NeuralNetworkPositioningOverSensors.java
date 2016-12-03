package uni.miskolc.ips.ilona.positioning.service.impl.neuralnetwork;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Coordinate;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.service.ZoneService;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;
import weka.core.Debug.Log;
import weka.core.Instance;

/**
 * 
 * @author tamas13 NeuralNetworkPositioningOverSensors is a class for
 *         individually evaluate sensor values over specific neural network, and
 *         get an overall result based on the weight of each sensor. Implements
 *         PositioningService interface to determine Position.
 * 
 *
 */
public class NeuralNetworkPositioningOverSensors implements PositioningService {
	/**
	 * 
	 * @author tamas13 The sensor denominations
	 */
	private enum sensorNames {
		/**
		 * The denomination for Magnetic field sensor.
		 */
		MAGNETOMETER,
		/**
		 * The denomination for Bluetooth sensor.
		 */
		BLUETOOTH,
		/**
		 * The denomination for Wifi sensor.
		 */
		WIFI
	};

	/**
	 * A hashmap for mapping NeuralNetworks to weights.
	 */
	private Map<NeuralNetwork, Double> weights;
	/**
	 * A hashmap for mapping enum values to NeuralNetworks.
	 */
	private Map<sensorNames, NeuralNetwork> sensors;
	/**
	 * A service for get the Zone instances from the database.
	 */
	private ZoneService zoneService;
	/**
	 * A List to contain the summed weights of the element at the same index of
	 * zones List.
	 */
	private ArrayList<Double> votes;
	/**
	 * The estimated Zones from the sensor evaluations.
	 */
	private ArrayList<Zone> zones;

	private static final Logger LOG;

	static {
		// LoggerContext context = (LoggerContext) LogManager.getContext();
		// File log4jConfigFile = new
		// File(System.getProperty("log4j2.config.file"));
		// context.setConfigLocation(log4jConfigFile.toURI());
		LOG = LogManager.getLogger(NeuralNetworkPositioningOverSensors.class);
	}

	/**
	 * The constructor for the NeuralNetworkPositioningOverSensors class.
	 * 
	 * @param zoneService
	 *            The service for get the Zone instances from the database.
	 * @param bluetoothWeight
	 *            The weight of the estimated Zone based on only Bluetooth
	 *            sensor values.
	 * @param magnetometerWeight
	 *            The weight of the estimated Zone based on only Magnetometer
	 *            sensor values.
	 * @param wifiWeight
	 *            The weight of the estimated Zone based on only Wifi sensor
	 *            values.
	 * @param pathOfBluetoothNeuralNetwork
	 *            The path of the serialized NeuralNetwork of only Bluetooth
	 *            sensor values.
	 * @param pathToMagnetometerNeuralNetwork
	 *            The path of the serialized NeuralNetwork of only Magnetometer
	 *            sensor values.
	 * @param pathToWifiNeuralNetwork
	 *            The path of the serialized NeuralNetwork of only Wifi sensor
	 *            values.
	 */
	public NeuralNetworkPositioningOverSensors(final ZoneService zoneService, final double bluetoothWeight,
			final double magnetometerWeight, final double wifiWeight, final String pathOfBluetoothNeuralNetwork,
			final String pathToMagnetometerNeuralNetwork, final String pathToWifiNeuralNetwork) {
		super();
		this.zoneService = zoneService;

		NeuralNetwork bluetoothNeuralNetwork = NeuralNetwork.deserialization(pathOfBluetoothNeuralNetwork);
		NeuralNetwork magnetometerNeuralNetwork = NeuralNetwork.deserialization(pathToMagnetometerNeuralNetwork);
		NeuralNetwork wifiNeuralNetwork = NeuralNetwork.deserialization(pathToWifiNeuralNetwork);

		weights = new HashMap<NeuralNetwork, Double>();
		sensors = new HashMap<sensorNames, NeuralNetwork>();
		if (bluetoothNeuralNetwork != null) {
			weights.put(bluetoothNeuralNetwork, bluetoothWeight);
			sensors.put(sensorNames.BLUETOOTH, bluetoothNeuralNetwork);
		}
		if (magnetometerNeuralNetwork != null) {
			weights.put(magnetometerNeuralNetwork, magnetometerWeight);

			sensors.put(sensorNames.MAGNETOMETER, magnetometerNeuralNetwork);
		}
		if (wifiNeuralNetwork != null) {
			weights.put(wifiNeuralNetwork, wifiWeight);

			sensors.put(sensorNames.WIFI, wifiNeuralNetwork);
		}

	}

	/**
	 * Determine Position of the given measurement based on each available
	 * sensor.
	 * 
	 * @param measurement
	 *            The incoming measurement to estimate it's Position.
	 * @return The Position calculated based on the weight of Position estimated
	 *         with each sensor.
	 */
	public final Position determinePosition(final Measurement measurement) {
		zones = new ArrayList<Zone>();
		int maxsize = sensors.size();
		votes = new ArrayList<Double>(maxsize);

		for (sensorNames s : sensorNames.values()) {
			NeuralNetwork neuralNetwork = sensors.get(s);

			if (neuralNetwork != null) {
				Instance instance = neuralNetwork.convertMeasurementToInstance(measurement);
		
				if (measurement.getMagnetometer() != null && s.equals(sensorNames.MAGNETOMETER)) {
					System.out.println("M");
					evaluateNeuralNetwork(neuralNetwork, instance);
				}
				if (measurement.getBluetoothTags() != null && s.equals(sensorNames.BLUETOOTH)) {
					System.out.println("B");
					evaluateNeuralNetwork(neuralNetwork, instance);
				}
				if (measurement.getWifiRSSI() != null && s.equals(sensorNames.WIFI)) {
					System.out.println("W");
					evaluateNeuralNetwork(neuralNetwork, instance);
				}
			}

		}
		if (zones.isEmpty()) {
			return new Position(new Coordinate(), Zone.UNKNOWN_POSITION);
		}
		double[] votesArray = new double[maxsize];
		for (int i = 0; i < votes.size(); i++) {
			votesArray[i] = votes.get(i);
		}
		int maxIndex = getIndexOfMaxValue(votesArray);
		Zone result = zones.get(maxIndex);
		if (measurement.getPosition() != null) {
			if (measurement.getPosition().getZone() != null) {

				LOG.warn(measurement.getPosition().getZone() + "," + result);
			}
		}
		return new Position(result);
	}

	/**
	 * Add Zone to the zones list , and weight value to the votes list if zones
	 * is not contain the Zone, or increment the corresponding vote with the
	 * weight of the Zone if it is contained in the zones List.
	 * 
	 * @param neuralNetwork
	 *            The specific NeuralNetwork for evaluation.
	 * @param instance
	 *            The incoming instance to classify over the NeuralNetwork
	 */
	private void evaluateNeuralNetwork(final NeuralNetwork neuralNetwork, final Instance instance) {
		double cls;
		try {
			cls = neuralNetwork.getMultilayerPerceptron().classifyInstance(instance);
			Zone result = zoneService.getZone(UUID.fromString(instance.classAttribute().value((int) cls)));
			if (zones.contains(result)) {
				int index = zones.indexOf(result);
				incrementVotes(index, weights.get(neuralNetwork));
			} else {
				zones.add(result);
				votes.add(weights.get(neuralNetwork));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * An assistant function to add weight to the corresponding place to the
	 * votes List.
	 * 
	 * @param index
	 *            The index of the element for alter
	 * @param value
	 *            The increment quantity of the weight
	 */
	private void incrementVotes(final int index, final double value) {
		double current = votes.get(index);
		double incremented = current + value;
		votes.set(index, incremented);
	}

	/**
	 * An assistant function to get the index of the element with the maximum
	 * value.
	 * 
	 * @param votes
	 *            The array of the elements
	 * @return the index of the elemenet with the maximum value.
	 */
	private int getIndexOfMaxValue(final double[] votes) {
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
