package com.dynamicpmc.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		System.out.println("Main---Start");
		SpringApplication.run(BackendApplication.class, args);
		System.out.println("Main---End");
	}

}
