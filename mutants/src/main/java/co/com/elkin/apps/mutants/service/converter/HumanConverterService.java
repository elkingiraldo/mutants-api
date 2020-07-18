package co.com.elkin.apps.mutants.service.converter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import co.com.elkin.apps.mutants.dto.HumanDTO;
import co.com.elkin.apps.mutants.entity.Human;

/**
 * This service converts {@link HumanDTO} in {@link Human} and vice versa with
 * the help of {@link ModelMapper}
 * 
 * @author elkin.giraldo
 *
 */
@Service
public class HumanConverterService {

	/**
	 * This method transforms {@link Human} to {@link HumanDTO}
	 * 
	 * @param entity, Human entity
	 * @return {@link HumanDTO}
	 */
	public HumanDTO toDTO(final Human entity) {
		final ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(entity, HumanDTO.class);
	}

	/**
	 * This method transforms {@link HumanDTO} to {@link Human}
	 * 
	 * @param dto, Human DTO
	 * @return {@link Human}
	 */
	public Human toEntity(final HumanDTO dto) {
		final ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(dto, Human.class);
	}

}
