package com.assesment.farmcollector.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assesment.farmcollector.entities.Planted;

public interface PlantedRepository extends JpaRepository<Planted, Long> {
	
	List<Planted> findByFarmIdAndSeason(Long farmId, String season);
    List<Planted> findBySeason(String season);
}
