package com.nizami.homekitchen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main entry point for the Home Kitchen application.
 */
@SpringBootApplication
@EnableCaching
public class HomeKitchenApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeKitchenApplication.class, args);
	}
}
