package uni.miskolc.ips.ilona.positioning.service.gateway;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@Configuration
@ComponentScan
@EnableIntegration
@IntegrationComponentScan("uni.miskolc.ips.ilona.positioning.service.gateway")
@MessageEndpoint
public class TrackingGatewaySIConfig {
    @Autowired
    private Environment env;

    private static final Logger LOG = LogManager.getLogger(TrackingGatewaySIConfig.class);

    @Router(inputChannel = "trackingQueryRequestChannel")
    public String route(@Header(value = "METHOD_NAME") String methodname) {
        if (methodname.equals("addHistory")) {
            return "addHistoryQueryChannel";
        }

        return "stdErrChannel";
    }

    @Bean
    public MessageChannel stdErrChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel addHistoryQueryChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel addHistoryReplyChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "stdErrChannel", autoStartup = "true")
    public CharacterStreamWritingMessageHandler logwriter00() {
        LOG.error("Invalid gateway name in TrackingGatewaySIConfig");
        return new CharacterStreamWritingMessageHandler(new BufferedWriter(new OutputStreamWriter(System.err)));
    }

    @Bean
    @ServiceActivator(inputChannel = "addHistoryQueryChannel")
    public HttpRequestExecutingMessageHandler addHistoryGateway() {
        HttpRequestExecutingMessageHandler gateway = new HttpRequestExecutingMessageHandler("http://" + System.getProperty("tracking.host") + ":" + System.getProperty("tracking.port") + "/history/addHistory");
        gateway.setHttpMethod(HttpMethod.GET);
        gateway.setOutputChannel(addHistoryReplyChannel());
        LOG.info("History added to tracking using spring integration");
        return gateway;
    }

}
