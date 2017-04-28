package com.htg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static String urlToRunner = "http://localhost:4567";


	public static void main(String[] args) {

		if (args.length > 0) urlToRunner = args[0];
		SpringApplication.run(Application.class, args);
	}

}