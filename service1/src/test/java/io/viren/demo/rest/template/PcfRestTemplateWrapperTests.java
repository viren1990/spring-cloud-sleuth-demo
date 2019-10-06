package io.viren.demo.rest.template;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PcfRestTemplateWrapperTests {

    @Mock
    private RestTemplate restTemplate;

    private PcfRestTemplateWrapper pcfRestTemplateWrapper;

    private static final String TEST_BODY_MESSAGE = "\"This is a String\"";

    private static final String TEST_BODY_MESSAGE2 = "This is a String";

    private final ResponseEntity<String> responseEntity = new ResponseEntity<>(TEST_BODY_MESSAGE, HttpStatus.OK);
    private final ResponseEntity<String> erroneousResponseEntity = new ResponseEntity<>(TEST_BODY_MESSAGE2, HttpStatus.OK);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        pcfRestTemplateWrapper = new PcfRestTemplateWrapper(restTemplate, mapper);
    }

    @Test
    public void test_getForEntity_positive() {
        Mockito.doReturn(responseEntity).when(restTemplate).getForEntity(Matchers.anyString(), Matchers.any(Class.class), Matchers.<Object>anyVararg());
        final String response = pcfRestTemplateWrapper.getForEntity(String.class, "/", new Object[5]);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.equalsIgnoreCase("This is a String"));

        Mockito.verify(restTemplate , Mockito.times(1)).getForEntity("/" , String.class , new Object[5]);
        Mockito.verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void test_getForEntity_negative() {
        Mockito.doReturn(erroneousResponseEntity).when(restTemplate).getForEntity(Matchers.anyString(), Matchers.any(Class.class), Matchers.<Object>anyVararg());
        final String response = pcfRestTemplateWrapper.getForEntity(String.class, "/", new Object[5]);
        Assert.assertNull(response);

        Mockito.verify(restTemplate , Mockito.times(1)).getForEntity("/" , String.class , new Object[5]);
        Mockito.verifyNoMoreInteractions(restTemplate);
    }


    @Test
    public void test_getForList_positive() {
        Mockito.doReturn(responseEntity).when(restTemplate).getForEntity(Matchers.anyString(), Matchers.any(Class.class), Matchers.<Object>anyVararg());
        final List<String> response = pcfRestTemplateWrapper.getForList(String.class, "/", new Object[5]);
        Assert.assertNotNull(response);
        Assert.assertTrue(!response.isEmpty());
        Assert.assertTrue(response.size() == 1);
        Assert.assertTrue(response.iterator().next().equals("This is a String"));

        Mockito.verify(restTemplate , Mockito.times(1)).getForEntity("/" , String.class , new Object[5]);
        Mockito.verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void test_PostForEntity_positive() {
        Mockito.doReturn(responseEntity).when(restTemplate).postForEntity(Matchers.anyString(), Matchers.any(HttpEntity.class), Matchers.any(Class.class), Matchers.<Object>anyVararg());
        final String response = pcfRestTemplateWrapper.postForEntity(String.class, "/", new Object(), new Object[5]);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.equalsIgnoreCase("This is a String"));

        Mockito.verify(restTemplate , Mockito.times(1)).postForEntity(Matchers.anyString(), Matchers.any(HttpEntity.class), Matchers.any(Class.class), Matchers.<Object>anyVararg());
        Mockito.verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void test_PutForEntity_positive() {
        Mockito.doReturn(responseEntity).when(restTemplate).exchange(Matchers.anyString(), Matchers.any(HttpMethod.class), Matchers.any(HttpEntity.class), Matchers.any(Class.class), Matchers.<Object>anyVararg());
        final String response = pcfRestTemplateWrapper.putForEntity(String.class, "/", new Object(), new Object[5]);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.equalsIgnoreCase("This is a String"));

        Mockito.verify(restTemplate , Mockito.times(1)).exchange(Matchers.anyString(), Matchers.any(HttpMethod.class), Matchers.any(HttpEntity.class), Matchers.any(Class.class), Matchers.<Object>anyVararg());
        Mockito.verifyNoMoreInteractions(restTemplate);
    }

    @Test
    public void test_delete_positive() {
        Mockito.doNothing().when(restTemplate).delete(Matchers.anyString(), Matchers.<Object>anyVararg());
        final boolean response = pcfRestTemplateWrapper.delete("/", new Object[5]);
        Assert.assertTrue(response);

        Mockito.verify(restTemplate , Mockito.times(1)).delete("/", new Object[5]);
        Mockito.verifyNoMoreInteractions(restTemplate);
    }
}
