package co.com.elkin.apps.mutants.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/health")
public class HealthCheckController {

	@GetMapping("/status")
	public ResponseEntity<String> checkStatus() {
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}

}
