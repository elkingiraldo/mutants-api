package co.com.elkin.apps.mutants.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.elkin.apps.mutants.entity.Human;

/**
 * Repository in charge of persist humans in the DB
 * 
 * @author elkin.giraldo
 *
 */
public interface HumanRepository extends JpaRepository<Human, Integer> {

	public Optional<Human> findByDna(String[] dna);

}
