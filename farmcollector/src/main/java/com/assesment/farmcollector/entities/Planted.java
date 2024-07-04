package com.assesment.farmcollector.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Planted {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double plantingArea;
    private String cropType;
    private double expectedProduct;
    private String season;
    
    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;
}
