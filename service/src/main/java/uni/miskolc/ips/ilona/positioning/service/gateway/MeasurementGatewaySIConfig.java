package uni.miskolc.ips.ilona.positioning.service.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.integration.stream.CharacterStreamWritingMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Collection;

@Configuration
@ComponentScan
@EnableIntegration
@IntegrationComponentScan("uni.miskolc.ips.ilona.positioning.service.gateway")
@MessageEndpoint
public class MeasurementGatewaySIConfig {

    @Autowired
    private Environment env;

    @Router(inputChannel = "measurementQueryRequestChannel")
    public String route(@Header(value = "METHOD_NAME") String methodname) {
        if (methodname.equals("listMeasurement")) {
            return "listMeasurementQueryChannel";
        }

        return "stdErrChannel";
    }

    @Bean
    public MessageChannel stdErrChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel listMeasurementQueryChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel listMeasurementReplyChannel() {
        return new DirectChannel();
    }

    /**
     * TODO std error output
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "stdErrChannel", autoStartup = "true")
    public CharacterStreamWritingMessageHandler logwriter00() {
        return new CharacterStreamWritingMessageHandler(new BufferedWriter(new OutputStreamWriter(System.err)));
    }

    @Bean
    @ServiceActivator(inputChannel = "listMeasurementQueryChannel")
    public HttpRequestExecutingMessageHandler listGateway() {


        HttpRequestExecutingMessageHandler gateway = new HttpRequestExecutingMessageHandler("http://" + System.getProperty("measurement.host") + ":" + System.getProperty("measurement.port") + "/resources/listMeasurements");
        gateway.setHttpMethod(HttpMethod.GET);
        gateway.setExpectedResponseType(Collection.class);
        gateway.setOutputChannel(listMeasurementReplyChannel());
        return gateway;
    }

}
