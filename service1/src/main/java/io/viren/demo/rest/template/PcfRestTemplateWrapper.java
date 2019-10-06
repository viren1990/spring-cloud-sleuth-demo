package io.viren.demo.rest.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PcfRestTemplateWrapper extends  RestTemplateWrapper {

    public PcfRestTemplateWrapper(RestTemplate pcfRestTemplate, ObjectMapper objectMapper) {
        super(pcfRestTemplate, objectMapper);
    }
}
