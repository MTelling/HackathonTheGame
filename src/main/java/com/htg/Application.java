package com.htg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@SpringBootApplication
public class Application {

	//public static String urlToRunner = "http://localhost:4567";
	public static String urlToRunner = "http://192.168.0.105:4567";
	public static Logger logger = Logger.getLogger("Logger");
	private static FileHandler fileHandler;

	public static void main(String[] args) {

		try {

			fileHandler = new FileHandler("Server.log");
			logger.addHandler(fileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
			logger.info("com.htg.runner.Application started!");

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (args.length > 0) urlToRunner = args[0];
		SpringApplication.run(Application.class, args);
	}

}