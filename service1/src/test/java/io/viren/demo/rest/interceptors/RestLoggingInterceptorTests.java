package io.viren.demo.rest.interceptors;

import io.viren.demo.request.MockRequestExecution;
import io.viren.demo.rest.logging.DefaultRestLogFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import java.io.IOException;

public class RestLoggingInterceptorTests {

    private RestLoggingInterceptor restLoggingInterceptor = new RestLoggingInterceptor(new DefaultRestLogFormatter());

    @Mock
    private MockRequestExecution mockRequestExecution;
    private MockClientHttpResponse mockClientHttpResponse = new MockClientHttpResponse(new byte[100], HttpStatus.OK);
    private MockClientHttpRequest mockClientHttpRequest = new MockClientHttpRequest();

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockRequestExecution.execute(Mockito.any(HttpRequest.class), Mockito.any(byte[].class))).thenReturn(mockClientHttpResponse);
    }

    @Test
    public void test_intercept_positive() throws IOException {
        final ClientHttpResponse response = restLoggingInterceptor.intercept(mockClientHttpRequest, "testBody".getBytes(), mockRequestExecution);
        Assert.assertNotNull(response);
        // Just to verify mocking behavior provided is working as expected.
        Assert.assertEquals(response , mockClientHttpResponse);

        Mockito.verify(mockRequestExecution , Mockito.times(1)).execute(Mockito.any(HttpRequest.class), Mockito.any(byte[].class));
        Mockito.verifyNoMoreInteractions(mockRequestExecution);
    }
}
