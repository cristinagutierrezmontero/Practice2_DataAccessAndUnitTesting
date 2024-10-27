package com.quarkuscourse.interceptors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;

import java.io.IOException;

@Provider
public class CustomHeaderInterceptor implements WriterInterceptor {
    //Interceptor to add a custom header to responses.
    @Override
    public void aroundWriteTo(WriterInterceptorContext writerInterceptorContext) throws IOException, WebApplicationException {
        writerInterceptorContext.getHeaders().add("custom_header","my_custom_header");
        writerInterceptorContext.proceed();
    }
}



