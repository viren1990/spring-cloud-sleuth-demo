package io.viren.demo.configuration;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Service1AppConfig {

    @Value("${http.connection.pool.max.total:20}")
    private int httpClientConnectionPoolMaxTotal;

    @Value("${http.connect.timeout:20000}")
    private int connectTimeOut;

    @Value("${http.read.timeout:20000}")
    private int readTimeOut;

    @Value("${http.socket.timeout:20000}")
    private int socketTimeOut;

    @Autowired
    private ClientHttpRequestInterceptor restLoggingInterceptor;

    @Autowired
    private ClientHttpRequestInterceptor distributedLoggingHeaderInterceptor;

    @Autowired
    private ResponseErrorHandler pcfRestErrorResponseHandler;

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        final PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager();
        result.setMaxTotal(httpClientConnectionPoolMaxTotal);
        return result;
    }

    @Bean
    public RequestConfig requestConfig() {
        final RequestConfig result = RequestConfig.custom()
                .setConnectionRequestTimeout(readTimeOut)
                .setConnectTimeout(connectTimeOut)
                .setSocketTimeout(socketTimeOut)
                .build();
        return result;
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {
        final CloseableHttpClient result = HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
        return result;
    }

    @Bean
    public ClientHttpRequestFactory httpComponentsClientHttpRequestFactory(HttpClient httpClient) {
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory httpComponentsClientHttpRequestFactory) {
        final RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
        restTemplate.getInterceptors().add(restLoggingInterceptor);
        return restTemplate;
    }

    @Bean
    public RestTemplate pcfRestTemplate(RestTemplate restTemplate) {
        // Decorate rest template with additional interceptor
        restTemplate.getInterceptors().add(distributedLoggingHeaderInterceptor);
        restTemplate.setErrorHandler(pcfRestErrorResponseHandler);
        return restTemplate;
    }

    @Bean
    public HystrixCommandAspect hystrixAspect() {
        return new HystrixCommandAspect();
    }
}