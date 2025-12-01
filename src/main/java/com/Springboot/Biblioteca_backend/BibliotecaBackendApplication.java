package com.Springboot.Biblioteca_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BibliotecaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaBackendApplication.class, args);
	}

}
