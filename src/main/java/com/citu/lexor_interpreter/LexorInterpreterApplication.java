package com.citu.lexor_interpreter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.citu.lexor_interpreter", "com.cit.lexor"})
public class LexorInterpreterApplication {

	public static void main(String[] args) {
		SpringApplication.run(LexorInterpreterApplication.class, args);
	}

}
