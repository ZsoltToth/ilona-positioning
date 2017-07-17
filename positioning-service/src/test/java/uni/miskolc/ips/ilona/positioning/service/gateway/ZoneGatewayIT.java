package uni.miskolc.ips.ilona.positioning.service.gateway;

import static org.junit.Assume.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by tothzs on 2017.07.11..
 */

public class ZoneGatewayIT {

    private static final String sysEnvMeasurementHost = "measurement.host";
    private static final String sysEnvMeasurementPort = "measurement.port";

    private static final String siConfigLocation = "/si-config-measurement.xml";

    private ApplicationContext context;

    @BeforeClass
    public static void beforeClass(){
        assumeNotNull(System.getProperty(sysEnvMeasurementHost));
        assumeNotNull(System.getProperty(sysEnvMeasurementPort));
        try{
            Integer.parseInt(System.getProperty(sysEnvMeasurementPort));
        }catch(NumberFormatException ex){
            assumeNoException(ex);
        }
    }

    @Before
    public void setUp(){
        this.context = new ClassPathXmlApplicationContext(siConfigLocation);
    }

    @Test
    public void test(){
        System.out.println(
                String.format("%s:%d/ilona-measurement/listZones",
                        System.getProperty(sysEnvMeasurementHost),
                        Integer.parseInt(System.getProperty(sysEnvMeasurementPort))));
        ZoneGateway gateway = context.getBean("ZoneGateway", ZoneGateway.class);
        Collection<Zone> result = gateway.listZones();
        System.out.println("---> "+result);
        System.out.println("--->"+result.size());
        System.out.println(result.getClass());
        for(Zone zone : result){
            System.out.println(String.format("%s : %s",zone.getId(), zone.getName()));
        }

        System.out.println("Result for gateway getZoneById method: "+ gateway.getZoneById(UUID.fromString("14fc835a-ee28-4b78-9c59-9ee0f759ce56")));
    }
}
