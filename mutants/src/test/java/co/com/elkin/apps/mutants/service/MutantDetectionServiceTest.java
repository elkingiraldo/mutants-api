package co.com.elkin.apps.mutants.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import co.com.elkin.apps.mutants.dto.HumanDTO;
import co.com.elkin.apps.mutants.entity.Human;
import co.com.elkin.apps.mutants.exception.APIServiceErrorCodes;
import co.com.elkin.apps.mutants.exception.APIServiceException;
import co.com.elkin.apps.mutants.repository.HumanRepository;
import co.com.elkin.apps.mutants.service.converter.HumanConverterService;

public class MutantDetectionServiceTest {

	@InjectMocks
	private MutantDetectionServiceImpl mutantDetectionServiceImpl;
	@Mock
	private HumanRepository humanRepository;
	@Mock
	private HumanConverterService humanConverterService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void shouldCountZeroMutations() {
		final char[] lineZeroMutations = { 'A', 'A', 'A', 'T', 'A', 'A', 'A', 'T', 'A', 'A' };
		final int countMutations = mutantDetectionServiceImpl.countMutations(lineZeroMutations);
		assertEquals(0, countMutations);
	}

	@Test
	public void shouldExcludeEmptyChar() {
		final char[] lineZeroMutations = { 'A', 'A', 'A', 'A', 'T', '\0', '\0', '\0', '\0', 'A' };
		final int countMutations = mutantDetectionServiceImpl.countMutations(lineZeroMutations);
		assertEquals(1, countMutations);
	}

	@Test
	public void shouldCountTwoMutations() {
		final char[] lineZeroMutations = { 'A', 'A', 'A', 'A', 'T', 'G', 'G', 'G', 'G', 'A' };
		final int countMutations = mutantDetectionServiceImpl.countMutations(lineZeroMutations);
		assertEquals(2, countMutations);
	}

	@Test
	public void shouldCountOnlyTwoMutationsWithMoreThanTwoMutations() {
		final char[] lineZeroMutations = { 'A', 'A', 'A', 'A', 'T', 'G', 'G', 'G', 'G', 'A', 'G', 'G', 'G', 'G', 'A' };
		final int countMutations = mutantDetectionServiceImpl.countMutations(lineZeroMutations);
		assertEquals(2, countMutations);
	}

	@Test
	public void shouldCountOneMutationHorizontally() {
		final char[][] charMatrix = new char[4][4];
		final String[] dna = { "ATGC", "CAGT", "TAGT", "CCCC" };

		final int countMutationsHorizontally = mutantDetectionServiceImpl.countMutationsHorizontally(charMatrix, dna);

		assertEquals(1, countMutationsHorizontally);
	}

	@Test
	public void shouldCountOnlyTwoMutationsHorizontally() {
		final char[][] charMatrix = new char[4][4];
		final String[] dna = { "ATGC", "GGGG", "TTTT", "CCCC" };

		final int countMutationsHorizontally = mutantDetectionServiceImpl.countMutationsHorizontally(charMatrix, dna);

		assertEquals(2, countMutationsHorizontally);
	}

	@Test
	public void shouldCountOneMutationVertically() {
		final char[][] charMatrix = { { 'A', 'G', 'C', 'A' }, { 'A', 'T', 'G', 'C' }, { 'A', 'T', 'T', 'T' },
				{ 'A', 'C', 'G', 'C' } };

		final int countMutationsVertically = mutantDetectionServiceImpl.countMutationsVertically(charMatrix);

		assertEquals(1, countMutationsVertically);
	}

	@Test
	public void shouldCountOnlyTwoMutationsVertically() {
		final char[][] charMatrix = { { 'A', 'G', 'C', 'C' }, { 'A', 'G', 'G', 'C' }, { 'A', 'G', 'T', 'C' },
				{ 'A', 'G', 'G', 'C' } };

		final int countMutationsVertically = mutantDetectionServiceImpl.countMutationsVertically(charMatrix);

		assertEquals(2, countMutationsVertically);
	}

	@Test
	public void shouldCountOneMutationDiagonallyDesc() {
		final char[][] charMatrix = { { 'A', 'G', 'C', 'A', 'T' }, { 'A', 'A', 'G', 'C', 'G' },
				{ 'A', 'T', 'A', 'T', 'A' }, { 'A', 'C', 'G', 'A', 'G' }, { 'A', 'C', 'G', 'C', 'C' } };

		final int countMutationsVertically = mutantDetectionServiceImpl.countMutationsDiagonallyDesc(charMatrix);

		assertEquals(1, countMutationsVertically);
	}

