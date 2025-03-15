package com.company.pawpet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableJpaRepositories
public class PawpetApplication {
@CrossOrigin
	public static void main(String[] args) {
		SpringApplication.run(PawpetApplication.class, args);
	}

}

