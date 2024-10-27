package com.quarkuscourse.resources;

import com.quarkuscourse.clients.ServiceEmployeeClient;
import com.quarkuscourse.entity.Employee;
import com.quarkuscourse.exceptions.CustomException;
import com.quarkuscourse.mappers.ExceptionMapper;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Path("/service-two/employee")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceTwoEmployeeResource {
    @Inject
    @RestClient
    ServiceEmployeeClient serviceEmployeeClient;

    @GET
    public Uni<List<Employee>> getAll() {
        return serviceEmployeeClient.getAll();
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getById(@PathParam("id") String id) {
        return serviceEmployeeClient.getById(id)
                .onFailure(throwable ->
                        Optional.ofNullable(throwable)
                                .filter(t->t instanceof WebApplicationException)
                                .map(WebApplicationException.class::cast)
                                .map(WebApplicationException::getResponse)
                                .filter(response-> response.getStatus()== Response.Status.NOT_FOUND.getStatusCode())
                                .isPresent()
                ).recoverWithItem(()->Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee doesn't exist for the given employee id").build());
    }


    @POST
    //Retry mechanism to handle failures gracefully
    @Retry(delay = 2000, delayUnit = ChronoUnit.MILLIS, maxRetries = 4, retryOn = {RuntimeException.class})
    public Uni<Employee> create(Employee employee) {
        return serviceEmployeeClient.create(employee);
    }

    @PUT
    @Path("/{id}")
    // Circuit breaker mechanisms to handle failures gracefully
    // requestVolumeThreshold = 5,failureRatio = 0.75, delay = 10000
    @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.75, delay = 10000)
    public Uni<Response> update(@PathParam("id") String id, Employee employee) {
        return serviceEmployeeClient.update(id, employee)
                .onFailure(throwable ->
                        Optional.ofNullable(throwable)
                                .filter(t->t instanceof WebApplicationException)
                                .map(WebApplicationException.class::cast)
                                .map(WebApplicationException::getResponse)
                                .filter(response-> response.getStatus()== Response.Status.NOT_FOUND.getStatusCode())
                                .isPresent()
                ).recoverWithItem(()->Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee doesn't exist for the given employee id").build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") String id) {
        return serviceEmployeeClient.delete(id)
                .onFailure(throwable ->
                        Optional.ofNullable(throwable)
                                .filter(t->t instanceof WebApplicationException)
                                .map(WebApplicationException.class::cast)
                                .map(WebApplicationException::getResponse)
                                .filter(response-> response.getStatus()== Response.Status.NOT_FOUND.getStatusCode())
                                .isPresent()
                ).recoverWithItem(()->Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee doesn't exist for the given employee id").build());

    }

}
