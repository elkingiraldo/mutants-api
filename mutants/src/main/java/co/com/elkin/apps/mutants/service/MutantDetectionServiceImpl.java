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

		validateImpossilityToBeMutant(human);

		final Optional<Human> optionalHuman = humanRepository.findByDna(human.getDna());
		if (optionalHuman.isPresent()) {
			return humanAlreadyAnalized(optionalHuman.get());
		}

		int mutationsCount = 0;
		final char[][] charMatrix = new char[human.getDna().length][human.getDna().length];

		mutationsCount = countMutationsHorizontally(charMatrix, human.getDna(), mutationsCount);
		mutationsCount = countMutationsVertically(charMatrix, mutationsCount);
		mutationsCount = countMutationsDiagonallyDesc(charMatrix, mutationsCount);
		mutationsCount = countMutationsDiagonallyAsc(charMatrix, mutationsCount);

		if (mutationsCount <= 2) {
			proceedIfNotMutant(human);
		}

		return proceedIfMutant(human);
	}

	private void validateImpossilityToBeMutant(final HumanDTO human) throws APIServiceException {
		if (human.getDna().length < 4) {
			proceedIfNotMutant(human);
		}
	}

	private HumanDTO humanAlreadyAnalized(final Human human) throws APIServiceException {
		if (human.isHasMutantDna()) {
			return humanConverterService.toDTO(human);
		} else {
			throw new APIServiceException(HttpStatus.FORBIDDEN.getReasonPhrase(),
					APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION);
		}
	}

	private int countMutationsHorizontally(final char[][] matrix, final String[] dna, int mutationsCount) {

		for (int i = 0; i < dna.length; i++) {
			final String line = dna[i];
			mutationsCount += countMutations(line);
			matrix[i] = line.toCharArray();
		}

		return mutationsCount;
	}

	private int countMutationsVertically(final char[][] matrix, int mutationsCount) {

		for (int i = 0; i < matrix.length; i++) {

			final StringBuilder line = new StringBuilder();

			for (int j = 0; j < matrix.length; j++) {
				line.append(matrix[j][i]);
			}

			mutationsCount += countMutations(line.toString());
		}

		return mutationsCount;
	}

	private int countMutationsDiagonallyDesc(final char[][] matrix, int mutationsCount) {

		for (int i = matrix.length - 4; i > 0; i--) {

			final StringBuilder line = new StringBuilder();

			for (int j = 0, x = i; x <= matrix.length - 1; j++, x++) {
				line.append(matrix[x][j]);
			}

			mutationsCount += countMutations(line.toString());
		}

		for (int i = 0; i <= matrix.length - 4; i++) {

			final StringBuilder line = new StringBuilder();

			for (int j = 0, y = i; y <= matrix.length - 1; j++, y++) {
				line.append(matrix[j][y]);
			}

			mutationsCount += countMutations(line.toString());
		}

		return mutationsCount;
	}

	private int countMutationsDiagonallyAsc(final char[][] matrix, int mutationsCount) {

		for (int i = 3; i < matrix.length; i++) {

			final StringBuilder line = new StringBuilder();

			for (int j = 0; j <= i; j++) {
				line.append(matrix[i - j][j]);
			}

			mutationsCount += countMutations(line.toString());
		}

		for (int i = 0; i < matrix.length - 4; i++) {

			final StringBuilder line = new StringBuilder();

			for (int j = 0; j < matrix.length - i - 1; j++) {
				line.append(matrix[matrix.length - j - 1][j + i + 1]);
			}

			mutationsCount += countMutations(line.toString());
		}

		return mutationsCount;
	}

	private int countMutations(final String line) {
		int countMutants = 0;
		int countEqualsDna = 1;

		for (int i = 0; i < line.length() - 1; i++) {
			if (line.charAt(i) == line.charAt(i + 1) && line.charAt(i) != '\0') {
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

	private void proceedIfNotMutant(final HumanDTO human) throws APIServiceException {
		human.setHasMutantDna(false);
		humanRepository.save(humanConverterService.toEntity(human));
		throw new APIServiceException(HttpStatus.FORBIDDEN.getReasonPhrase(),
				APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION);
	}

	private HumanDTO proceedIfMutant(final HumanDTO human) {
		human.setHasMutantDna(true);
		final Human savedHuman = humanRepository.save(humanConverterService.toEntity(human));
		return humanConverterService.toDTO(savedHuman);
	}

}
