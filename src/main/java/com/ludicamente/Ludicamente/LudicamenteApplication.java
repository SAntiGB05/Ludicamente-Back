	package com.ludicamente.Ludicamente;

	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;
	import org.springframework.boot.autoconfigure.domain.EntityScan;
	import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

	@SpringBootApplication
	@EnableJpaRepositories("com.ludicamente.Ludicamente.model.Repository")
	@EntityScan(basePackages = "com.ludicamente.Ludicamente.model")

	public class LudicamenteApplication {

		public static void main(String[] args) {
			SpringApplication.run(LudicamenteApplication.class, args);
		}

	}
