package co.com.elkin.apps.mutants.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import co.com.elkin.apps.mutants.dto.HumanDTO;
import co.com.elkin.apps.mutants.exception.APIServiceErrorCodes;
import co.com.elkin.apps.mutants.exception.APIServiceException;
import co.com.elkin.apps.mutants.service.MutantDetectionServiceImpl;

public class MutantDetectionControllerTest {

	private static final String LANG_EN = "en";
	private static final String[] MUTANT_DNA = { "ATGCGA", "CAGTGC", "TTATGT", "AGTAGG", "ATCCCC", "TCACTG" };
	private static final String[] NOT_MUTANT_DNA = { "CTGCCA", "CAGTGC", "TTATGT", "AGTAGG", "ATCCCC", "CCACTG" };

	@InjectMocks
	private MutantDetectionController mutantDetectionController;
	@Mock
	private MutantDetectionServiceImpl mutantDetectionServiceImpl;

	private final HumanDTO humanRequestWithMutantDna = new HumanDTO();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		humanRequestWithMutantDna.setDna(MUTANT_DNA);
	}

	@Test
	public void onlyOneCallToMutantDetectionServiceIdentifyMutant() throws APIServiceException {
		mutantDetectionController.verifyDna(humanRequestWithMutantDna, LANG_EN);
		verify(mutantDetectionServiceImpl, times(1)).identifyMutant(humanRequestWithMutantDna);
	}

	@Test
	public void shouldNotChangeMutantDetectionServiceResponseWhenMutant() throws APIServiceException {
		final HumanDTO humanResponseWithMutantDna = new HumanDTO();
		humanResponseWithMutantDna.setDna(MUTANT_DNA);
		humanResponseWithMutantDna.setMutantDna(true);

		when(mutantDetectionServiceImpl.identifyMutant(humanRequestWithMutantDna))
				.thenReturn(humanResponseWithMutantDna);

		final ResponseEntity<HumanDTO> responseEntity = mutantDetectionController.verifyDna(humanRequestWithMutantDna,
				LANG_EN);

		assertEquals(humanResponseWithMutantDna, responseEntity.getBody());
		assertEquals(humanResponseWithMutantDna.getDna(), responseEntity.getBody().getDna());
		assertTrue(responseEntity.getBody().isMutantDna());
	}

	@Test(expected = APIServiceException.class)
	public void shouldThrowsExceptionWhenNotMutant() throws APIServiceException {
		final HumanDTO humanWithoutMutantDna = new HumanDTO();
		humanWithoutMutantDna.setDna(NOT_MUTANT_DNA);

		when(mutantDetectionServiceImpl.identifyMutant(humanWithoutMutantDna)).thenThrow(new APIServiceException(
				HttpStatus.FORBIDDEN.getReasonPhrase(), APIServiceErrorCodes.HUMAN_IS_NOT_MUTANT_EXCEPTION));

		mutantDetectionController.verifyDna(humanWithoutMutantDna, LANG_EN);
	}

}
