package co.com.elkin.apps.mutants.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.elkin.apps.mutants.dto.StatisticsDTO;
import co.com.elkin.apps.mutants.entity.Human;
import co.com.elkin.apps.mutants.repository.HumanRepository;

/**
 * Service implementation for handling statistics
 * 
 * @author elkin.giraldo
 *
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {

	private final HumanRepository humanRepository;

	@Autowired
	public StatisticsServiceImpl(final HumanRepository humanRepository) {
		this.humanRepository = humanRepository;
	}

	@Override
	public StatisticsDTO obtainStatistics() {
		final List<Human> allHumans = humanRepository.findAll();

		final long numberOfMutants = allHumans.stream().filter(Human::isMutantDna).count();

		return new StatisticsDTO(numberOfMutants, (long) allHumans.size());
	}

}
