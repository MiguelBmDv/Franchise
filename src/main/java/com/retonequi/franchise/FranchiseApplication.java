package com.retonequi.franchise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FranchiseApplication {

	public static void main(String[] args) {
		EnvLoader.load();
		SpringApplication.run(FranchiseApplication.class, args);
	}

}
