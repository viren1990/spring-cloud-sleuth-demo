package io.viren.demo.rest.exception;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpResponse;

import java.io.IOException;

public class PcfRestErrorResponseErrorHandlerTests {

    private PcfRestErrorResponseErrorHandler pcfRestErrorResponseErrorHandler = new PcfRestErrorResponseErrorHandler();

    private MockClientHttpResponse mockClientHttpResponse = new MockClientHttpResponse("testResponse".getBytes() , HttpStatus.OK);

    @Test(expected =  PcfClientException.class)
    public void test_handleError_whenClientExceptionOccurs() throws IOException {
        pcfRestErrorResponseErrorHandler.handleError(mockClientHttpResponse , HttpStatus.BAD_REQUEST);
    }

    @Test(expected =  PcfClientException.class)
    public void test_handleError_whenServerExceptionOccurs() throws IOException {
        pcfRestErrorResponseErrorHandler.handleError(mockClientHttpResponse , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test(expected =  PcfClientException.class)
    public void test_handleError_whenUnknownExceptionOccurs() throws IOException {
        pcfRestErrorResponseErrorHandler.handleError(mockClientHttpResponse , HttpStatus.MOVED_PERMANENTLY);
    }

}
