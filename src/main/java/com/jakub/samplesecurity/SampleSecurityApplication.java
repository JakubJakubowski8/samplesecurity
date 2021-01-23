package com.jakub.samplesecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class SampleSecurityApplication {

	public static void main(String[] args) {

		SpringApplication.run(SampleSecurityApplication.class, args);
	}
}
