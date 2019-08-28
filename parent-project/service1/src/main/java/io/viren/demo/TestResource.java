package io.viren.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TestResource {

	@Autowired
	private RestTemplate restTempltae;

	@Autowired
	private HttpEntity<String> httpEnity;

	@GetMapping("/sample")
	public String sample() {
		final ResponseEntity<String> responseEntity = restTempltae.exchange("http://localhost:8090/resource2",
				HttpMethod.GET, httpEnity, String.class);
		log.info("~~~~~~~~~~~~~~~ Response is {}",
				responseEntity.getStatusCode().is2xxSuccessful() ? "sab badhiya hai" : "pil gae sanam");
		
		final ResponseEntity<String> responseEntity2 = restTempltae.exchange("http://localhost:8090/resource3",
				HttpMethod.GET, httpEnity, String.class);
		
		return "Sab \"hunky-dory\" hai !!";
	}

	@GetMapping("/")
	public String sayHello() {
		log.info("sample log.....!!!");
		return "Hello";
	}
}
