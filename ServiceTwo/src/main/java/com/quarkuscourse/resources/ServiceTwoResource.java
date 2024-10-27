package com.quarkuscourse.resources;

import com.quarkuscourse.clients.ServiceOneClient;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.temporal.ChronoUnit;

@Path("/service-two")
@Produces(MediaType.TEXT_PLAIN)
public class ServiceTwoResource {

    @RestClient
    ServiceOneClient serviceOneClient;

    @GET
    public Uni<String> delayedTwoSeconds() {
        return serviceOneClient.delayedTwoSeconds();
    }
    @POST
    @Path("/success")
    //Retry mechanism to handle failures gracefully
    @Retry(delay = 2000,delayUnit = ChronoUnit.MILLIS,maxRetries = 4, retryOn = {RuntimeException.class})
    public Uni<String> postAsyncSuccess(String body) {
        return serviceOneClient.success(body);
    }

    @POST
    @Path("/error")
   // Circuit breaker mechanisms to handle failures gracefully
   // requestVolumeThreshold = 5,failureRatio = 0.75, delay = 10000
    @CircuitBreaker(requestVolumeThreshold = 5,failureRatio = 0.75, delay = 10000)
    public Uni<String> postAsyncError(String body) {
        return serviceOneClient.error(body);
    }
}