	@Test
	public void shouldCountOnlyTwoMutationsDiagonallyDesc() {
		final char[][] charMatrix = { { 'A', 'G', 'C', 'A', 'T' }, { 'C', 'A', 'G', 'C', 'G' },
				{ 'A', 'C', 'A', 'G', 'A' }, { 'A', 'C', 'C', 'A', 'G' }, { 'A', 'C', 'G', 'C', 'C' } };

		final int countMutationsVertically = mutantDetectionServiceImpl.countMutationsDiagonallyDesc(charMatrix);

		assertEquals(2, countMutationsVertically);
	}

	@Test
	public void shouldCountOnlyTwoMutationsDiagonallyDescFirstHalf() {
		final char[][] charMatrix = { { 'A', 'G', 'C', 'A', 'T', 'A', 'T' }, { 'C', 'A', 'G', 'C', 'G', 'A', 'T' },
				{ 'A', 'C', 'A', 'G', 'A', 'A', 'T' }, { 'A', 'C', 'C', 'A', 'G', 'C', 'G' },
				{ 'A', 'A', 'C', 'C', 'C', 'A', 'T' }, { 'G', 'C', 'A', 'C', 'C', 'A', 'T' },
				{ 'G', 'A', 'G', 'A', 'C', 'A', 'T' } };

		final int countMutationsVertically = mutantDetectionServiceImpl.countMutationsDiagonallyDesc(charMatrix);

		assertEquals(2, countMutationsVertically);
	}

	@Test
	public void shouldCountOneMutationDiagonallyAsc() {
		final char[][] charMatrix = { { 'A', 'G', 'C', 'A', 'T' }, { 'A', 'A', 'G', 'T', 'G' },
				{ 'A', 'T', 'T', 'T', 'A' }, { 'A', 'T', 'G', 'A', 'G' }, { 'A', 'C', 'G', 'C', 'C' } };

		final int countMutationsVertically = mutantDetectionServiceImpl.countMutationsDiagonallyAsc(charMatrix);

		assertEquals(1, countMutationsVertically);
	}

	@Test
	public void shouldCountOnlyTwoMutationsDiagonallyAsc() {
		final char[][] charMatrix = { { 'A', 'G', 'C', 'C', 'T' }, { 'C', 'A', 'C', 'T', 'G' },
				{ 'A', 'C', 'T', 'G', 'A' }, { 'C', 'T', 'G', 'A', 'G' }, { 'A', 'G', 'G', 'C', 'C' } };

		final int countMutationsVertically = mutantDetectionServiceImpl.countMutationsDiagonallyAsc(charMatrix);

		assertEquals(2, countMutationsVertically);
	}

	@Test
	public void shouldCountOnlyTwoMutationsDiagonallyAscFirstHalf() {
		final char[][] charMatrix = { { 'A', 'G', 'C', 'A', 'T', 'A', 'T' }, { 'C', 'A', 'A', 'C', 'G', 'A', 'T' },
				{ 'A', 'A', 'C', 'G', 'A', 'A', 'T' }, { 'A', 'C', 'G', 'A', 'G', 'C', 'G' },
				{ 'C', 'G', 'C', 'C', 'C', 'A', 'T' }, { 'G', 'C', 'A', 'C', 'C', 'A', 'T' },
				{ 'G', 'A', 'G', 'A', 'C', 'A', 'T' } };

		final int countMutationsVertically = mutantDetectionServiceImpl.countMutationsDiagonallyAsc(charMatrix);

		assertEquals(2, countMutationsVertically);
	}

	@Test
	public void shouldThrowsAPIExceptionWhenTraverseMatrixAllDirections() throws APIServiceException {
		final String[] dna = { "ATGC", "CAGT", "TAGT", "CCCC" };

		final HumanDTO humanDTO = new HumanDTO();
		humanDTO.setDna(dna);
		humanDTO.setMutantDna(false);

		final Human humanEntity = new Human();
		humanEntity.setDna(dna);

		when(humanRepository.save(humanEntity)).thenReturn(humanEntity);
		when(humanConverterService.toEntity(humanDTO)).thenReturn(humanEntity);

		try {
			mutantDetectionServiceImpl.identifyMutant(humanDTO);
		} catch (final APIServiceException apiException) {
			assertEquals(HttpStatus.FORBIDDEN.getReasonPhrase(), apiException.getMessage());
			assertEquals(APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION, apiException.getCode());
		}
	}

