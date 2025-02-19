package com.assesment.farmcollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
public class FarmCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmCollectorApplication.class, args);
		log.info("Application Startup");
	}

}
