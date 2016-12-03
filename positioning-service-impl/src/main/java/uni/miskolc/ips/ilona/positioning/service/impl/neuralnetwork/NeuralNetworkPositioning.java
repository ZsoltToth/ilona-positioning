package uni.miskolc.ips.ilona.positioning.service.impl.neuralnetwork;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.measurement.service.ZoneService;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;


/**
 * The implementation of PositioningService interface to Positioning over a NeuralNetwork.
 * @author tamas13
 *
 */
public class NeuralNetworkPositioning implements PositioningService {
	/**
	 * The NeuralNetwork to estimate the Position.
	 */
	private NeuralNetwork neuralNetwork;
	/**
	 * A service to get the Zone instances from the database.
	 */
	private ZoneService zoneService;
	/**
	 * A log istance for the class.
	 */
	private static final Logger LOG = LogManager.getLogger(NeuralNetworkPositioning.class);

	/**
	 * The constructor of NeuralNetworkPositioning class.
	 * @param zoneService A service to get the Zone instances from the database.
	 * @param serializedNeuralNetwork The path of serialized NeuralNetwork
	 */
	public NeuralNetworkPositioning(final ZoneService zoneService, final String serializedNeuralNetwork) {
		super();
		this.neuralNetwork = NeuralNetwork.deserialization(serializedNeuralNetwork);
		this.zoneService = zoneService;

	}

	/**
	 * Determine Position of the given measurement based on the NeuralNetwork.
	 * @param measurement The incoming measurement to estimate it's Position.
	 * @return The Position estimated with the NeuralNetwork.
	 */
	public final Position determinePosition(final Measurement measurement) {
		Position result;
		MultilayerPerceptron mlp = neuralNetwork.getMultilayerPerceptron();
		Instance instance = neuralNetwork.convertMeasurementToInstance(measurement);
		double cls;
		try {
			cls = mlp.classifyInstance(instance);
			Zone zoneresult = zoneService.getZone(UUID.fromString(instance.classAttribute().value((int) cls)));
			result = new Position(zoneresult);
		} catch (Exception e) {
			result = new Position(Zone.UNKNOWN_POSITION);
		}
		LOG.info(String.format("The incoming measurement is " + measurement.toString()
				+ ". The determined position for this is " + result.toString()));
		if(measurement.getPosition().getZone()!=null)
			LOG.warn(measurement.getPosition().getZone()+","+result.getZone());
		return result;
	}


}