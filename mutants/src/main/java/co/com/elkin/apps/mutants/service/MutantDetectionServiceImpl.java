package co.com.elkin.apps.mutants.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import co.com.elkin.apps.mutants.dto.HumanDTO;
import co.com.elkin.apps.mutants.entity.Human;
import co.com.elkin.apps.mutants.exception.APIServiceErrorCodes;
import co.com.elkin.apps.mutants.exception.APIServiceException;
import co.com.elkin.apps.mutants.repository.HumanRepository;
import co.com.elkin.apps.mutants.service.converter.HumanConverterService;
import co.com.elkin.apps.mutants.util.MatrixConverter;

@Service
public class MutantDetectionServiceImpl implements IMutantDetectionService {

	private final HumanRepository humanRepository;
	private final HumanConverterService humanConverterService;

	@Autowired
	public MutantDetectionServiceImpl(final HumanRepository humanRepository,
			final HumanConverterService humanConverterService) {
		this.humanRepository = humanRepository;
		this.humanConverterService = humanConverterService;
	}

	@Override
	public HumanDTO identifyMutant(final HumanDTO human) throws APIServiceException {

		final Optional<Human> optionalHuman = humanRepository.findByDna(human.getDna());
		if (optionalHuman.isPresent() && optionalHuman.get().isHasMutantDna()) {
			return humanConverterService.toDTO(optionalHuman.get());
		} else if (optionalHuman.isPresent() && !optionalHuman.get().isHasMutantDna()) {
			throw new APIServiceException(HttpStatus.FORBIDDEN.getReasonPhrase(),
					APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION);
		}

		final MatrixConverter converter = new MatrixConverter(human.getDna());

		int mutationsCount = 0;
		mutationsCount = countMutationsInMatrix(converter.getHorizontal(), mutationsCount);
		mutationsCount = countMutationsInMatrix(converter.getVertical(), mutationsCount);

		proceedIfNotMutant(human, mutationsCount);

		return proceedIfMutant(human);
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

	private int countMutationsInMatrix(final char[][] matrix, int mutationsCount) {
		for (final char[] line : matrix) {
			mutationsCount += countMutations(line);
		}
		return mutationsCount;
	}

	private void proceedIfNotMutant(final HumanDTO human, final int mutationsCount) throws APIServiceException {
		if (mutationsCount < 2) {
			human.setHasMutantDna(false);
			humanRepository.save(humanConverterService.toEntity(human));
			throw new APIServiceException(HttpStatus.FORBIDDEN.getReasonPhrase(),
					APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION);
		}
	}

	private HumanDTO proceedIfMutant(final HumanDTO human) {
		human.setHasMutantDna(true);
		final Human savedHuman = humanRepository.save(humanConverterService.toEntity(human));
		return humanConverterService.toDTO(savedHuman);
	}

}
