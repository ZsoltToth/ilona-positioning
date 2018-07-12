package uni.miskolc.ips.ilona.positioning.service.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import uni.miskolc.ips.ilona.measurement.controller.dto.MeasurementDTO;

import java.util.Collection;

/**
 * Created by satan on 2018.07.12..
 */
@MessagingGateway(name = "MeasurementQueryGateway", defaultRequestChannel = "measurementQueryRequestChannel")
public interface MeasurementGateway {

    @Gateway(headers = {@GatewayHeader(name = "METHOD_NAME", value = "listMeasurement")}, replyChannel = "listMeasurementReplyChannel")
    @Payload("new java.util.Date()")
    Collection<MeasurementDTO> listMeasurements();

}
