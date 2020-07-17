package co.com.elkin.apps.mutants.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.com.elkin.apps.mutants.dto.StatisticsDTO;
import co.com.elkin.apps.mutants.entity.Human;
import co.com.elkin.apps.mutants.exception.APIServiceException;
import co.com.elkin.apps.mutants.repository.HumanRepository;

public class StatisticsServiceImplTest {

	@InjectMocks
	private StatisticsServiceImpl statisticsServiceImpl;
	@Mock
	private HumanRepository humanRepository;

	private final StatisticsDTO statisticsResponse = new StatisticsDTO(1L, 2L);
	private final List<Human> humanList = new ArrayList<>();

	private final Human human01 = new Human();
	private final Human human02 = new Human();
	private final Human human03 = new Human();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		human01.setMutantDna(true);
		human02.setMutantDna(false);
		human03.setMutantDna(false);

		humanList.add(human01);
		humanList.add(human02);
		humanList.add(human03);
	}

	@Test
	public void onlyOneCallToHumanRepositoryFindAll() throws APIServiceException {
		statisticsServiceImpl.obtainStatistics();
		verify(humanRepository, times(1)).findAll();
	}

	@Test
	public void shouldNotChangeHumanRepositoryResponse() {
		when(humanRepository.findAll()).thenReturn(humanList);

		final StatisticsDTO obtainStatistics = statisticsServiceImpl.obtainStatistics();

		assertEquals(statisticsResponse.getCount_human_dna(), obtainStatistics.getCount_human_dna());
		assertEquals(statisticsResponse.getCount_mutant_dna(), obtainStatistics.getCount_mutant_dna());
		assertEquals(statisticsResponse.getRatio(), obtainStatistics.getRatio());
	}

}
