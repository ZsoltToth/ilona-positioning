package uni.miskolc.ips.ilona.positioning.service.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.*;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.integration.stream.CharacterStreamWritingMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import uni.miskolc.ips.ilona.measurement.controller.dto.ZoneDTO;
import uni.miskolc.ips.ilona.measurement.model.position.Zone;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;


@Configuration
@ComponentScan
@EnableIntegration
@IntegrationComponentScan("uni.miskolc.ips.ilona.positioning.service.gateway")
@MessageEndpoint
public class ZoneQueryServiceSIConfig {

    @Autowired
    private Environment env;

    @Router(inputChannel = "zoneQueryRequestChannel")
    public String route(@Header(value = "METHOD_NAME") String methodname){
        if (methodname.equals("listZones"))
            return "listZonesQueryChannel";
        else if (methodname.equals("getZone")) {
            return "getZoneQueryChannel";
        }
        return "stdErrChannel";
    }

    @Bean
    public MessageChannel zoneQueryRequestChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel zoneQueryChannel(){
        return new DirectChannel();
    }


    @Bean
    public MessageChannel listZonesQueryChannel(){
        return new DirectChannel();
    }


    @Bean
    public MessageChannel getZoneQueryChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel stdErrChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel listZonesReplyChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel getZoneReplyChannel(){
        return new DirectChannel();
    }


    /**
     * TODO std error output
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "stdErrChannel", autoStartup = "true")
    public CharacterStreamWritingMessageHandler logwriter00() {
        return new CharacterStreamWritingMessageHandler(new BufferedWriter(new OutputStreamWriter(System.err)));
    }


    @Bean
    @ServiceActivator(inputChannel = "getZoneQueryChannel")
    public HttpRequestExecutingMessageHandler httpGateway() {

        SpelExpressionParser expressionParser = new SpelExpressionParser();
        Map<String, Expression> uriVariableExpressions = new HashMap<>(1);
        uriVariableExpressions.put("zoneID", expressionParser.parseExpression("headers['zoneID']"));
       HttpRequestExecutingMessageHandler gateway = new HttpRequestExecutingMessageHandler("http://"+System.getProperty("measurement.host")+":"+System.getProperty("measurement.port")+"/zones/{zoneID}");
//       HttpRequestExecutingMessageHandler gateway = new HttpRequestExecutingMessageHandler("http://localhost:8081/zones/183f0204-5029-4b33-a128-404ba5c68fa8");
       gateway.setUriVariableExpressions(uriVariableExpressions);
        gateway.setHttpMethod(HttpMethod.GET);
        gateway.setExpectedResponseType(ZoneDTO.class);
        gateway.setOutputChannel(getZoneReplyChannel());
        return gateway;
    }







}
