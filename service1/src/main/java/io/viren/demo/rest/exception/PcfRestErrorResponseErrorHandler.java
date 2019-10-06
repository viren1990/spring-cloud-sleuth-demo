package io.viren.demo.rest.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class PcfRestErrorResponseErrorHandler extends DefaultResponseErrorHandler {

    /**
     * {@inheritDoc}
     * <p> PCF client code need not to bother handling of particular (client/server/unknownstatuscode) exception,rather {@linkplain PcfClientException} will be adequate enough to alarm of an error.
     * For a particular treatment of any of the exception types , client can however use {@link Throwable#getCause()}
     * <p/>
     */
    @Override
    public void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        final String statusText = response.getStatusText();
        final HttpHeaders headers = response.getHeaders();
        final byte[] body = this.getResponseBody(response);
        final Charset charset = this.getCharset(response);

        Throwable exception;

        switch (statusCode.series()) {
            case CLIENT_ERROR:
                exception = HttpClientErrorException.create(statusCode, statusText, headers, body, charset);
            case SERVER_ERROR:
                exception = HttpServerErrorException.create(statusCode, statusText, headers, body, charset);
            default:
                exception = new UnknownHttpStatusCodeException(statusCode.value(), statusText, headers, body, charset);
        }
        throw new PcfClientException(exception);
    }
}
