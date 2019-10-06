package io.viren.demo.rest.interceptors;

import io.viren.demo.rest.logging.RestLogFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestLoggingInterceptor implements ClientHttpRequestInterceptor {

    private RestLogFormatter restLogFormatter;

    private static final Logger LOG = LoggerFactory.getLogger(RestLoggingInterceptor.class);

    @Autowired
    public RestLoggingInterceptor(final RestLogFormatter restLogFormatter){
        this.restLogFormatter = restLogFormatter;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        LOG.info(restLogFormatter.formatRequest(httpRequest,bytes));

        final ClientHttpResponse response =  clientHttpRequestExecution.execute(httpRequest,bytes);

        LOG.info(restLogFormatter.formatResponse(response));
        return response;
    }
}
