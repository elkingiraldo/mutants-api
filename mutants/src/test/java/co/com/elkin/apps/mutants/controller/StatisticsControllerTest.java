package co.com.elkin.apps.mutants.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import co.com.elkin.apps.mutants.dto.StatisticsDTO;
import co.com.elkin.apps.mutants.exception.APIServiceException;
import co.com.elkin.apps.mutants.service.StatisticsServiceImpl;

public class StatisticsControllerTest {

	@InjectMocks
	private StatisticsController statisticsController;
	@Mock
	private StatisticsServiceImpl statisticsServiceImpl;

	private final StatisticsDTO statisticsResponse = new StatisticsDTO(2L, 10L);

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void onlyOneCallToStatisticsServiceObtainStatistics() throws APIServiceException {
		statisticsController.statistics();
		verify(statisticsServiceImpl, times(1)).obtainStatistics();
	}

	@Test
	public void shouldNotChangeStatisticsServiceResponse() {
		when(statisticsServiceImpl.obtainStatistics()).thenReturn(statisticsResponse);

		final ResponseEntity<StatisticsDTO> statistics = statisticsController.statistics();

		assertEquals(statisticsResponse, statistics.getBody());
		assertEquals(statisticsResponse.getCount_human_dna(), statistics.getBody().getCount_human_dna());
		assertEquals(statisticsResponse.getCount_mutant_dna(), statistics.getBody().getCount_mutant_dna());
		assertEquals(statisticsResponse.getRatio(), statistics.getBody().getRatio());
	}

}
