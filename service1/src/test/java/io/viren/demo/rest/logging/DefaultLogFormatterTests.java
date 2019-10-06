package io.viren.demo.rest.logging;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import java.io.IOException;

public class DefaultLogFormatterTests {

    private DefaultRestLogFormatter formatter = new DefaultRestLogFormatter();

    private MockClientHttpResponse mockClientHttpResponse = new MockClientHttpResponse("testResponse".getBytes(), HttpStatus.OK);
    private MockClientHttpRequest mockClientHttpRequest = new MockClientHttpRequest();

    @Test
    public void test_formatRequest(){
        final String formattedRequest =  formatter.formatRequest(mockClientHttpRequest, "testRequest".getBytes());
        Assert.assertTrue(StringUtils.isNotEmpty(formattedRequest));

        Assert.assertTrue(formattedRequest.contains("GET"));
    }

    @Test
    public void test_formatResponse() throws IOException {
        final String formattedResponse =  formatter.formatResponse(mockClientHttpResponse);
        Assert.assertTrue(StringUtils.isNotEmpty(formattedResponse));

        Assert.assertTrue(formattedResponse.contains("200"));
    }
}
