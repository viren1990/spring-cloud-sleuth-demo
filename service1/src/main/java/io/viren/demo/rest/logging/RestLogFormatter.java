package io.viren.demo.rest.logging;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public interface RestLogFormatter {

    String formatRequest(HttpRequest request, byte[] body);

    String formatResponse(ClientHttpResponse response) throws IOException;
}
