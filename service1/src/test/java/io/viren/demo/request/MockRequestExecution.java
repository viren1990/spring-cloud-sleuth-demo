package io.viren.demo.request;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpResponse;

import java.io.IOException;

public class MockRequestExecution implements ClientHttpRequestExecution {

    @Override
    public ClientHttpResponse execute(HttpRequest httpRequest, byte[] bytes) throws IOException {
        return new MockClientHttpResponse(bytes , HttpStatus.OK);
    }
}
