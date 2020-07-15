package co.com.elkin.apps.mutants.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.elkin.apps.mutants.entity.Human;

public interface HumanRepository extends JpaRepository<Human, Integer> {

	public Optional<Human> findByDna(String[] dna);

}
