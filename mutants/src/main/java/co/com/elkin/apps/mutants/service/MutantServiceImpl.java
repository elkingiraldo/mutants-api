package co.com.elkin.apps.mutants.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.com.elkin.apps.mutants.dto.HumanDTO;
import co.com.elkin.apps.mutants.exception.APIServiceErrorCodes;
import co.com.elkin.apps.mutants.exception.APIServiceException;
import co.com.elkin.apps.mutants.util.MatrixConverter;

@Service
public class MutantServiceImpl implements IMutantService {

	@Override
	public HumanDTO validateIfMutant(final HumanDTO human) throws APIServiceException {
		int mutationsCount = 0;
		final MatrixConverter converter = new MatrixConverter(human.getDna());

		for (final char[] line : converter.getHorizontal()) {
			mutationsCount += countMutations(line);
		}

		for (final char[] line : converter.getVertical()) {
			mutationsCount += countMutations(line);
		}
		
		if (mutationsCount < 2) {
			throw new APIServiceException(HttpStatus.FORBIDDEN.getReasonPhrase(), APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION);
		}

		human.setMutant(true);
		return human;
	}

	private int countMutations(final char[] dna) {
		int countMutants = 0;
		int countEqualsDna = 1;

		for (int i = 0; i < dna.length - 1; i++) {
			if (dna[i] == dna[i + 1]) {
				countEqualsDna++;
				if (countEqualsDna == 4) {
					countMutants++;
				}
			} else {
				countEqualsDna = 1;
			}
		}

		return countMutants;
	}

}
