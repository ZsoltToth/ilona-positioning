package uni.miskolc.ips.ilona.positioning.service.impl.svm;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;
import uni.miskolc.ips.ilona.positioning.model.svm.SupportVectorMachine;
import uni.miskolc.ips.ilona.positioning.service.gateway.ZoneQueryService;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;

/**
 * Created by tamas on 2017.07.17..
 */
public class SVMIT {
    private static final String sysEnvMeasurementHost = "measurement.host";
    private static final String sysEnvMeasurementPort = "measurement.port";

    private static final String siConfigLocation = "/si-config-measurement.xml";

    private ApplicationContext context;
    private ZoneQueryService zoneGateway;

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
        this.context = new ClassPathXmlApplicationContext(siConfigLocation);
        zoneGateway = context.getBean("ZoneGateway", ZoneQueryService.class);
    }


    private Measurement instanceFromJSON() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\r\n \"id\":\"aa7b48e2-ab67-4888-b5c9-1edc21c13d63\",\r\n   \"timestamp\":1456588248000,\r\n"
                + "\"position\":{\r\n\"coordinate\":{\r\n\"x\":23.0,\r\n\"y\":8.0,\r\n\"z\":4.4\r\n  "
                + "},\r\n      \"zone\":{\r\n\"id\":\"07a25de0-a013-486d-9463-404a348e05ee\",\r\n        "
                + "\"name\":\"1st Floor East Corridor\"\r\n},\r\n\"uuid\":\"11a89987-bc93-411b-9d75-af3c5a7312da\"\r\n},\r\n  "
                + "\"wifiRSSI\":{\r\n\"rssiValues\":{\r\n\"dd\":-81.0,\r\n\"aut-sams-1\":-79.0,\r\n       "
                + "\"doa208\":-75.0,\r\n\"TP-LINK_B2765A\":-72.0,\r\n\"IITAP3\":-86.0,\r\n\"doa200\":-85.0,\r\n\"GEIAKFSZ\""
                + ":-79.0,\r\n\"IITAP2\":-82.0,\r\n\"IITAP2-GUEST\":-81.0,\r\n\"KRZ\":-85.0,\r\n\"library114\":-86.0,\r\n\"IITAP1\":-74.0,\r\n "
                + "\"IITAP3-GUEST\":-86.0,\r\n\"109\":-65.0,\r\n\"IITAP1-GUEST\":-82.0\r\n}\r\n},\r\n \"magnetometer\":{\r\n "
                + "\"xAxis\":-0.5067219138145447,\r\n\"yAxis\":-0.964530348777771,\r\n\"zAxis\":0.055498506873846054,\r\n\"radian\":0.0\r\n},"
                + "\r\n\"bluetoothTags\":{\r\n\"tags\":[\r\n\"EV3 00:16:53:4C:FA:60\",\r\n\"IZE 00:16:53:4C:B1:F9\",\r\n"
                + "\"EV3BD 00:16:53:4C:F5:2D\",\r\n\"EV3 00:16:53:4C:FA:67\",\r\n\"JOE 00:16:53:4C:F9:A4\",\r\n\"DANI 6B:C2:26:12:62:60\",\r\n"
                + "\"EV3 00:16:53:4C:F2:6A\",\r\n\"MrEv3 00:16:53:4C:B4:EB\"\r\n]\r\n},\r\n\"gpsCoordinates\":null,\r\n\"rfidtags\":{\r\n"
                + "\"tags\":[\r\n\r\n]\r\n}\r\n}";
        Measurement result = mapper.readValue(json, Measurement.class);
        if (result.getPosition() == null) {
            result.setPosition(new Position(Zone.UNKNOWN_POSITION));
        }
        return result;

    }

    @Test
    public void determinePositionOfJSONTest() throws FileNotFoundException, IOException, Exception {
        SupportVectorMachine svm = new SupportVectorMachine("src/resources/training_set.arff");
        String serializedPath = "src/resources/svm.ser";
        SupportVectorMachine.serializeSupportVectorMachine(svm, serializedPath);
        SVMPositioning svmPositioning = new SVMPositioning(serializedPath, zoneGateway);
        System.out.println(svm.getHeader());
        Measurement measurement = instanceFromJSON();
        Position result = svmPositioning.determinePosition(measurement);
        Assert.assertNotNull(result);

    }


}
