package uni.miskolc.ips.ilona.positioning.service.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import uni.miskolc.ips.ilona.measurement.controller.dto.ZoneDTO;

import java.util.Collection;

/**
 * Created by benczus on 2018.07.6..
 */


@MessagingGateway(name = "ZoneQueryGateway", defaultRequestChannel = "zoneQueryRequestChannel")
public interface ZoneQueryService {

    @Gateway(headers = {@GatewayHeader(name = "METHOD_NAME", value = "listZones")}, replyChannel = "listZonesReplyChannel")
    @Payload("new java.util.Date()")
    Collection<ZoneDTO> listZones();

    @Gateway(headers = {@GatewayHeader(name = "METHOD_NAME", value = "getZone")}, replyChannel = "getZoneReplyChannel")
    @Payload(("new java.util.Date()"))
    ZoneDTO getZoneById(@Header(name = "zoneID") final String zoneID);


}
