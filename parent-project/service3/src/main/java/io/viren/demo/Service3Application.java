package io.viren.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RestController
@Slf4j
public class Service3Application {

	public static void main(String[] args) {
		SpringApplication.run(Service3Application.class, args);
	}
	
	@GetMapping("/")
	public String sayHelloService2() {
		log.info("~~~~~~~~~~~~~~ In service 2 ~~~~~~~~~~~~~~~~~");
		return "Hello from Service3";
	}
	
}
