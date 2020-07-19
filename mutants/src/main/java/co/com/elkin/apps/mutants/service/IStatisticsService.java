package co.com.elkin.apps.mutants.service;

import co.com.elkin.apps.mutants.dto.StatisticsDTO;

/**
 * Interface that exposes methods for obtain statistics
 * 
 * @author elkin.giraldo
 *
 */
public interface IStatisticsService {

	/**
	 * Method that takes the request for building the response with the statistics
	 * of mutants, humans and ratio of mutants.
	 * 
	 * @return {@link StatisticsDTO}
	 */
	public StatisticsDTO obtainStatistics();

}
