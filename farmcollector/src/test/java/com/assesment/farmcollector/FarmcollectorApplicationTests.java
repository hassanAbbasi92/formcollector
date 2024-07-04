package com.assesment.farmcollector.controllers;

import com.assesment.farmcollector.entities.Farm;
import com.assesment.farmcollector.entities.Harvested;
import com.assesment.farmcollector.entities.Planted;
import com.assesment.farmcollector.service.FarmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class FarmcollectorApplicationTests {

	@Mock
	private FarmService farmService;

	@InjectMocks
	private FarmController farmController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateFarm_Success() {
		String farmName = "TestFarm";

		HttpStatus response = farmController.createFarm(farmName);

		assertEquals(HttpStatus.OK, response);
		verify(farmService, times(1)).saveFarm(farmName);
	}

	@Test
	public void testHarvested_Success() {
		String farmName = "TestFarm";
		String harvestedJson = "{ \"actualAmount\": 150, \"cropType\": \"mango\", \"season\": \"summer\" }";

		Harvested harvested = new Harvested();
		harvested.setActualAmount(150);
		harvested.setCropType("mango");
		harvested.setSeason("summer");

		when(farmService.saveHarvested(eq(harvestedJson), eq(farmName))).thenReturn(harvested);

		HttpStatus response = farmController.harvested(harvestedJson, farmName);

		assertEquals(HttpStatus.OK, response);
		verify(farmService, times(1)).saveHarvested(eq(harvestedJson), eq(farmName));
	}

	@Test
	public void testPlanted_Success() {
		String farmName = "TestFarm";
		String plantedJson = "{ \"plantingArea\": 200, \"cropType\": \"mango\", \"expectedProduct\": 10, \"season\": \"summer\" }";

		Planted planted = new Planted();
		planted.setPlantingArea(200);
		planted.setCropType("mango");
		planted.setExpectedProduct(10);
		planted.setSeason("summer");

		when(farmService.savePlanted(eq(plantedJson), eq(farmName))).thenReturn(planted);

		HttpStatus response = farmController.planted(plantedJson, farmName);

		assertEquals(HttpStatus.OK, response);
		verify(farmService, times(1)).savePlanted(eq(plantedJson), eq(farmName));
	}

	@Test
	public void testGetFarmSeasonReport_Success() {
		String farmName = "TestFarm";
		String season = "summer";
		Long farmId = 1L;

		when(farmService.getFarmId(eq(farmName))).thenReturn(farmId);
		when(farmService.getPlantedForFarmAndSeason(eq(farmId), eq(season))).thenReturn(Collections.singletonList(new Planted()));
		when(farmService.getHarvestedForFarmAndSeason(eq(farmId), eq(season))).thenReturn(Collections.singletonList(new Harvested()));

		String report = farmController.getFarmSeasonReport(farmName, season);

		assertNotNull(report);
		assertTrue(report.contains("Report for Farm ID: " + farmId));
		assertTrue(report.contains("Season: " + season));
		verify(farmService, times(1)).getFarmId(eq(farmName));
		verify(farmService, times(1)).getPlantedForFarmAndSeason(eq(farmId), eq(season));
		verify(farmService, times(1)).getHarvestedForFarmAndSeason(eq(farmId), eq(season));
	}

	@Test
	public void testGetSeasonReport_Success() {
		String season = "summer";

		when(farmService.getPlantedForSeason(eq(season))).thenReturn(Collections.singletonList(new Planted()));
		when(farmService.getHarvestedForSeason(eq(season))).thenReturn(Collections.singletonList(new Harvested()));

		String report = farmController.getSeasonReport(season);

		assertNotNull(report);
		assertTrue(report.contains("Report for Season: " + season));
		verify(farmService, times(1)).getPlantedForSeason(eq(season));
		verify(farmService, times(1)).getHarvestedForSeason(eq(season));
	}
}
