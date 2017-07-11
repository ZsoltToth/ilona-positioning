package uni.miskolc.ips.ilona.positioning.service.gateway;

import static org.junit.Assume.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
                String.format("%s:%d/ilona/listZones",
                        System.getProperty(sysEnvMeasurementHost),
                        Integer.parseInt(System.getProperty(sysEnvMeasurementPort))));
    }
}
