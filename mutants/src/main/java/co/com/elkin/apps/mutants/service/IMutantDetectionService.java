package co.com.elkin.apps.mutants.service;

import co.com.elkin.apps.mutants.dto.HumanDTO;
import co.com.elkin.apps.mutants.exception.APIServiceException;

public interface IMutantDetectionService {

	public HumanDTO identifyMutant(final HumanDTO human) throws APIServiceException;

}
