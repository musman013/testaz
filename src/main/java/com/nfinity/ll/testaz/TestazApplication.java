package com.nfinity.ll.testaz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TestazApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestazApplication.class, args);
	}

}

