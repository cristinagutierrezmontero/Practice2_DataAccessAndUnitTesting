package com.quarkuscourse.resource;

import com.quarkuscourse.clients.ServiceEmployeeClient;
import com.quarkuscourse.entity.Department;
import com.quarkuscourse.entity.Employee;
import com.quarkuscourse.resources.ServiceTwoEmployeeResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestHTTPEndpoint(ServiceTwoEmployeeResource.class)
class EmployeeResourceTest {


    @InjectMock
    @RestClient
    ServiceEmployeeClient serviceEmployeeClient;

    ObjectId existId = new ObjectId();
    ObjectId notExistId = new ObjectId();

    @BeforeEach
    public void setup() {
        // Clean BD before each test
        //employeeRepository.deleteAll();

        // Insertar algunos datos de prueba
        Department department = new Department();
        department.setName("Tech");
        Employee emp1 = new Employee();
        emp1.setName("Cris");
        emp1.setDepartment(department);
        Employee emp2 = new Employee();
        emp2.setName("Alfonso");
        emp2.setDepartment(department);
        Employee emp3 = new Employee();
        emp3.setName("Ricardo");
        emp3.setDepartment(department);

        Mockito.when(serviceEmployeeClient.getAll())
                .thenReturn(Uni.createFrom().item(Arrays.asList(emp1, emp2)));

        Mockito.when(serviceEmployeeClient.getById(existId.toHexString()))
                .thenReturn(Uni.createFrom().item(() -> Response.status(Response.Status.OK)
                        .entity(emp1).build()));

        Mockito.when(serviceEmployeeClient.getById(notExistId.toHexString()))
                .thenReturn(Uni.createFrom().item(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity(null).build()));

        Mockito.when(serviceEmployeeClient.create(Mockito.any(Employee.class)))
                .thenReturn(Uni.createFrom().item(emp3));


        Mockito.when(serviceEmployeeClient.delete(existId.toHexString()))
                .thenReturn(Uni.createFrom().item(() -> Response.status(Response.Status.NO_CONTENT)
                        .entity(null).build()));

        Mockito.when(serviceEmployeeClient.delete(notExistId.toHexString()))
                .thenReturn(Uni.createFrom().item(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity(null).build()));
    }

    @Test
    public void testGetAllEmployees() {
        given()
                .when().get()
                .then()
                .statusCode(200);
        Mockito.verify(serviceEmployeeClient).getAll();
    }

    @Test
    public void testGetEmployeeById() {
        given()
                .pathParam("id", existId.toHexString())
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .body("name", is("Cris"))
                .body("department.name", is("Tech"));

        Mockito.verify(serviceEmployeeClient).getById(existId.toHexString());
    }


    @Test
    public void testGetNonExistentEmployee() {
        given()
                .pathParam("id", notExistId.toHexString())
                .when().get("/{id}")
                .then()
                .statusCode(404);

        Mockito.verify(serviceEmployeeClient).getById(notExistId.toHexString());
    }


    @Test
    public void testCreateEmployee() {
        Department department = new Department();
        department.setName("Tech");
        Employee emp3 = new Employee();
        emp3.setName("Ricardo");
        emp3.setDepartment(department);

        given()
                .contentType(ContentType.JSON)
                .body(emp3)
                .when().post()
                .then()
                .statusCode(200);

        Mockito.verify(serviceEmployeeClient).create(emp3);
    }

    @Test
    public void testUpdateEmployee() {
        Department department = new Department();
        department.setName("Tech");
        Employee emp1 = new Employee();
        emp1.setName("Cris");
        emp1.setDepartment(department);
        Mockito.when(serviceEmployeeClient.update(Mockito.any(String.class), Mockito.any(Employee.class)))
                .thenReturn(Uni.createFrom().item(() -> Response.status(Response.Status.OK)
                        .entity(emp1).build()));

        given()
                .pathParam("id", existId.toHexString())
                .contentType(ContentType.JSON)
                .body(emp1)
                .when().put("/{id}")
                .then()
                .statusCode(200)
                .body("name", is("Cris"));

        Mockito.verify(serviceEmployeeClient).update(existId.toHexString(), emp1);
    }

    @Test
    public void testUpdateEmployeeByIdNotFound() {

        Department department = new Department();
        department.setName("Tech");
        Employee emp1 = new Employee();
        emp1.setName("Cris");
        emp1.setDepartment(department);

        Mockito.when(serviceEmployeeClient.update(Mockito.anyString(), Mockito.any(Employee.class)))
                .thenReturn(Uni.createFrom().item(() -> Response.status(Response.Status.NOT_FOUND)
                        .entity(null).build()));

        given()
                .pathParam("id", notExistId.toHexString())
                .contentType(ContentType.JSON)
                .body(emp1)
                .when().put("/{id}")
                .then()
                .statusCode(404);
        Mockito.verify(serviceEmployeeClient).update(notExistId.toHexString(), emp1);
    }

    @Test
    public void testDeleteEmployeeById() {
        given()
                .pathParam("id", existId.toHexString())
                .when().delete("/{id}")
                .then()
                .statusCode(204)
                .body(is(""));


        Mockito.verify(serviceEmployeeClient).delete(Mockito.any(String.class));
    }

    @Test
    public void testDeleteEmployeeByIdNotFound() {
        given()
                .pathParam("id", notExistId.toHexString())
                .when().delete("/{id}")
                .then()
                .statusCode(404);

        Mockito.verify(serviceEmployeeClient).delete((notExistId.toHexString()));
    }


}
