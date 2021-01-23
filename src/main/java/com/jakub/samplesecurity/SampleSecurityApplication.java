package com.jakub.samplesecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@EnableWebSecurity
public class SampleSecurityApplication {

	public static void main(String[] args) {

		SpringApplication.run(SampleSecurityApplication.class, args);
	}

	@Bean
	DispatcherServlet dispatcherServlet () {
		DispatcherServlet ds = new DispatcherServlet();
		ds.setThrowExceptionIfNoHandlerFound(true);
		return ds;
	}
}