	@Test
	public void shouldTraverseMatrixAllDirectionsAndStopHorizontally() throws APIServiceException {
		final String[] dna = { "ATGC", "CAGT", "AAAA", "CCCC" };

		final HumanDTO humanDTO = new HumanDTO();
		humanDTO.setDna(dna);
		humanDTO.setMutantDna(true);

		final Human humanEntity = new Human();
		humanEntity.setDna(dna);
		humanEntity.setMutantDna(true);

		when(humanRepository.save(humanEntity)).thenReturn(humanEntity);
		when(humanConverterService.toEntity(humanDTO)).thenReturn(humanEntity);
		when(humanConverterService.toDTO(humanEntity)).thenReturn(humanDTO);

		final HumanDTO traverseMatrixAllDirections = mutantDetectionServiceImpl.identifyMutant(humanDTO);

		assertEquals(traverseMatrixAllDirections, humanDTO);
		assertEquals(traverseMatrixAllDirections.getDna(), humanDTO.getDna());
		assertEquals(traverseMatrixAllDirections.isMutantDna(), humanDTO.isMutantDna());
	}

	@Test
	public void shouldTraverseMatrixAllDirectionsAndStopVertically() throws APIServiceException {
		final String[] dna = { "ATGC", "AAGC", "AAGC", "ATCC" };

		final HumanDTO humanDTO = new HumanDTO();
		humanDTO.setDna(dna);
		humanDTO.setMutantDna(true);

		final Human humanEntity = new Human();
		humanEntity.setDna(dna);
		humanEntity.setMutantDna(true);

		when(humanRepository.save(humanEntity)).thenReturn(humanEntity);
		when(humanConverterService.toEntity(humanDTO)).thenReturn(humanEntity);
		when(humanConverterService.toDTO(humanEntity)).thenReturn(humanDTO);

		final HumanDTO traverseMatrixAllDirections = mutantDetectionServiceImpl.identifyMutant(humanDTO);

		assertEquals(traverseMatrixAllDirections, humanDTO);
		assertEquals(traverseMatrixAllDirections.getDna(), humanDTO.getDna());
		assertEquals(traverseMatrixAllDirections.isMutantDna(), humanDTO.isMutantDna());
	}

	@Test
	public void shouldTraverseMatrixAllDirectionsAndStopDiagonallyDesc() throws APIServiceException {
		final String[] dna = { "CTGC", "TCGC", "AACC", "ATCC" };

		final HumanDTO humanDTO = new HumanDTO();
		humanDTO.setDna(dna);
		humanDTO.setMutantDna(true);

		final Human humanEntity = new Human();
		humanEntity.setDna(dna);
		humanEntity.setMutantDna(true);

		when(humanRepository.save(humanEntity)).thenReturn(humanEntity);
		when(humanConverterService.toEntity(humanDTO)).thenReturn(humanEntity);
		when(humanConverterService.toDTO(humanEntity)).thenReturn(humanDTO);

		final HumanDTO traverseMatrixAllDirections = mutantDetectionServiceImpl.identifyMutant(humanDTO);

		assertEquals(traverseMatrixAllDirections, humanDTO);
		assertEquals(traverseMatrixAllDirections.getDna(), humanDTO.getDna());
		assertEquals(traverseMatrixAllDirections.isMutantDna(), humanDTO.isMutantDna());
	}

	@Test
	public void shouldTraverseMatrixAllDirectionsAndStopDiagonallyAsc() throws APIServiceException {
		final String[] dna = { "ATGC", "TGCC", "ACTC", "CTCC" };

		final HumanDTO humanDTO = new HumanDTO();
		humanDTO.setDna(dna);
		humanDTO.setMutantDna(true);

		final Human humanEntity = new Human();
		humanEntity.setDna(dna);
		humanEntity.setMutantDna(true);

		when(humanRepository.save(humanEntity)).thenReturn(humanEntity);
		when(humanConverterService.toEntity(humanDTO)).thenReturn(humanEntity);
		when(humanConverterService.toDTO(humanEntity)).thenReturn(humanDTO);

		final HumanDTO traverseMatrixAllDirections = mutantDetectionServiceImpl.identifyMutant(humanDTO);

		assertEquals(traverseMatrixAllDirections, humanDTO);
		assertEquals(traverseMatrixAllDirections.getDna(), humanDTO.getDna());
		assertEquals(traverseMatrixAllDirections.isMutantDna(), humanDTO.isMutantDna());
	}

