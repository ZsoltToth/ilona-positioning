package uni.miskolc.ips.ilona.positioning.service.gateway;

import org.springframework.messaging.handler.annotation.Payload;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by tothzs on 2017.07.11..
 */
public interface ZoneGateway {

    @Payload("new java.util.Date()")
    Collection<Zone> listZones();

    Zone getZoneById(@Payload UUID id);
}
