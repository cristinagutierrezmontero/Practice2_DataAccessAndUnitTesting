package com.quarkuscourse.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;



@Provider
public class LoggingFilter implements ContainerRequestFilter,
        ContainerResponseFilter {
   //Filter to log request and response details.
    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext)  {

        LOG.info("Call method : "+ containerRequestContext.getRequest().getMethod());
        LOG.info("url: " + containerRequestContext.getUriInfo().getAbsolutePath());
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext)  {
        LOG.info("Response body : " + containerResponseContext.getEntity());
    }
}
