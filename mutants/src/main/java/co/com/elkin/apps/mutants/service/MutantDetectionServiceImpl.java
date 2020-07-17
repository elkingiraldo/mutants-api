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

	private static final int MUTANT_THRESHOLD = 2;

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

		validateInputDna(human);

		final Optional<Human> optionalHuman = humanRepository.findByDna(human.getDna());
		if (optionalHuman.isPresent()) {
			return humanAlreadyAnalized(optionalHuman.get());
		}

		return traverseMatrixAllDirections(human);
	}

	private void validateInputDna(final HumanDTO human) throws APIServiceException {
		if (human.getDna().length < 4) {
			handleNotMutant(human);
		}
	}

	private HumanDTO humanAlreadyAnalized(final Human human) throws APIServiceException {
		if (human.isMutantDna()) {
			return humanConverterService.toDTO(human);
		} else {
			throw new APIServiceException(HttpStatus.FORBIDDEN.getReasonPhrase(),
					APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION);
		}
	}

	private HumanDTO traverseMatrixAllDirections(final HumanDTO human) throws APIServiceException {

		int mutationsCount = 0;
		final char[][] charMatrix = new char[human.getDna().length][human.getDna().length];

		mutationsCount += countMutationsHorizontally(charMatrix, human.getDna());
		if (foundMutant(mutationsCount)) {
			return handleMutant(human);
		}

		mutationsCount += countMutationsVertically(charMatrix);
		if (foundMutant(mutationsCount)) {
			return handleMutant(human);
		}

		mutationsCount += countMutationsDiagonallyDesc(charMatrix);
		if (foundMutant(mutationsCount)) {
			return handleMutant(human);
		}

		mutationsCount += countMutationsDiagonallyAsc(charMatrix);
		if (!foundMutant(mutationsCount)) {
			handleNotMutant(human);
		}

		return handleMutant(human);
	}

	private boolean foundMutant(final int mutationsCount) {
		return mutationsCount >= MUTANT_THRESHOLD;
	}

	private int countMutationsHorizontally(final char[][] matrix, final String[] dna) {
		int mutationsCount = 0;

		for (int i = 0; i < dna.length && mutationsCount < MUTANT_THRESHOLD; i++) {
			final char[] line = dna[i].toCharArray();
			mutationsCount += countMutations(line);
			matrix[i] = line;
		}

		return mutationsCount;
	}

	private int countMutationsVertically(final char[][] matrix) {
		int mutationsCount = 0;

		for (int i = 0; i < matrix.length && mutationsCount < MUTANT_THRESHOLD; i++) {

			final char[] line = new char[matrix.length];

			for (int j = 0; j < matrix.length; j++) {
				line[j] = matrix[j][i];
			}

			mutationsCount += countMutations(line);
		}

		return mutationsCount;
	}

	private int countMutationsDiagonallyDesc(final char[][] matrix) {
		int mutationsCount = 0;

		for (int i = matrix.length - 4; i > 0 && mutationsCount < MUTANT_THRESHOLD; i--) {

			final char[] line = new char[matrix.length];

			for (int j = 0, x = i; x <= matrix.length - 1; j++, x++) {
				line[j] = matrix[x][j];
			}

			mutationsCount += countMutations(line);
		}

		for (int i = 0; i <= matrix.length - 4 && mutationsCount < MUTANT_THRESHOLD; i++) {

			final char[] line = new char[matrix.length];

			for (int j = 0, y = i; y <= matrix.length - 1; j++, y++) {
				line[j] = matrix[j][y];
			}

			mutationsCount += countMutations(line);
		}

		return mutationsCount;
	}

	private int countMutationsDiagonallyAsc(final char[][] matrix) {
		int mutationsCount = 0;

		for (int i = 3; i < matrix.length && mutationsCount < MUTANT_THRESHOLD; i++) {

			final char[] line = new char[matrix.length];

			for (int j = 0; j <= i; j++) {
				line[j] = matrix[i - j][j];
			}

			mutationsCount += countMutations(line);
		}

		for (int i = 0; i < matrix.length - 4 && mutationsCount < MUTANT_THRESHOLD; i++) {

			final char[] line = new char[matrix.length];

			for (int j = 0; j < matrix.length - i - 1; j++) {
				line[j] = matrix[matrix.length - j - 1][j + i + 1];
			}

			mutationsCount += countMutations(line);
		}

		return mutationsCount;
	}

	private int countMutations(final char[] line) {
		int countMutants = 0;
		int countEqualsDna = 1;

		for (int i = 0; i < line.length - 1 && countMutants < MUTANT_THRESHOLD; i++) {
			if (line[i] == line[i + 1] && line[i] != '\0') {
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

	private HumanDTO handleMutant(final HumanDTO human) {
		human.setMutantDna(true);
		final Human savedHuman = humanRepository.save(humanConverterService.toEntity(human));
		return humanConverterService.toDTO(savedHuman);
	}

	private void handleNotMutant(final HumanDTO human) throws APIServiceException {
		human.setMutantDna(false);
		humanRepository.save(humanConverterService.toEntity(human));
		throw new APIServiceException(HttpStatus.FORBIDDEN.getReasonPhrase(),
				APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION);
	}

}
