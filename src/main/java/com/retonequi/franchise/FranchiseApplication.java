package com.retonequi.franchise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import io.r2dbc.spi.ConnectionFactory;


@SpringBootApplication
public class FranchiseApplication {

	public static void main(String[] args) {
		EnvLoader.load();
		SpringApplication.run(FranchiseApplication.class, args);
	}

	@Bean
	@Profile("!test")
	ConnectionFactoryInitializer initializer(ConnectionFactory connectiontFactory){

		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectiontFactory);
		initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
		return initializer;

	}

}
