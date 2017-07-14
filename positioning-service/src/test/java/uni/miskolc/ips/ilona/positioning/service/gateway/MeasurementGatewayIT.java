package uni.miskolc.ips.ilona.positioning.service.gateway;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;

import java.util.Collection;

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;

/**
 * Created by tamas on 2017.07.12..
 */
public class MeasurementGatewayIT {

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
                String.format("%s:%d/ilona/resources/listMeasurements",
                        System.getProperty(sysEnvMeasurementHost),
                        Integer.parseInt(System.getProperty(sysEnvMeasurementPort))));
        MeasurementGateway measurementGateway = context.getBean("MeasurementGateway", MeasurementGateway.class);
        Collection<Measurement> result = measurementGateway.listMeasurements();
        Assert.assertNotEquals(0,result.size());



    }

}
