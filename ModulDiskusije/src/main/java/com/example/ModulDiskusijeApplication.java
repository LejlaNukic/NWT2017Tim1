package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableDiscoveryClient
@SpringBootApplication
public class ModulDiskusijeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModulDiskusijeApplication.class, args);
	}
}
