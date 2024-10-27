package com.quarkuscourse.clients;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


//REST client that consume the 3 APIs from the service-one microservice
@RegisterRestClient(configKey = "ServiceOneClient")
@Path("service-one")
@Produces(MediaType.TEXT_PLAIN)
public interface ServiceOneClient {

    @GET
    Uni<String> delayedTwoSeconds();

    @POST
    @Path("/success")
    Uni<String> success(String body);

    @POST
    @Path("/error")
    Uni<String> error(String body);
}
