package io.viren.demo.rest.interceptors;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Component
@Data
public class DistributedLoggingHeaderInterceptor implements ClientHttpRequestInterceptor {

    private static final String MESSAGE_DIGEST_ALGO_TYPE = "MD5";
    private static final String X_B3_TRACE_ID = "x-b3-traceid";
    private static final String X_B3_SPAN_ID = "x-b3-spanid";

    private String algoName;

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLoggingHeaderInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>(10);
        final String traceId = generate16CharacterHash();
        headers.put(X_B3_TRACE_ID, Arrays.asList(traceId));
        headers.put(X_B3_SPAN_ID, Arrays.asList(traceId));

        final HttpRequestWrapper requestWrapper = new HttpRequestWrapper(httpRequest);
        requestWrapper.getHeaders().addAll(headers);
        return clientHttpRequestExecution.execute(requestWrapper, bytes);
    }

    private String generate16CharacterHash() {
        try {
            String text = RandomStringUtils.randomAlphabetic(10);
            MessageDigest msg = MessageDigest.getInstance(StringUtils.isNotBlank(algoName) ? algoName : MESSAGE_DIGEST_ALGO_TYPE);
            msg.update(text.getBytes(), 0, text.length());
            String digest = new BigInteger(1, msg.digest()).toString(16);
            return digest.substring(0, 16);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error(ExceptionUtils.getStackTrace(ex));
        }
        return StringUtils.EMPTY;
    }

    public DistributedLoggingHeaderInterceptor withMessageDigestAlgo(final String algoName){
        this.algoName = algoName;
        return this;
    }
}

