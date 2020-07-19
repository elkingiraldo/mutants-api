package co.com.elkin.apps.mutants.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.elkin.apps.mutants.dto.StatisticsDTO;
import co.com.elkin.apps.mutants.service.IStatisticsService;

/**
 * Controller for handling requests related with statistics
 * 
 * @author elkin.giraldo
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/stats")
public class StatisticsController {

	private final IStatisticsService statisticsService;

	@Autowired
	public StatisticsController(final IStatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	/**
	 * This method takes GET API request for handling and passing it to correct
	 * service application layer for searching statistics of the mutants, humans and
	 * ratio of mutants found
	 * 
	 * @return {@link StatisticsDTO}
	 */
	@GetMapping
	public ResponseEntity<StatisticsDTO> statistics() {
		final StatisticsDTO statistics = statisticsService.obtainStatistics();
		return new ResponseEntity<>(statistics, HttpStatus.OK);
	}

}
