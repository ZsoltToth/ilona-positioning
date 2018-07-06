package uni.miskolc.ips.ilona.positioning.service.impl;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uni.miskolc.ips.ilona.measurement.controller.dto.ZoneDTO;
import uni.miskolc.ips.ilona.positioning.service.App;
import uni.miskolc.ips.ilona.positioning.service.gateway.ZoneQueryService;
import uni.miskolc.ips.ilona.positioning.service.gateway.ZoneQueryServiceSIConfig;


public class IntegrationTest {


    @Test
    public void testZoneConnection(){

        ApplicationContext context=new AnnotationConfigApplicationContext(ZoneQueryServiceSIConfig.class);

        ZoneQueryService zoneQueryService= context.getBean("ZoneQueryGateway", ZoneQueryService.class);


        String zoneDTO=zoneQueryService.getZoneById("183f0204-5029-4b33-a128-404ba5c68fa8").getName();
        System.out.println(zoneDTO);
        zoneDTO=zoneQueryService.getZoneById("183f0204-5029-4b33-a128-404ba5c68fa").getName();
        System.out.println(zoneDTO);
        zoneDTO=zoneQueryService.getZoneById("743d2365-2eaa-412f-8324-6b6b1361ba5b").getName();


        System.out.println(zoneDTO);
    }
}
