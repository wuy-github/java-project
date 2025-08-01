package com.project.futabuslines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class FutabuslinesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FutabuslinesApplication.class, args);
	}

}
