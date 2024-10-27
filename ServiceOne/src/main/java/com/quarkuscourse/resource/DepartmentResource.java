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
import org.bson.types.ObjectId;


@Path("/departments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentResource {

    @Inject
    DepartmentRepository departmentRepository;

    @GET
    @Path("/{id}")
    public Uni<Department> getById(@PathParam("id") ObjectId id) {
        return departmentRepository.findById(id);
    }

    @POST
    public Uni<Department> create(Department department) {
        return departmentRepository.persist(department);
    }

    @PUT
    @Path("/{id}")
    public Uni<Department> update(@PathParam("id") ObjectId id, Department department) {
        department.setId(id);
        return departmentRepository.update(department);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Void> delete(@PathParam(value = "id") ObjectId id) {
        return departmentRepository.deleteById(id).replaceWithVoid();
    }
}