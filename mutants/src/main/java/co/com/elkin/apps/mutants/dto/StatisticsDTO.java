package co.com.elkin.apps.mutants.dto;

import java.io.Serializable;

import lombok.Getter;

/**
 * This DTO represents the statistics of the service with the information of
 * mutants, the total humans consulted and the ratio of the mutants found
 * 
 * @author elkin.giraldo
 *
 */
@Getter
public class StatisticsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public StatisticsDTO(final Long countMutantDna, final Long totalHuman) {
		this.count_mutant_dna = countMutantDna;
		this.count_human_dna = totalHuman;

		if (totalHuman != 0) {
			this.ratio = (double) countMutantDna / (double) totalHuman;
		}
	}

	private final Long count_mutant_dna;
	private final Long count_human_dna;
	private Double ratio = null;

}
