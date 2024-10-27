package com.quarkuscourse.clients;

import com.quarkuscourse.entity.Employee;
import com.quarkuscourse.mappers.ExceptionMapper;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;


//REST client that consume the 5 APIs from the employees microservice
    @RegisterRestClient(configKey = "ServiceEmployeeClient")
    @RegisterProvider(ExceptionMapper.class)
    @Path("employees")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public interface ServiceEmployeeClient {
        @GET
        Uni<List<Employee>> getAll();

        @GET
        @Path("/{id}")
         Uni<Response> getById(String id);

        @POST
        Uni<Employee> create(Employee employee);

        @PUT
        @Path("/{id}")
        Uni<Response> update(String id, Employee employee) ;

        @DELETE
        @Path("/{id}")
         Uni<Response> delete(String id);
    }



