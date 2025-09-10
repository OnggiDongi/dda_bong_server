package com.hana7.ddabong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DdabongApplication {


	public static void main(String[] args) {
		SpringApplication.run(DdabongApplication.class, args);
	}

}
