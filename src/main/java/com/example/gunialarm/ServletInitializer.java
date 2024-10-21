package com.example.gunialarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.WebApplicationContext;

@SpringBootApplication
@EnableScheduling
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ServletInitializer.class);
	}
	@Override
  	protected WebApplicationContext run(SpringApplication application) {
      	return super.run(application);
  	}

	public static void main(String[] args) {
		SpringApplication.run(ServletInitializer.class, args);
	}
}
