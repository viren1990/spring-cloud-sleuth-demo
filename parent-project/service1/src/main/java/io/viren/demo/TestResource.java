package io.viren.demo;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TestResource {

	@Autowired
	private RestTemplate restTempltae;

	@GetMapping("/")
	public String sayHello() {
		log.info("sample log.....!!!");
		return "Hello";
	}

	@GetMapping("/sample")
	public void sample() {
		final MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>(10);
		headers.put("x-b3-traceid", Arrays.asList("b756f1edf56e041a"));
		headers.put("x-b3-spanid", Arrays.asList("4bc8d77a21d0e028"));
		headers.put("x-b3-parentspanid", Arrays.asList("4bc8d77a21d0e028"));
		headers.put("x-b3-sampled", Arrays.asList("0"));

		final HttpHeaders httpHeaders = new HttpHeaders(headers);

		final HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);
		ResponseEntity<String> responseEntity = restTempltae.exchange("http://localhost:8090/", HttpMethod.GET, entity,
				String.class);
		log.info("~~~~~~~~~~~~~~~ Response is {}",
				responseEntity.getStatusCode().is2xxSuccessful() ? "sab badhiya hai" : "pil gae sanam");
	}
}
