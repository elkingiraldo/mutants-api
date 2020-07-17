package co.com.elkin.apps.mutants.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HumanDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String[] dna;
	private boolean mutantDna;

}
