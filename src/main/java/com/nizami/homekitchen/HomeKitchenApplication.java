package com.nizami.homekitchen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

/**
 * Main entry point for the Home Kitchen application.
 */
@SpringBootApplication
public class HomeKitchenApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeKitchenApplication.class, args);
	}
}
