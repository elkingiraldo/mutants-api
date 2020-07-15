package co.com.elkin.apps.mutants.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public StatisticsDTO(final Long count_mutant_dna, final Long count_human_dna) {
		this.count_mutant_dna = count_mutant_dna;
		this.count_human_dna = count_human_dna;

		if (count_human_dna != 0) {
			this.ratio = (double) (count_mutant_dna / count_human_dna);
		}
	}

	private Long count_mutant_dna;
	private Long count_human_dna;
	private Double ratio = null;

}
