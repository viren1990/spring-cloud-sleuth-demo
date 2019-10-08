package io.viren.demo.rest.exception;

import org.springframework.web.client.RestClientException;

import java.util.Objects;

public class PcfClientException extends  RuntimeException {

	private static final long serialVersionUID = 5816439467963083285L;

	public PcfClientException(final String message ){
        super( message);
    }

    public PcfClientException(final Throwable exception){
        super(exception);
    }

    public PcfClientException(final String message, final Throwable exception){
        super(message,exception);
    }

    public boolean isRestResponseException(){
        if(Objects.nonNull(getCause()) && getCause() instanceof RestClientException) return true;
        return false;
    }

}
