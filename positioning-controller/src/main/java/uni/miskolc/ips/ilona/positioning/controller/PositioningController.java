package uni.miskolc.ips.ilona.positioning.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;

@Controller
public class PositioningController {

	private static final Logger LOG = LogManager
			.getLogger(PositioningController.class);

	@Autowired
	private PositioningService positioningService;

	@RequestMapping("/getLocation")
	@ResponseBody
	public Position getLocation(@RequestBody Measurement meas) {
		LOG.info(String.format("Called with parameters: %s", meas.toString()));
		Position result = null;

		result = positioningService.determinePosition(meas);
		LOG.info(String.format("Location estimated for %s as %s", meas, result
				.getZone().getName()));
		return result;
	}

	@RequestMapping("/positioningSetup/{algorithm}")
	public ModelAndView loadPositioningSetupPages(@PathVariable String algorithm) {
		ModelAndView view = new ModelAndView(algorithm);

		return view;
	}

	// @RequestMapping("/positioningSetup/setupKNN")
	// @ResponseBody
	// public boolean setupKNNPositioningService(@RequestParam("k") int k) {
	// KNNPositioning service = (KNNPositioning) this.positioningService;
	// service.setK(k);
	// // this.positioningService = service;
	// return true;
	// }
}
