package uni.miskolc.ips.ilona.positioning.service.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import uni.miskolc.ips.ilona.measurement.controller.dto.UserPositionDTO;

@MessagingGateway(name = "TrackingQueryGateway", defaultRequestChannel = "trackingQueryRequestChannel")
public interface TrackingGateway {

    @Gateway(headers = {@GatewayHeader(name = "METHOD_NAME", value = "addHistory")}, replyChannel = "addHistoryReplyChannel")
    @Payload(("new java.util.Date()"))
    void addHistory(UserPositionDTO userPositionDTO);
}
