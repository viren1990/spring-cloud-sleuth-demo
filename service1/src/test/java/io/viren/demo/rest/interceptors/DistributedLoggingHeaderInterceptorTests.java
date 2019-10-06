package io.viren.demo.rest.interceptors;

import io.viren.demo.request.MockRequestExecution;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

public class DistributedLoggingHeaderInterceptorTests {

    @InjectMocks
    private DistributedLoggingHeaderInterceptor distributedLoggingHeaderInterceptor;
    private static final Pattern ALPHA_NUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]*$");

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
        final ClientHttpResponse response = distributedLoggingHeaderInterceptor.intercept(mockClientHttpRequest, new byte[100], mockRequestExecution);
        Assert.assertNotNull(response);
        // Just to verify mocking behavior provided is working as expected.
        Assert.assertEquals(response , mockClientHttpResponse);

        final HttpHeaders headers = mockClientHttpRequest.getHeaders();
        Assert.assertNotNull(headers);

        final Map<String, String> singleValueMap = headers.toSingleValueMap();
        Assert.assertTrue(!singleValueMap.isEmpty());
        Assert.assertTrue(singleValueMap.size() == 2);

        Assert.assertTrue(singleValueMap.containsKey("x-b3-traceid"));
        final String tracedIdHeaderValue = singleValueMap.get("x-b3-traceid");

        Assert.assertTrue(tracedIdHeaderValue.length() == 16);
        Assert.assertTrue(ALPHA_NUMERIC_PATTERN.matcher(tracedIdHeaderValue).find());

        Assert.assertTrue(singleValueMap.containsKey("x-b3-spanid"));
        final String spanIdHeaderValue = singleValueMap.get("x-b3-spanid");

        Assert.assertTrue(spanIdHeaderValue.length() == 16);
        Assert.assertTrue(ALPHA_NUMERIC_PATTERN.matcher(spanIdHeaderValue).find());

        Mockito.verify(mockRequestExecution , Mockito.times(1)).execute(Mockito.any(HttpRequest.class), Mockito.any(byte[].class));
        Mockito.verifyNoMoreInteractions(mockRequestExecution);
    }

    @Test
    public void test_intercept_failure() throws IOException {
        final ClientHttpResponse response = distributedLoggingHeaderInterceptor.withMessageDigestAlgo("randomAlgo").intercept(mockClientHttpRequest, new byte[100], mockRequestExecution);
        Assert.assertNotNull(response);

        final HttpHeaders headers = mockClientHttpRequest.getHeaders();
        Assert.assertNotNull(headers);

        final Map<String, String> singleValueMap = headers.toSingleValueMap();
        Assert.assertTrue(!singleValueMap.isEmpty());
        Assert.assertTrue(singleValueMap.size() == 2);

        Assert.assertTrue(singleValueMap.containsKey("x-b3-traceid"));
        final String tracedIdHeaderValue = singleValueMap.get("x-b3-traceid");

        Assert.assertTrue(StringUtils.isBlank(tracedIdHeaderValue));

        Assert.assertTrue(singleValueMap.containsKey("x-b3-spanid"));
        final String spanIdHeaderValue = singleValueMap.get("x-b3-spanid");

        Assert.assertTrue(StringUtils.isBlank(spanIdHeaderValue));

        Mockito.verify(mockRequestExecution , Mockito.times(1)).execute(Mockito.any(HttpRequest.class), Mockito.any(byte[].class));
        Mockito.verifyNoMoreInteractions(mockRequestExecution);
    }
}
