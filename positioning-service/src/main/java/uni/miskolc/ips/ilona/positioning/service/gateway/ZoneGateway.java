package uni.miskolc.ips.ilona.positioning.service.gateway;

import uni.miskolc.ips.ilona.measurement.model.position.Zone;

import java.util.Collection;

/**
 * Created by tothzs on 2017.07.11..
 */
public interface ZoneGateway {

    Collection<Zone> listZones();
}
