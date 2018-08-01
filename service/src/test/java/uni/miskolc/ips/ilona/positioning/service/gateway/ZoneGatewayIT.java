package uni.miskolc.ips.ilona.positioning.service.gateway;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uni.miskolc.ips.ilona.measurement.controller.dto.ZoneDTO;

import java.util.Collection;

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;

/**
 * Created by tothzs on 2017.07.11..
 */
@Ignore
public class ZoneGatewayIT {

    private static final String sysEnvMeasurementHost = "measurement.host";
    private static final String sysEnvMeasurementPort = "measurement.port";

    //private static final String siConfigLocation = "/si-config-measurement.xml";
    private ZoneQueryService zoneQueryService;

    @BeforeClass
    public static void beforeClass() {
        assumeNotNull(System.getProperty(sysEnvMeasurementHost));
        assumeNotNull(System.getProperty(sysEnvMeasurementPort));
        try {
            Integer.parseInt(System.getProperty(sysEnvMeasurementPort));
        } catch (NumberFormatException ex) {
            assumeNoException(ex);
        }
    }

    @Before
    public void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext();
        zoneQueryService = context.getBean("ZoneGateway", ZoneQueryService.class);

    }

    @Test
    public void test() {
        System.out.println(
                String.format("%s:%d/ilona-measurement/listZones",
                        System.getProperty(sysEnvMeasurementHost),
                        Integer.parseInt(System.getProperty(sysEnvMeasurementPort))));
        Collection<ZoneDTO> result = zoneQueryService.listZones();
        System.out.println("---> " + result);
        System.out.println("--->" + result.size());
        System.out.println(result.getClass());
        for (ZoneDTO zone : result) {
            System.out.println(String.format("%s : %s", zone.getId(), zone.getName()));
        }

        System.out.println("Result for gateway getZoneById method: " + zoneQueryService.getZoneById(("14fc835a-ee28-4b78-9c59-9ee0f759ce56")));
    }
}
