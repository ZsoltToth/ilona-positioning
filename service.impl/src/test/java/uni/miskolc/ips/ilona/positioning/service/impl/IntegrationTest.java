package uni.miskolc.ips.ilona.positioning.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uni.miskolc.ips.ilona.measurement.controller.dto.ZoneDTO;
import uni.miskolc.ips.ilona.positioning.service.gateway.ZoneQueryService;
import uni.miskolc.ips.ilona.positioning.service.gateway.ZoneQueryServiceSIConfig;

import java.util.Collection;


public class IntegrationTest {


    @Test
    public void testZoneConnection() {

        ApplicationContext context = new AnnotationConfigApplicationContext(ZoneQueryServiceSIConfig.class);

        ZoneQueryService zoneQueryService = context.getBean("ZoneQueryGateway", ZoneQueryService.class);


        String zoneDTO = zoneQueryService.getZoneById("183f0204-5029-4b33-a128-404ba5c68fa8").getName();
        Assert.assertTrue(zoneDTO.equals("bedroom"));
        zoneDTO = zoneQueryService.getZoneById("183f0204-5029-4b33-a128-404ba5c68fa").getName();
        Assert.assertTrue(zoneDTO.equals("Unknown"));
        zoneDTO = zoneQueryService.getZoneById("743d2365-2eaa-412f-8324-6b6b1361ba5b").getName();
        Assert.assertTrue(zoneDTO.equals("kitchen"));
    }

    @Test
    public void testListZones() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ZoneQueryServiceSIConfig.class);

        ZoneQueryService zoneQueryService = context.getBean("ZoneQueryGateway", ZoneQueryService.class);
        Collection<ZoneDTO> zoneDTOS = zoneQueryService.listZones();
        System.out.println(zoneDTOS.size());
        for (Object zoneDTO : zoneDTOS) {
            System.out.println(zoneDTO);
        }
    }
}
