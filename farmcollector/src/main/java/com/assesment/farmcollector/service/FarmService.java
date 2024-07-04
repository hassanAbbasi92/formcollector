package com.assesment.farmcollector.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assesment.farmcollector.entities.Farm;
import com.assesment.farmcollector.entities.Harvested;
import com.assesment.farmcollector.entities.Planted;
import com.assesment.farmcollector.repos.FarmRepository;
import com.assesment.farmcollector.repos.HarvestedRepository;
import com.assesment.farmcollector.repos.PlantedRepository;

@Service
@Log4j2
public class FarmService {

	@Autowired
	PlantedRepository plantedRepository;

	@Autowired
	private HarvestedRepository harvestedRepository;

	@Autowired
	private FarmRepository farmRepository;

	public Planted savePlanted(String planted, String farmName) {
		try {
			List<Farm> farm = farmRepository.findByName(farmName);
			if(farm.isEmpty()){
				return null;
			}
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
			Planted plantedBean = objectMapper.readValue(planted.toString(), Planted.class);
			plantedBean.setFarm(farm.get(0));

			return plantedRepository.save(plantedBean);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}

	public Harvested saveHarvested(String harvested, String farmName) {
		Harvested harvestedEntity = null;
		try {
			List<Farm> farm = farmRepository.findByName(farmName);
			if(farm.isEmpty()){
				return null;
			}

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
			harvestedEntity = objectMapper.readValue(harvested.toString(), Harvested.class);
			harvestedEntity.setFarm(farm.get(0));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(),e);
			return null;
		}
		return harvestedRepository.save(harvestedEntity);
	}

	public Farm saveFarm(String name) {
		Farm farm = new Farm();
		farm.setName(name);
		return farmRepository.save(farm);
	}
	public Long getFarmId(String farmName){
		List<Farm> farm = farmRepository.findByName(farmName);
		if(farm.isEmpty()){
			return null;
		}
		return farm.get(0).getId();
	}
	
	public List<Planted> getPlantedForFarmAndSeason(Long farmId, String season) {
        return plantedRepository.findByFarmIdAndSeason(farmId, season);
    }

    public List<Harvested> getHarvestedForFarmAndSeason(Long farmId, String season) {
        return harvestedRepository.findByFarmIdAndSeason(farmId, season);
    }

    public List<Planted> getPlantedForSeason(String season) {
        return plantedRepository.findBySeason(season);
    }

    public List<Harvested> getHarvestedForSeason(String season) {
        return harvestedRepository.findBySeason(season);
    }
}
