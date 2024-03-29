package io.viren.demo.rest.logging;

import org.springframework.http.HttpMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.springframework.util.StreamUtils.copyToByteArray;

@Component
public class DefaultRestLogFormatter implements RestLogFormatter{

    private static final Charset DEFAULT_CHARSET = ISO_8859_1;

    @Override
    public String formatRequest(HttpRequest request, byte[] body)
    {
        final String formattedBody = formatBody(body, getCharset(request));
        return String.format("Request: %s %s %s", request.getMethod(), request.getURI(), formattedBody);
    }

    @Override
    public String formatResponse(ClientHttpResponse response) throws IOException
    {
        final String formattedBody = formatBody(copyToByteArray(response.getBody()), getCharset(response));
        return String.format("Response: %s %s", response.getStatusCode().value(), formattedBody);
    }

    protected String formatBody(byte[] body, Charset charset)
    {
        return new String(body, charset);
    }

    protected Charset getCharset(HttpMessage message)
    {
        return Optional.ofNullable(message.getHeaders().getContentType())
                .map(MediaType::getCharset)
                .orElse(DEFAULT_CHARSET);
    }
}
