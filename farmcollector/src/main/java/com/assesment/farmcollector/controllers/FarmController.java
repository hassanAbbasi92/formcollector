package com.assesment.farmcollector.controllers;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assesment.farmcollector.entities.Farm;
import com.assesment.farmcollector.entities.Harvested;
import com.assesment.farmcollector.entities.Planted;
import com.assesment.farmcollector.service.FarmService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/farm")
@Log4j2
public class FarmController {
	
	@Autowired
	FarmService farmService;

    @RequestMapping(value = "/createFarm", method = RequestMethod.POST)
    public HttpStatus createFarm(@RequestParam(value = "name") String name) {
        farmService.saveFarm(name);
        return HttpStatus.OK;
    }
	
	@RequestMapping(value = "/harvested/{farmName}", method = RequestMethod.POST)
	public HttpStatus harvested(@RequestParam(value = "harvested") String harvested, @PathVariable String farmName) {
		Harvested status = farmService.saveHarvested(harvested, farmName);
        if(status==null){
            return HttpStatus.BAD_REQUEST;
        }
		return HttpStatus.OK;
	}

    @RequestMapping(value = "/planted/{farmName}", method = RequestMethod.POST)
    public HttpStatus planted(@RequestParam(value = "planted") String planted, @PathVariable String farmName) {
        log.info("Planted request received");
        Planted status =  farmService.savePlanted(planted, farmName);
        if(status==null) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }
	
	
	@RequestMapping(value = "/report/farm/{farmName}/season/{season}", method = RequestMethod.GET)
	public String getFarmSeasonReport(@PathVariable String farmName, @PathVariable String season) {
        Long farmId = farmService.getFarmId(farmName);
        if(farmId==null){
            return "Invalid farm Name";
        }
		List<Planted> plantedList = farmService.getPlantedForFarmAndSeason(farmId, season);
        List<Harvested> harvestedList = farmService.getHarvestedForFarmAndSeason(farmId, season);

        StringBuilder report = new StringBuilder();
        report.append("Report for Farm ID: ").append(farmId).append(" Season: ").append(season).append("\n");

        for (Planted planted : plantedList) {
            report.append("Crop: ").append(planted.getCropType())
                  .append(", Planted Area: ").append(planted.getPlantingArea())
                  .append(", Expected Product: ").append(planted.getExpectedProduct()).append(" tons\n");
            for (Harvested harvested : harvestedList) {
                if (harvested.getCropType().equals(planted.getCropType())) {
                    report.append("Actual Harvested Product: ").append(harvested.getActualAmount()).append(" tons\n");
                }
            }
        }
        return report.toString();
	}
	
	@RequestMapping(value = "/report/season/{season}", method = RequestMethod.GET)
    public String getSeasonReport(@PathVariable String season) {
        List<Planted> plantedList = farmService.getPlantedForSeason(season);
        List<Harvested> harvestedList = farmService.getHarvestedForSeason(season);

        StringBuilder report = new StringBuilder();
        report.append("Report for Season: ").append(season).append("\n");

        for (Planted planted : plantedList) {
            report.append("Farm ID: ").append(planted.getFarm().getId())
                  .append(", Crop: ").append(planted.getCropType())
                  .append(", Planted Area: ").append(planted.getPlantingArea())
                  .append(", Expected Product: ").append(planted.getExpectedProduct()).append(" tons\n");
            for (Harvested harvested : harvestedList) {
                if (harvested.getCropType().equals(planted.getCropType())
                        && harvested.getFarm().getId().equals(planted.getFarm().getId())) {
                    report.append("Actual Harvested Product: ").append(harvested.getActualAmount()).append(" tons\n");
                }
            }
        }
        return report.toString();
    }
	
}
