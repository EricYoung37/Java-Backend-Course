package com.company.backend.mongoblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// If no run button shows up or application.properties fails to read the .env file,
// check out README.md for instructions on setting up the environment.

@SpringBootApplication
public class MongoBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoBlogApplication.class, args);
	}

}
