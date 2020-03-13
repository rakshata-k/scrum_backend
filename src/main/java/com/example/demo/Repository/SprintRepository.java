package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Sprint;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
	
	Optional<Sprint> findByName(String name);
	
	Void deleteByName(String name);

}
