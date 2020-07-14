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
import co.com.elkin.apps.mutants.service.IMutantService;

@RestController
@CrossOrigin
@RequestMapping("/mutant")
public class MutantController {

	private final IMutantService mutantService;

	@Autowired
	public MutantController(final IMutantService mutantService) {
		super();
		this.mutantService = mutantService;
	}

	@PostMapping()
	public ResponseEntity<HumanDTO> create(@RequestBody final HumanDTO human,
			@RequestHeader(value = "locale", required = false) final String locale) throws APIServiceException {

		final HumanDTO humanResponse = mutantService.validateIfMutant(human);

		return new ResponseEntity<>(humanResponse, HttpStatus.OK);
	}

}
