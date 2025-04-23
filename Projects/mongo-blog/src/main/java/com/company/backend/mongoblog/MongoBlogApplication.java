package com.company.backend.mongoblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

// If no run button shows up or application.properties fails to read the .env file,
// check out README.md for instructions on setting up the environment.

@SpringBootApplication
@EnableMongoAuditing
public class MongoBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoBlogApplication.class, args);
	}

}
