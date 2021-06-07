package com.onushi.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.onushi")
public class SampleApp {
	public static void main(String[] args) {
		SpringApplication.run(SampleApp.class, args);
	}
}
