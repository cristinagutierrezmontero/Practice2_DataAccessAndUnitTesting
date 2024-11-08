package com.quarkuscourse.resource;

import com.quarkuscourse.entity.Department;
import com.quarkuscourse.repository.DepartmentRepository;
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


@Path("/departments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentResource {
    @Inject
    DepartmentRepository departmentRepository;

    @GET
    @Path("/{id}")
    public Uni<Response> getById(@PathParam("id") ObjectId id) {
        return departmentRepository.findById(id)
                .onItem().ifNotNull().transform(e -> Response.ok(e).build())
                .onItem().ifNull().continueWith(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity("Department not found").build());
    }
    @POST
    public Uni<Department> create(Department department) {
        return departmentRepository.persist(department);
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> update(@PathParam("id") ObjectId id, Department department) {
        return departmentRepository.findById(id)
                .onItem().ifNotNull().transformToUni(existingDepartment -> {
                    existingDepartment.setName(department.getName());
                    return departmentRepository.update(existingDepartment)
                            .onItem().transform(updated -> Response.ok(updated).build());
                })
                .onItem().ifNull().continueWith(() -> Response.status(Response.Status.NOT_FOUND).entity("Department not found").build()
                );
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") ObjectId id) {
        return departmentRepository.findById(id)
                .onItem().ifNotNull().transformToUni(e -> departmentRepository.delete(e)
                        .onItem().transform(r -> Response.noContent().build())
                )
                .onItem().ifNull().continueWith(() ->
                        Response.status(Response.Status.NOT_FOUND).entity("Department not found").build()
                );

    }
}