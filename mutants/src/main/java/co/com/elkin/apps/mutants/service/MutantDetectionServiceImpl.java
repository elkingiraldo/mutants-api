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

/**
 * Service implementation for analyzing human DNA and finding mutants
 * 
 * @author elkin.giraldo
 *
 */
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

		validateSquareDnaMatrix(human.getDna());
		validateInputDna(human);

		final Optional<Human> optionalHuman = humanRepository.findByDna(human.getDna());
		if (optionalHuman.isPresent()) {
			return humanAlreadyAnalized(optionalHuman.get());
		}

		return traverseMatrixAllDirections(human);
	}

	/**
	 * This method verify if is a valid matrix posted
	 * 
	 * @param dna, human DNA to be analyzed
	 * @throws APIServiceException when no square matrix
	 */
	private void validateSquareDnaMatrix(final String[] dna) throws APIServiceException {
		if (dna == null) {
			throw new APIServiceException("dna null", APIServiceErrorCodes.HUMAN_MATRIX_DNA_SIZE_EXCEPTION);
		}
		if (dna.length == 0) {
			throw new APIServiceException(HttpStatus.BAD_REQUEST.getReasonPhrase(),
					APIServiceErrorCodes.HUMAN_MATRIX_DNA_SIZE_EXCEPTION);
		}
		for (final String line : dna) {
			if (dna.length != line.length()) {
				throw new APIServiceException(HttpStatus.BAD_REQUEST.getReasonPhrase(),
						APIServiceErrorCodes.HUMAN_MATRIX_DNA_SIZE_EXCEPTION);
			}
		}
	}

	/**
	 * Validate the minimum input matrix size for analyzing human DNA
	 * 
	 * @param human, human with the DNA to be analyzed
	 * @throws APIServiceException when the human analyzed doesn't have mutant DNA
	 */
	private void validateInputDna(final HumanDTO human) throws APIServiceException {
		if (human.getDna().length < 4) {
			handleNotMutant(human);
		}
	}

	/**
	 * Validate if the human DNA was already analyzed and returning the correct
	 * response whether has mutant DNA or not.
	 * 
	 * @param human, human with the DNA to be analyzed
	 * @return {@link HumanDTO}, Human found in DB
	 * @throws APIServiceException when the human analyzed doesn't have mutant DNA
	 */
	private HumanDTO humanAlreadyAnalized(final Human human) throws APIServiceException {
		if (human.isMutantDna()) {
			return humanConverterService.toDTO(human);
		} else {
			throw new APIServiceException(HttpStatus.FORBIDDEN.getReasonPhrase(),
					APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION);
		}
	}

	/**
	 * Traverse matrix in all directions and return the correct response whether the
	 * human has mutant DNA or not.
	 * 
	 * @param human, human with the DNA to be analyzed
	 * @return {@link HumanDTO}, Human saved in DB
	 * @throws APIServiceException when the human analyzed doesn't have mutant DNA
	 */
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

	/**
	 * Method that verify if the mutant was already found because there are more
	 * than a sequence of 4 equal letters.
	 * 
	 * @param mutationsCount, counter with the number of mutations
	 * @return a flag if the mutant was already found.
	 */
	private boolean foundMutant(final int mutationsCount) {
		return mutationsCount >= MUTANT_THRESHOLD;
	}

	/**
	 * Traverse the matrix horizontally for knowing the number of mutations found
	 * 
	 * @param matrix, matrix of char to be analyzed
	 * @param dna,    to be analized
	 * @return the number of mutations found
	 */
	int countMutationsHorizontally(final char[][] matrix, final String[] dna) {
		int mutationsCount = 0;

		for (int i = 0; i < dna.length && mutationsCount < MUTANT_THRESHOLD; i++) {
			final char[] line = dna[i].toCharArray();
			mutationsCount += countMutations(line);
			matrix[i] = line;
		}

		return mutationsCount;
	}

	/**
	 * Traverse the matrix vertically for knowing the number of mutations found
	 * 
	 * @param matrix, matrix of char to be analyzed
	 * @return the number of mutations found
	 */
	int countMutationsVertically(final char[][] matrix) {
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

	/**
	 * Traverse the matrix diagonally desc for knowing the number of mutations found
	 * 
	 * @param matrix, matrix of char to be analyzed
	 * @return the number of mutations found
	 */
	int countMutationsDiagonallyDesc(final char[][] matrix) {
		int mutationsCount = 0;

		/**
		 * First half starting four positions above to the bottom left corner, without
		 * including the diagonal
		 */
		for (int i = matrix.length - 4; i > 0 && mutationsCount < MUTANT_THRESHOLD; i--) {

			final char[] line = new char[matrix.length];

			for (int j = 0, x = i; x <= matrix.length - 1; j++, x++) {
				line[j] = matrix[x][j];
			}

			mutationsCount += countMutations(line);
		}

		/**
		 * Second half finishing four positions before top right corner, including the
		 * diagonal
		 */
		for (int i = 0; i <= matrix.length - 4 && mutationsCount < MUTANT_THRESHOLD; i++) {

			final char[] line = new char[matrix.length];

			for (int j = 0, y = i; y <= matrix.length - 1; j++, y++) {
				line[j] = matrix[j][y];
			}

			mutationsCount += countMutations(line);
		}

		return mutationsCount;
	}

	/**
	 * Traverse the matrix diagonally asc for knowing the number of mutations found
	 * 
	 * @param matrix, matrix of char to be analyzed
	 * @return the number of mutations found
	 */
	int countMutationsDiagonallyAsc(final char[][] matrix) {
		int mutationsCount = 0;

		/**
		 * First half starting four positions below to the top left corner, including
		 * the diagonal
		 */
		for (int i = 3; i < matrix.length && mutationsCount < MUTANT_THRESHOLD; i++) {

			final char[] line = new char[matrix.length];

			for (int j = 0; j <= i; j++) {
				line[j] = matrix[i - j][j];
			}

			mutationsCount += countMutations(line);
		}

		/**
		 * Second half finishing four positions before bottom right corner, without
		 * including the diagonal
		 */
		for (int i = 0; i < matrix.length - 4 && mutationsCount < MUTANT_THRESHOLD; i++) {

			final char[] line = new char[matrix.length];

			for (int j = 0; j < matrix.length - i - 1; j++) {
				line[j] = matrix[matrix.length - j - 1][j + i + 1];
			}

			mutationsCount += countMutations(line);
		}

		return mutationsCount;
	}

	/**
	 * Count mutations in a line of char
	 * 
	 * @param line, line to be anlyzed
	 * @return the number of mutations found
	 */
	int countMutations(final char[] line) {
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

	/**
	 * This method saves the human saying that he's a mutant and returning the
	 * correspond Human saved.
	 * 
	 * @param human, human with the DNA to be analyzed
	 * @return {@link HumanDTO} saved in DB
	 */
	private HumanDTO handleMutant(final HumanDTO human) {
		human.setMutantDna(true);
		final Human savedHuman = humanRepository.save(humanConverterService.toEntity(human));
		return humanConverterService.toDTO(savedHuman);
	}

	/**
	 * This method saves the human saying that he isn't a mutant and throws the
	 * correspond exception
	 * 
	 * @param human, human with the DNA to be analyzed
	 * @throws APIServiceException when the human analyzed doesn't have mutant DNA
	 */
	private void handleNotMutant(final HumanDTO human) throws APIServiceException {
		human.setMutantDna(false);
		humanRepository.save(humanConverterService.toEntity(human));
		throw new APIServiceException(HttpStatus.FORBIDDEN.getReasonPhrase(),
				APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION);
	}

}
