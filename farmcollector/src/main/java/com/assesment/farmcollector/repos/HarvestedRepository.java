package com.assesment.farmcollector.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assesment.farmcollector.entities.Harvested;

public interface HarvestedRepository extends JpaRepository<Harvested, Long> {
	List<Harvested> findByFarmIdAndSeason(Long farmId, String season);
    List<Harvested> findBySeason(String season);	
}
