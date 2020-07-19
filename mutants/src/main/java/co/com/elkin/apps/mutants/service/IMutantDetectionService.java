package co.com.elkin.apps.mutants.service;

import co.com.elkin.apps.mutants.dto.HumanDTO;
import co.com.elkin.apps.mutants.exception.APIServiceException;

/**
 * Interface that exposes methods for identifying mutants DNA
 * 
 * @author elkin.giraldo
 *
 */
public interface IMutantDetectionService {

	/**
	 * Method that takes the request for handling the response whether a human has
	 * mutant DNA or not.
	 * 
	 * @param human, human with the DNA to be analyzed
	 * @return {@link HumanDTO}, Human saved in DB
	 * @throws APIServiceException when something was wrong during the process or
	 *                             the human analyzed doesn't have mutant DNA
	 */
	public HumanDTO identifyMutant(final HumanDTO human) throws APIServiceException;

}
