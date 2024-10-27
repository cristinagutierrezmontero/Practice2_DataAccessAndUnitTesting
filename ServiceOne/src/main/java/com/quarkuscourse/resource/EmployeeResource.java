package com.quarkuscourse.resource;

import com.quarkuscourse.entity.Employee;
import com.quarkuscourse.repository.EmployeeRepository;
import io.smallrye.mutiny.Multi;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeResource {

    @Inject
    EmployeeRepository employeeRepository;

    @GET
    public Multi<Employee> getAll() {
        return employeeRepository.streamAll();
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getById(@PathParam("id") ObjectId id) {
        return employeeRepository.findById(id)
                .onItem().ifNotNull().transform(e -> Response.ok(e).build())
                .onItem().ifNull().continueWith(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity("Employee not found").build());
    }

    @POST
    //Retry mechanism to handle failures gracefully
    @Retry(delay = 2000, delayUnit = ChronoUnit.MILLIS, maxRetries = 4, retryOn = {RuntimeException.class})
    public Uni<Employee> create(Employee employee) {
        return employeeRepository.persist(employee);
    }

    @PUT
    // Circuit breaker mechanisms to handle failures gracefully
    // requestVolumeThreshold = 5,failureRatio = 0.75, delay = 10000
    @CircuitBreaker(requestVolumeThreshold = 5,failureRatio = 0.75, delay = 10000)
    @Path("/{id}")
    public Uni<Response> update(@PathParam("id") ObjectId id, Employee employee) {
        return employeeRepository.findById(id)
                .onItem().ifNotNull().transformToUni(existingEmployee -> {
                    existingEmployee.setName(employee.getName());
                    existingEmployee.setDepartment(employee.getDepartment());
                    return employeeRepository.update(existingEmployee)
                            .onItem().transform(updated -> Response.ok(updated).build());
                })
                .onItem().ifNull().continueWith(() -> Response.status(Response.Status.NOT_FOUND).entity("Employee not found").build()
                );
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") ObjectId id) {
        return employeeRepository.findById(id)
                .onItem().ifNotNull().transformToUni(e -> employeeRepository.delete(e)
                        .onItem().transform(r -> Response.noContent().build())
                )
                .onItem().ifNull().continueWith(() ->
                        Response.status(Response.Status.NOT_FOUND).entity("Employee not found").build()
                );

    }
}