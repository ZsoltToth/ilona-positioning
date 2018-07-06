package uni.miskolc.ips.ilona.positioning.service.impl.svm;

import java.util.Collection;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.positioning.model.MeasurementToInstanceConverter;
import uni.miskolc.ips.ilona.positioning.model.svm.SupportVectorMachine;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;
import uni.miskolc.ips.ilona.positioning.service.gateway.ZoneQueryService;
import weka.classifiers.functions.LibSVM;
import weka.core.Instance;

public class SVMPositioning implements PositioningService {

	private SupportVectorMachine svm;
	
	private ZoneQueryService zoneGateway;
	
	private static final Logger LOG = LogManager.getLogger(SVMPositioning.class);

	public SVMPositioning(String serializedFilePath, ZoneQueryService zoneGateway) {
		super();
		this.svm = SupportVectorMachine.deserialization(serializedFilePath);
		this.zoneGateway = zoneGateway;
	}

	@Override
	public Position determinePosition(Measurement measurement) {
		Position result= new Position(Zone.UNKNOWN_POSITION);
		LibSVM libsvm = svm.getLibsvm();
		Instance instance = MeasurementToInstanceConverter.convertMeasurementToInstance(measurement, svm.getHeader());
		instance.setDataset(svm.getTrainingSet()); // SVM requires the known dataset
		double cls;
		try {
			cls = libsvm.classifyInstance(instance);
			Collection<Zone> gatewayresult = zoneGateway.listZones();
			for(Zone z : gatewayresult){
				if(z.getId().equals(UUID.fromString(instance.classAttribute().value((int) cls)))){
					result = new Position(z);
					break;
				}
			}
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
