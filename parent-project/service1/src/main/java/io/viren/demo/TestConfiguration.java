package io.viren.demo;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfiguration {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public HttpEntity<String> httpEntity() {
		final MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>(10);
		headers.put("x-b3-traceid", Arrays.asList("b756f1edf56e041a"));
		headers.put("x-b3-spanid", Arrays.asList("b756f1edf56e041a"));

		final HttpHeaders httpHeaders = new HttpHeaders(headers);
		final HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);
		return entity;
	}

}
