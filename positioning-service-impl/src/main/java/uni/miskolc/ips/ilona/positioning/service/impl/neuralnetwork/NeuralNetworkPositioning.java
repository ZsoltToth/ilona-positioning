package uni.miskolc.ips.ilona.positioning.service.impl.neuralnetwork;

import java.util.Collection;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;
import uni.miskolc.ips.ilona.positioning.service.gateway.ZoneGateway;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instance;


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
	 * A gateway to get the Zones.
	 */
	private ZoneGateway zoneGateway;
	/**
	 * A log istance for the class.
	 */
	private static final Logger LOG = LogManager.getLogger(NeuralNetworkPositioning.class);

	/**
	 * The constructor of NeuralNetworkPositioning class.
	 * @param zoneGateway A service to get the Zone instances from the database.
	 * @param serializedNeuralNetwork The path of serialized NeuralNetwork
	 */
	public NeuralNetworkPositioning(final ZoneGateway zoneGateway, final String serializedNeuralNetwork) {
		super();
		this.neuralNetwork = NeuralNetwork.deserialization(serializedNeuralNetwork);
		this.zoneGateway = zoneGateway;

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
			Collection<Zone> gatewayZones = zoneGateway.listZones();
			for(Zone z : gatewayZones){
				if(z.getId().equals(UUID.fromString(instance.classAttribute().value((int) cls)))){
					result = new Position(z);
					break;
				}
			}
			result = new Position(Zone.UNKNOWN_POSITION);
		} catch (Exception e) {
			result = new Position(Zone.UNKNOWN_POSITION);
		}
		LOG.info(String.format("The incoming measurement is " + measurement.toString()
				+ ". The determined position for this is " + result.toString()));
		if(measurement.getPosition() != null && measurement.getPosition().getZone()!=null){
			LOG.warn(measurement.getId()+","+measurement.getPosition().getZone().getName()+","+measurement.getPosition().getZone().getId() + "," + result.getZone().getName()+","+result.getZone().getId());}
		return result;
	}


}