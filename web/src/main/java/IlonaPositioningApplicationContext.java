package uni.miskolc.ips.ilona.positioning.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import uni.miskolc.ips.ilona.measurement.model.measurement.MeasurementDistanceCalculator;
import uni.miskolc.ips.ilona.measurement.model.measurement.MeasurementDistanceCalculatorImpl;
import uni.miskolc.ips.ilona.measurement.model.measurement.wifi.VectorIntersectionWiFiRSSIDistance;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;
import uni.miskolc.ips.ilona.positioning.service.gateway.MeasurementGateway;
import uni.miskolc.ips.ilona.positioning.service.gateway.MeasurementGatewaySIConfig;
import uni.miskolc.ips.ilona.positioning.service.impl.classification.bayes.NaiveBayesPositioningService;
import uni.miskolc.ips.ilona.positioning.service.impl.knn.KNNPositioning;
import uni.miskolc.ips.ilona.positioning.service.impl.knn.KNNSimplePositioning;
import uni.miskolc.ips.ilona.positioning.service.impl.knn.KNNWeightedPositioning;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "uni.miskolc.ips.ilona.positioning.controller")
public class IlonaPositioningApplicationContext extends WebMvcConfigurerAdapter {

    @Bean
    public VectorIntersectionWiFiRSSIDistance wifiDistanceCalculator() {
        return new VectorIntersectionWiFiRSSIDistance();
    }

    @Bean
    public MeasurementDistanceCalculator measurementDistanceCalculator() {
        //TODO
        double wifiDistanceWeight = 1.0;
        double magnetometerDistanceWeight = 0.5;
        double gpsDistanceWeight = 0.0;
        double bluetoothDistanceWeight = 1.0;
        double rfidDistanceWeight = 0.0;

        return new MeasurementDistanceCalculatorImpl(wifiDistanceCalculator(), wifiDistanceWeight, magnetometerDistanceWeight, gpsDistanceWeight, bluetoothDistanceWeight, rfidDistanceWeight);
    }

    @Bean
    public KNNPositioning positioningService() {
        //TODO
        MeasurementDistanceCalculator distanceCalculator = measurementDistanceCalculator();
        ApplicationContext context = new AnnotationConfigApplicationContext(MeasurementGatewaySIConfig.class);
        MeasurementGateway measurementGateway = context.getBean("MeasurementQueryGateway", MeasurementGateway.class);
        int k = 3;
        return new KNNSimplePositioning(distanceCalculator, measurementGateway, k);

    }

    @Bean
    public KNNPositioning knnpositioningService() {
        //TODO
/*        MeasurementDistanceCalculator distanceCalculator = measurementDistanceCalculator();
        MeasurementGateway measurementGateway = null;
        int k = 3;
        return new KNNSimplePositioning(distanceCalculator, measurementGateway, k);
  */
        return positioningService();
    }

    @Bean
    public KNNPositioning knnWpositioningService() {
        //TODO
        MeasurementDistanceCalculator distanceCalculator = measurementDistanceCalculator();
        ApplicationContext context = new AnnotationConfigApplicationContext(MeasurementGatewaySIConfig.class);
        MeasurementGateway measurementGateway = context.getBean("MeasurementQueryGateway", MeasurementGateway.class);
        int k = 3;
        return new KNNWeightedPositioning(distanceCalculator, measurementGateway, k);

    }

    @Bean
    public PositioningService naivebayespositioningService() {
        //TODO
        int maxMeasurementDistance = 50;
        ApplicationContext context = new AnnotationConfigApplicationContext(MeasurementGatewaySIConfig.class);
        MeasurementGateway measurementGateway = context.getBean("MeasurementQueryGateway", MeasurementGateway.class);
        MeasurementDistanceCalculator measDistanceCalculator = measurementDistanceCalculator();
        return new NaiveBayesPositioningService(measurementGateway, measDistanceCalculator, maxMeasurementDistance);

    }

    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setPrefix("/WEB-INF/views/");
        bean.setSuffix(".jsp");
        return bean;
    }


    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/img/**").addResourceLocations("/img/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("/fonts/");

    }


}
