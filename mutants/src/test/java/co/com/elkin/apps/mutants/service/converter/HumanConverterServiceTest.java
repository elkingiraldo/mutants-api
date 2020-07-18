package co.com.elkin.apps.mutants.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import co.com.elkin.apps.mutants.dto.HumanDTO;
import co.com.elkin.apps.mutants.entity.Human;

public class HumanConverterServiceTest {

	private static final String[] MUTANT_DNA = { "ATGCGA", "CAGTGC", "TTATGT", "AGTAGG", "ATCCCC", "TCACTG" };

	@InjectMocks
	private HumanConverterService humanConverterService;
	@Mock
	private ModelMapper modelMapper;

	private final Human human = new Human();
	private final HumanDTO humanDTO = new HumanDTO();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		human.setDna(MUTANT_DNA);
		human.setMutantDna(true);

		humanDTO.setDna(MUTANT_DNA);
		humanDTO.setMutantDna(true);
	}

	@Test
	public void shouldReturnCorrectDTO() {
		when(modelMapper.map(human, HumanDTO.class)).thenReturn(humanDTO);

		final HumanDTO dto = humanConverterService.toDTO(human);

		assertEquals(humanDTO.toString(), dto.toString());
	}

	@Test
	public void shouldReturnCorrectEntity() {
		when(modelMapper.map(humanDTO, Human.class)).thenReturn(human);

		final Human entity = humanConverterService.toEntity(humanDTO);

		assertEquals(human.toString(), entity.toString());
	}

}
