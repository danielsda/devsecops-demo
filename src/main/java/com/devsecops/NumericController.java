package com.devsecops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class NumericController {

	private static final Logger LOGGER = LoggerFactory.getLogger(NumericController.class);
	private static final String BASE_URL = "http://node-service:5000/plusone";
	
	private final RestTemplate restTemplate = new RestTemplate();

	@GetMapping("/")
	public String welcome() {
		return "Kubernetes DevSecOps";
	}

	@GetMapping("/compare/{value}")
	public String compareToFifty(@PathVariable int value) {
		String message;
		if (value > 50) {
			message = "Greater than 50";
		} else {
			message = "Smaller than or equal to 50";
		}
		return message;
	}

	@GetMapping("/increment/{value}")
	public int increment(@PathVariable int value) {
		var responseEntity = restTemplate.getForEntity(BASE_URL + '/' + value, String.class);
		var response = responseEntity.getBody();
		LOGGER.info("Value Received in Request - {}", value);
		LOGGER.info("Node Service Response - {}",  response);
		return Integer.parseInt(response);
	}

}