	@Test
	public void shouldThrowsAPIEXceptionBecauseNullMatrix() throws APIServiceException {
		final HumanDTO humanDTO = new HumanDTO();

		try {
			mutantDetectionServiceImpl.identifyMutant(humanDTO);
		} catch (final APIServiceException apiException) {
			assertEquals("dna null", apiException.getMessage());
			assertEquals(APIServiceErrorCodes.HUMAN_MATRIX_DNA_SIZE_EXCEPTION, apiException.getCode());
		}
	}

	@Test
	public void shouldThrowsAPIEXceptionBecauseMatrixSizeZero() throws APIServiceException {
		final String[] dna = {};

		final HumanDTO humanDTO = new HumanDTO();
		humanDTO.setDna(dna);

		try {
			mutantDetectionServiceImpl.identifyMutant(humanDTO);
		} catch (final APIServiceException apiException) {
			assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), apiException.getMessage());
			assertEquals(APIServiceErrorCodes.HUMAN_MATRIX_DNA_SIZE_EXCEPTION, apiException.getCode());
		}
	}

	@Test
	public void shouldThrowsAPIEXceptionBecauseNotSquareMatrix() throws APIServiceException {
		final String[] dna = { "ATGT", "GACC", "ATCG", "ATC" };

		final HumanDTO humanDTO = new HumanDTO();
		humanDTO.setDna(dna);
		humanDTO.setMutantDna(false);

		try {
			mutantDetectionServiceImpl.identifyMutant(humanDTO);
		} catch (final APIServiceException apiException) {
			assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), apiException.getMessage());
			assertEquals(APIServiceErrorCodes.HUMAN_MATRIX_DNA_SIZE_EXCEPTION, apiException.getCode());
		}
	}

	@Test
	public void shouldThrowsAPIEXceptionBecauseOfMatrixSize() throws APIServiceException {
		final String[] dna = { "ATG", "GCC", "ATC" };

		final HumanDTO humanDTO = new HumanDTO();
		humanDTO.setDna(dna);
		humanDTO.setMutantDna(false);

		final Human humanEntity = new Human();
		humanEntity.setDna(dna);
		humanEntity.setMutantDna(false);

		try {
			mutantDetectionServiceImpl.identifyMutant(humanDTO);
		} catch (final APIServiceException apiException) {
			assertEquals(HttpStatus.FORBIDDEN.getReasonPhrase(), apiException.getMessage());
			assertEquals(APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION, apiException.getCode());
		}
	}

	@Test
	public void shouldFindMutantInDB() throws APIServiceException {
		final String[] dna = { "ATGC", "CAGT", "AAAA", "CCCC" };

		final HumanDTO humanDTO = new HumanDTO();
		humanDTO.setDna(dna);
		humanDTO.setMutantDna(true);

		final Human humanEntity = new Human();
		humanEntity.setDna(dna);
		humanEntity.setMutantDna(true);

		final Optional<Human> humanOptional = Optional.of(humanEntity);

		when(humanRepository.findByDna(dna)).thenReturn(humanOptional);
		when(humanConverterService.toDTO(humanEntity)).thenReturn(humanDTO);

		final HumanDTO traverseMatrixAllDirections = mutantDetectionServiceImpl.identifyMutant(humanDTO);

		assertEquals(traverseMatrixAllDirections, humanDTO);
		assertEquals(traverseMatrixAllDirections.getDna(), humanDTO.getDna());
		assertEquals(traverseMatrixAllDirections.isMutantDna(), humanDTO.isMutantDna());
	}

	@Test
	public void shouldThrowsAPIExceptionBecauseFoundNotMutantInDB() throws APIServiceException {
		final String[] dna = { "ATGC", "CAGT", "ATAA", "CCCC" };

		final HumanDTO humanDTO = new HumanDTO();
		humanDTO.setDna(dna);
		humanDTO.setMutantDna(false);

		final Human humanEntity = new Human();
		humanEntity.setDna(dna);
		humanEntity.setMutantDna(false);

		final Optional<Human> humanOptional = Optional.of(humanEntity);

		when(humanRepository.findByDna(dna)).thenReturn(humanOptional);
		when(humanConverterService.toDTO(humanEntity)).thenReturn(humanDTO);

		try {
			mutantDetectionServiceImpl.identifyMutant(humanDTO);
		} catch (final APIServiceException apiException) {
			assertEquals(HttpStatus.FORBIDDEN.getReasonPhrase(), apiException.getMessage());
			assertEquals(APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION, apiException.getCode());
		}
	}

}
