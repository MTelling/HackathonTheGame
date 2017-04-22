package com.htg;

import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    @Autowired
    private static ServerState state;

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
		state.setCurrentGame(new Game("This is a test challenge for testing"));
	}
}
