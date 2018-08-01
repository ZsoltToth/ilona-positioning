package uni.miskolc.ips.ilona.positioning.service.gateway;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uni.miskolc.ips.ilona.measurement.controller.dto.CoordinateDTO;
import uni.miskolc.ips.ilona.measurement.controller.dto.PositionDTO;
import uni.miskolc.ips.ilona.measurement.controller.dto.UserPositionDTO;
import uni.miskolc.ips.ilona.measurement.controller.dto.ZoneDTO;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;
import java.util.UUID;

public class TrackingGatewayIT {
    private TrackingGateway trackingGateway;

    @Before
    public void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TrackingGatewaySIConfig.class);
        trackingGateway = context.getBean("TrackingQueryGateway", TrackingGateway.class);
    }

    @Test
    public void addHistoryTest() throws DatatypeConfigurationException {
        UserPositionDTO userPositionDTO = new UserPositionDTO();
        userPositionDTO.setUserId("testId");
        userPositionDTO.setTime(convertToXMLXmlGregorianCalendar(System.currentTimeMillis()));
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(UUID.randomUUID().toString());
        ZoneDTO zoneDTO = new ZoneDTO();
        zoneDTO.setName("dummyZone");
        zoneDTO.setId(UUID.randomUUID().toString());
        positionDTO.setZone(zoneDTO);
        CoordinateDTO coordinateDTO = new CoordinateDTO();
        coordinateDTO.setX(1);
        coordinateDTO.setY(1);
        coordinateDTO.setZ(1);
        positionDTO.setCoordinate(coordinateDTO);
        userPositionDTO.setPosition(positionDTO);
        trackingGateway.addHistory(userPositionDTO);
    }

    private static XMLGregorianCalendar convertToXMLXmlGregorianCalendar(long time) throws DatatypeConfigurationException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(time);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    }

}
