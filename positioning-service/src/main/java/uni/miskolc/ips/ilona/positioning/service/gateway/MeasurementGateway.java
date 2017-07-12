package uni.miskolc.ips.ilona.positioning.service.gateway;

import org.springframework.messaging.handler.annotation.Payload;
import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;

import java.util.Collection;
import java.util.Date;

/**
 * Created by tamas on 2017.07.12..
 */
public interface MeasurementGateway {

    @Payload("new java.util.Date()")
    Collection<Measurement> listMeasurements();

}
