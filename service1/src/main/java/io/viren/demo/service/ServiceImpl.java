package io.viren.demo.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.viren.demo.rest.exception.PcfClientException;
import io.viren.demo.rest.template.RestTemplateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;

@org.springframework.stereotype.Service
@Slf4j
public class ServiceImpl implements Service {

    @Autowired
    private RestTemplateWrapper pcfRestTemplateWrapper;

    @Override
    @HystrixCommand(commandKey = "otherService", fallbackMethod = "fallback")
    public boolean callOtherService() {
        String response = pcfRestTemplateWrapper.getForEntity(String.class, "http://localhost:8090/");
        log.info(Objects.isNull(response) ? "No response found from server" : response);
        return true;
    }

    public boolean fallback(final Throwable exception) {
        Assert.isTrue(exception instanceof PcfClientException && ((PcfClientException) exception).isRestResponseException(), "exception should be REST response exception");
        log.error("Causing exception is {} ", exception.getCause().getMessage());
        return false;
    }
}
