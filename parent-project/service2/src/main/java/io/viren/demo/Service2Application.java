package io.viren.demo;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RestController
@Configuration
@Slf4j
public class Service2Application {

	@Autowired
	private RestTemplate restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Service2Application.class, args);
	}
	
	@GetMapping("/")
	public String sayHello() {
		return "Hello";
	}

	@GetMapping("/resource2")
	@NewSpan
	public void sampleHello(@RequestHeader MultiValueMap<String, String> headers) {
		log.info("~~~~~~~~~~~~~~~~~~~~ Headers listing is as follows ~~~~~~~~~~~~~~~~~~~~~~ ");
		
		headers.forEach((key, values) -> log.info("Header key is {} and value is {}", key,
				values.stream().collect(Collectors.joining(","))));
		log.info("###########################################################################");
		String response = restTemplate.getForObject("http://localhost:8091/", String.class);
		log.info("~~~~~~~~~~~ Response is {} ~~~~~~~~~~~~~~~~",response);
	}
	
	@GetMapping("/resource3")
	@NewSpan
	public void sampleHello1(@RequestHeader MultiValueMap<String, String> headers) {
		log.info("~~~~~~~~~~~~~~~~~~~~ in another method ~~~~~~~~~~~~~~~~~~~~~~ ");
		
		headers.forEach((key, values) -> log.info("Header key is {} and value is {}", key,
				values.stream().collect(Collectors.joining(","))));
		log.info("###########################################################################");
		String response = restTemplate.getForObject("http://localhost:8091/", String.class);
		log.info("~~~~~~~~~~~ Response is {} ~~~~~~~~~~~~~~~~",response);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
