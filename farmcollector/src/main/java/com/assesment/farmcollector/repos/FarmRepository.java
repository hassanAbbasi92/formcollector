package com.assesment.farmcollector.repos;

import org.apache.el.stream.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assesment.farmcollector.entities.Farm;

public interface FarmRepository extends JpaRepository<Farm, Long> {
    List<Farm> findByName(String name);
}
