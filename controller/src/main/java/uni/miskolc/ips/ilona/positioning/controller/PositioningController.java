package uni.miskolc.ips.ilona.positioning.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ContextLoader;
import uni.miskolc.ips.ilona.measurement.controller.dto.MeasurementDTO;
import uni.miskolc.ips.ilona.measurement.controller.dto.PositionDTO;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.positioning.exceptions.InvalidMeasurementException;
import uni.miskolc.ips.ilona.positioning.exceptions.PositioningFailureException;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;

@Controller
public class PositioningController {

    private static final Logger LOG = LogManager
            .getLogger(PositioningController.class);

    @Autowired
    private PositioningService positioningService;

	/*@RequestMapping("/getLocation")
	@ResponseBody
	public Position getLocation(@RequestBody Measurement meas) throws InvalidMeasurementException, PositioningFailureException {
		LOG.info(String.format("Called with parameters: %s", meas.toString()));
		Position result = null;
		try{
			result = positioningService.determinePosition(meas);
			LOG.info(String.format("Location estimated for %s as %s", meas, result
				.getZone().getName()));
		}
		catch (InvalidMeasurementException e) {
			result= new Position(new Zone("Invalid Measurement"));
		}
		catch (PositioningFailureException e) {
			System.out.println(meas);
			result= new Position(new Zone("Positioning Failure"));
		}
		catch (Exception e) {
			result= new Position(new Zone("Failure"));
		}
		return result;
	}*/

    @RequestMapping("/")
    public String index() {
        return "index";
    }


    @RequestMapping("/getLocation")
    @ResponseBody
    public PositionDTO getLocation(@RequestBody MeasurementDTO meas) throws InvalidMeasurementException, PositioningFailureException {
        LOG.info(String.format("Called with parameters: %s", meas.toString()));
        Position position = null;
        System.out.println(meas);
        position = positioningService.determinePosition(MeasurementDTOConverter.convertToMeasurement(meas));
        LOG.info(String.format("Location estimated for %s as %s", meas, position));
        System.out.println("asd");
        return PositionDTOConverter.convertToPositionDTO(position);
    }


    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED,
            reason = "Invalid or not measurement")
    @ExceptionHandler(InvalidMeasurementException.class)
    public void invalidMeasurement() {
        LOG.error("Invalid Measurement occurred during positioning");
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY,
            reason = "The positioning can not be performed due to the measurement")
    @ExceptionHandler(PositioningFailureException.class)
    public void unProcessable() {
        LOG.error("Positioning Failure Exception in PositioningController, The positioning can not be performed due to the measurement");
    }


    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
            reason = "The k value is higher than the number of samples")
    @ExceptionHandler(IllegalArgumentException.class)
    public void illegalKValue() {
        LOG.error("The k value is higher than the number of samples");

    }


    @RequestMapping("/positioningSetup/{algorithm}")
    public String loadPositioningSetupPages(@PathVariable String algorithm) {
        ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        PositioningService newService;
        System.out.println("old: " + positioningService);
        switch (algorithm) {
            case "knnW":

                newService = (PositioningService) context.getBean("knnWpositioningService");
                this.positioningService = newService;
                LOG.info("Positioning Service changed to" + algorithm);
                break;
            case "knn":
                newService = (PositioningService) context.getBean("knnpositioningService");
                this.positioningService = newService;
                LOG.info("Positioning Service changed to" + algorithm);
                break;

            case "neuralnetwork":
                newService = (PositioningService) context.getBean("nnpositioningService");
                this.positioningService = newService;
                LOG.info("Positioning Service changed to" + algorithm);
                break;
            case "naivebayes":
                newService = (PositioningService) context.getBean("naivebayespositioningService");
                this.positioningService = newService;
                LOG.info("Positioning Service changed to" + algorithm);
                break;
            default:
                System.out.println("No corresponding algorithm");
                LOG.warn("Positioning Service could not be changed to " + algorithm);
                break;
        }
        System.out.println(positioningService);

        return "redirect:/";
    }


}
