package co.com.elkin.apps.mutants.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This DTO represents a human with his DNA and whether is mutant or not
 * 
 * @author elkin.giraldo
 *
 */
@Getter
@Setter
@ToString
public class HumanDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String[] dna;
	private boolean mutantDna;

}
