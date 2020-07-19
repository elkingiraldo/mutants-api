package co.com.elkin.apps.mutants.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.elkin.apps.mutants.dto.HumanDTO;
import co.com.elkin.apps.mutants.exception.APIServiceException;
import co.com.elkin.apps.mutants.service.IMutantDetectionService;

/**
 * Controller for handling requests related with mutants detection
 * 
 * @author elkin.giraldo
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/mutant")
public class MutantDetectionController {

	private final IMutantDetectionService mutantDetectionService;

	@Autowired
	public MutantDetectionController(final IMutantDetectionService mutantDetectionService) {
		this.mutantDetectionService = mutantDetectionService;
	}

	/**
	 * This method takes POST API request for handling and passing it to correct
	 * service application layer for verifying DNA of a human
	 * 
	 * @param human,  for being analyzed.
	 * @param locale, language the user wants to use.
	 * @return {@link HumanDTO}, Human saved in DB
	 * @throws APIServiceException when something was wrong during the process or
	 *                             the human analyzed doesn't have mutant DNA
	 */
	@PostMapping()
	public ResponseEntity<HumanDTO> verifyDna(@RequestBody final HumanDTO human,
			@RequestHeader(value = "locale", required = false) final String locale) throws APIServiceException {
		final HumanDTO humanResponse = mutantDetectionService.identifyMutant(human);
		return new ResponseEntity<>(humanResponse, HttpStatus.OK);
	}

}
